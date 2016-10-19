package com.example.sadokey.tourism_guide.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sadokey.tourism_guide.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Random;

public class AddOffers extends AppCompatActivity {

    ImageButton imageButton;
    EditText edit_offertitle;
    EditText edite_offer_desc;
    EditText edite_offer_place;
    EditText edite_offer_tourismType;
    EditText edite_offer_price;
    int GALARY_REQUEST=1;
    Button btnAddPost;

    ProgressDialog progressDialog;

    Uri image_uri=null;
    StorageReference mystorage;
    DatabaseReference mydatabase;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offers);

        imageButton=(ImageButton)findViewById(R.id.imgbtn_PreviousOffersImage);
        edit_offertitle =(EditText)findViewById(R.id.edit_Place_name);
        edite_offer_desc =(EditText)findViewById(R.id.edit_Places_description);
        edite_offer_place =(EditText)findViewById(R.id.edittext_offer_place);
        edite_offer_tourismType =(EditText)findViewById(R.id.edittext_offer_tourismType);
        edite_offer_price =(EditText)findViewById(R.id.edittext_offer_price);


        btnAddPost=(Button)findViewById(R.id.btn_add_post);

        mystorage= FirebaseStorage.getInstance().getReference();
        mydatabase= FirebaseDatabase.getInstance().getReference().child("offers");
        myAuth= FirebaseAuth.getInstance();


        progressDialog=new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galaryIntent.setType("image/*");
            startActivityForResult(galaryIntent, GALARY_REQUEST);
            }
        });


        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startPost();
            }

        });



    }

    private void startPost()
    {
        final String offerTitle = edit_offertitle.getText().toString().trim();
        final String offerDesc  = edite_offer_desc.getText().toString().trim();
        final String offerplace  = edite_offer_place.getText().toString().trim();
        final String offerprice  = edite_offer_price.getText().toString().trim();
        final String offertourismtype  = edite_offer_tourismType.getText().toString().trim();
        final String company_id = myAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(offerTitle) &&  !TextUtils.isEmpty(offerDesc) && image_uri!=null &&!TextUtils.isEmpty(offerplace)&&!TextUtils.isEmpty(offerprice)&&!TextUtils.isEmpty(offertourismtype))
        {
            progressDialog.setTitle("Posting ...");
            progressDialog.show();

            //StorageReference filepath = mystorage.child("Blog_Images").child(image_uri.getPathSegments().toString());
            StorageReference filepath = mystorage.child("OffersImages").child(RandomName());
            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri = taskSnapshot.getDownloadUrl();

                    DatabaseReference offerspost = mydatabase.push();
                    offerspost.child("title").setValue(offerTitle);
                    offerspost.child("desc").setValue(offerDesc);
                    offerspost.child("place").setValue(offerplace);
                    offerspost.child("price").setValue(offerprice);
                    offerspost.child("tourismType").setValue(offertourismtype);
                    offerspost.child("time").setValue(new Date().toString());
                    offerspost.child("company_id").setValue(company_id);
                    offerspost.child("image").setValue(downloaduri.toString());

                    progressDialog.dismiss();

                    Intent mainIntent = new Intent(AddOffers.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALARY_REQUEST&&resultCode==RESULT_OK)
        {
            progressDialog.setTitle("Loading Image ...");
            progressDialog.show();

            image_uri = data.getData();
            imageButton.setImageURI(image_uri);

            progressDialog.dismiss();
        }
    }

    private String RandomName()
    {
        Random random=new Random();
        StringBuilder stringBuilder =new StringBuilder();
        int length=random.nextInt(20);
        char temp;
        for (int i = 0; i < length; i++) {
            temp = (char)(random.nextInt(96)+32);
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
