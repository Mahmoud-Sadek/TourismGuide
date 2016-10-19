package com.example.sadokey.tourism_guide.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sadokey.tourism_guide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {


    private EditText username;
    private EditText fullname;
    private EditText email;
    private EditText password;
    private EditText repassword;

    private DatabaseReference myUserDatabase;
    private FirebaseAuth myAuth;
    private ProgressDialog progressDialog;

    private static boolean validationCheck =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        username=(EditText)findViewById(R.id.editText_Regist_username);
        fullname=(EditText)findViewById(R.id.editText_Regist_fullName);
        email=(EditText)findViewById(R.id.editText_Regist_email);
        password=(EditText)findViewById(R.id.editText_Regist_Password);
        repassword=(EditText)findViewById(R.id.editText_Regist_Re_enter_password);


        myAuth=FirebaseAuth.getInstance();
        myUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog=new ProgressDialog(this);


        Button btn_registration=(Button)findViewById(R.id.btn_registration);
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startRegistration();
            }
        });

    }

    private void startRegistration() {
        validation();

        if (!validationCheck)
            return;

        String txt_email=email.getText().toString().trim();
        final String txt_password=password.getText().toString().trim();

        progressDialog.setTitle("Registration ...");
        progressDialog.show();
        myAuth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String user_id= myAuth.getCurrentUser().getUid();
                    DatabaseReference user_details_database = myUserDatabase.child(user_id);

                    user_details_database.child("full_name").setValue(fullname.getText().toString());
                    //user_details_database.child("password").setValue(txt_password);
                    user_details_database.child("username").setValue(username.getText().toString());
                    user_details_database.child("email").setValue(email.getText().toString());

                    progressDialog.dismiss();

                    Intent mainIntent=new Intent(Registration.this,loginSetup.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validation()
    {
        validationCheck =true;
        if(username.length()==0)
        {
            username.setError("Required");
            validationCheck =false;
        }

        if(fullname.length()==0)
        {
            fullname.setError("Required");
            validationCheck =false;
        }

        else if(!fullname.getText().toString().contains(" "))
        {
            fullname.setError("Enter your first and last name");
            validationCheck =false;
        }

        if(password.length()==0)
        {
            password.setError("Required");
            validationCheck =false;
        }
        if(password.length()<7)
        {
            password.setError("Must be at least 7 character");
            validationCheck =false;
        }

        if(repassword.length()==0)
        {
            username.setError("Required");
            validationCheck =false;
        }
        else if(!password.getText().toString().equals(repassword.getText().toString()))
        {
            repassword.setError("The password not match");
            validationCheck =false;
        }

        if(email.length()==0)
        {
            email.setError("Required");
            validationCheck =false;
        }

    }
}
