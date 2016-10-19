package com.example.sadokey.tourism_guide.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadokey.tourism_guide.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText Edit_email;
    private EditText Edite_password;
    private TextView txt_signup;
    private Button btn_login;
    private Button btn_sign_up;
    private SignInButton btn_google_login;
    private FirebaseAuth myAuth;
    private DatabaseReference myUserDatabase;
    private GoogleApiClient MyGoogleApiClient;
    private ProgressDialog progressDialog;
    private static int RC_SIGN_IN=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        myAuth = FirebaseAuth.getInstance();
        myUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        txt_signup=(TextView)findViewById(R.id.txt_signup);
        Edit_email=(EditText)findViewById(R.id.editText_login_email);
        Edite_password=(EditText)findViewById(R.id.editText_login_password);
        btn_login=(Button)findViewById(R.id.btn_log_in);
        btn_sign_up=(Button)findViewById(R.id.btn_signUp);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        progressDialog=new ProgressDialog(this);

        btn_google_login=(SignInButton)findViewById(R.id.btn_google_login);
        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressDialog.setTitle("Signing with Google Account");
            progressDialog.show();
            signin();
            }
        });


        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        MyGoogleApiClient=new GoogleApiClient.Builder(this)
            .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Toast.makeText(Login.this, "There is a problem with that email", Toast.LENGTH_SHORT).show();
                }
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
            .build();

        btn_google_login.setSize(SignInButton.SIZE_STANDARD);
        btn_google_login.setScopes(googleSignInOptions.getScopeArray());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            progressDialog.setTitle("Signing with Google Account");
            progressDialog.show();
            checklogin();
            }
        });

    }

    private void signin() {
        Intent signinIntent= Auth.GoogleSignInApi.getSignInIntent(MyGoogleApiClient);
        startActivityForResult(signinIntent, RC_SIGN_IN);
    }

    private void checklogin() {

        String Email=Edit_email.getText().toString().trim();
        String password=Edite_password.getText().toString().trim();

        if (Email.length()==0) {
            Edit_email.setError("Email is Required");
            return;
        }
        if (password.length()==0) {
            Edit_email.setError("Password is Required");
            return;
        }

        myAuth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUserExist();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN)
        {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                FirebaseAuthWithGoogle(account);
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "failed to login with Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void FirebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        myAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
            if(!task.isSuccessful())
            {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Failed To sign in", Toast.LENGTH_SHORT).show();
            }
            else {


                DatabaseReference databaseReference = myUserDatabase.child(myAuth.getCurrentUser().getUid());

                databaseReference.child("fullname").setValue(account.getGivenName()+" "+account.getFamilyName());
                databaseReference.child("username").setValue(account.getDisplayName());
                databaseReference.child("email").setValue(account.getEmail());
                databaseReference.child("status").setValue("user");

                progressDialog.dismiss();
                checkUserExist();
            }
            }
        });
    }

    private void checkUserExist() {
        final String user_id=myAuth.getCurrentUser().getUid();


        myUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user_id).hasChild("phone")) {
                    progressDialog.dismiss();
                    Intent mainIntent = new Intent(Login.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                } else {
                    progressDialog.dismiss();
                    Intent mainIntent = new Intent(Login.this, loginSetup.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
