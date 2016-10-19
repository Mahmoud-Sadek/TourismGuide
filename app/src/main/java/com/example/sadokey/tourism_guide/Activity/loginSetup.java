package com.example.sadokey.tourism_guide.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.sadokey.tourism_guide.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class loginSetup extends AppCompatActivity {

    private ImageButton imageButton;
    private Button btn_finish_setup;
    private int REQUEST_CODE=1;
    private FirebaseAuth myAuth;
    private StorageReference mystorage;
    private DatabaseReference myUserDatabase;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    private static String gender="Male";
    private EditText phone;
    private String faculty;
    private String year;
    private String department;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_setup);

        imageButton = (ImageButton) findViewById(R.id.imgbtn_setup_choose_photo);
        phone = (EditText) findViewById(R.id.editText_Regist_phone);
        btn_finish_setup = (Button) findViewById(R.id.btn_setup_FinishSetup);

        myAuth = FirebaseAuth.getInstance();
        myUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mystorage = FirebaseStorage.getInstance().getReference().child("ImagesProfile");

        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myMainActivity = new Intent();
                myMainActivity.setType("image/*");
                myMainActivity.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(myMainActivity, REQUEST_CODE);
            }
        });

        init_spinners();



        btn_finish_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Finishing Setup ...");
                progressDialog.show();

                String user_id = myAuth.getCurrentUser().getUid();
                final DatabaseReference user_details_database = myUserDatabase.child(user_id);
                if(imageUri!=null && !TextUtils.isEmpty(phone.getText().toString())) {
                    StorageReference filepath = mystorage.child(user_id);
                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloaduri = taskSnapshot.getDownloadUrl().toString();

                            user_details_database.child("image").setValue(downloaduri);
                            user_details_database.child("phone").setValue(phone.getText().toString());
                            user_details_database.child("gender").setValue(gender);
                            user_details_database.child("faculty").setValue("faculty");
                            user_details_database.child("department").setValue("Department");
                            user_details_database.child("year").setValue("year");

                            progressDialog.dismiss();

                            Intent mainIntent = new Intent(loginSetup.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }

                    });


                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(loginSetup.this, "Filed is empty", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    private void init_spinners()
    {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_Regist_gender);

        final List<String> myGendorList = new ArrayList<String>();
        myGendorList.add("Male");
        myGendorList.add("Female");

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, myGendorList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = myGendorList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {


         if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

             Uri myimageUri = data.getData();
             imageUri=myimageUri;
             imageButton.setImageURI(imageUri);

             CropImage.activity(myimageUri)
                     .setGuidelines(CropImageView.Guidelines.ON)
                     .setAspectRatio(1, 1)
                     .start(this);
         }
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             CropImage.ActivityResult result = CropImage.getActivityResult(data);

             if (resultCode == RESULT_OK) {
                 imageUri = result.getUri();
                 imageButton.setImageURI(imageUri);
             } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
             }
         }
     }


     private String RandomName() {
         Random random = new Random();
         StringBuilder stringBuilder = new StringBuilder();
         int length = random.nextInt(20);
         char temp;
         for (int i = 0; i < length; i++) {
             temp = (char) (random.nextInt(96) + 32);
             stringBuilder.append(temp);
         }
         return stringBuilder.toString();
     }

 }
