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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class OffersEditDeleteProcess extends AppCompatActivity {

    DatabaseReference MyPostDatabase;
    ProgressDialog progressDialog;
    Uri image_uri=null;
    int GALARY_REQUEST=1;
    ImageButton imageButton;
    StorageReference mystorage;
    EditText edit_header;
    EditText edit_description;
    EditText edit_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_edit_delete_process);

        String post_id = getIntent().getExtras().getString("post_id","");

        MyPostDatabase= FirebaseDatabase.getInstance().getReference().child("Posts").child(post_id);
        mystorage= FirebaseStorage.getInstance().getReference();

        edit_header=(EditText)findViewById(R.id.edit_PreviousOffers_title);
        edit_description=(EditText)findViewById(R.id.edit_PreviousOffers_description);
        edit_price=(EditText)findViewById(R.id.edit_PreviousOffers_price);

        imageButton=(ImageButton)findViewById(R.id.imgbtn_PreviousOffersImage);
        Button btn_save=(Button)findViewById(R.id.btn_save_Offer);
        Button btn_delete=(Button)findViewById(R.id.btn_Delete_offers);
        progressDialog=new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent, GALARY_REQUEST);
            }
        });

        MyPostDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_header.setText(dataSnapshot.child("title").getValue(String.class));
                edit_description.setText(dataSnapshot.child("desc").getValue(String.class));
                edit_price.setText(dataSnapshot.child("price").getValue(String.class));
                Picasso.with(OffersEditDeleteProcess.this).load(dataSnapshot.child("image").getValue(String.class)).fit().centerInside().into(imageButton);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startPost();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MyPostDatabase.removeValue();
                Intent mainIntent = new Intent(OffersEditDeleteProcess.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALARY_REQUEST&&resultCode==RESULT_OK)
        {
            progressDialog.setMessage("Loading Image ...");
            progressDialog.show();

            image_uri = data.getData();
            imageButton.setImageURI(image_uri);

            progressDialog.dismiss();
        }
    }

    private void startPost()
    {
        final String postTitle = edit_header.getText().toString().trim();
        final String postDesc  = edit_description.getText().toString().trim();
        final String postPrice  = edit_price.getText().toString().trim();

        if (!TextUtils.isEmpty(postTitle) &&!TextUtils.isEmpty(postDesc))
        {
            progressDialog.setTitle("Editing ...");
            progressDialog.show();

            if (image_uri!=null) {
                StorageReference filepath = mystorage.child("Blog_Images").child(image_uri.getPathSegments().toString());
                filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloaduri = taskSnapshot.getDownloadUrl();

                        MyPostDatabase.child("title").setValue(postTitle);
                        MyPostDatabase.child("desc").setValue(postDesc);
                        MyPostDatabase.child("price").setValue(postPrice);
                        MyPostDatabase.child("image").setValue(downloaduri.toString());

                        progressDialog.dismiss();

                        Intent mainIntent = new Intent(OffersEditDeleteProcess.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }
                });
            }
            else
            {
                MyPostDatabase.child("title").setValue(postTitle);
                MyPostDatabase.child("desc").setValue(postDesc);
                MyPostDatabase.child("price").setValue(postPrice);
            }
        }
    }
}
