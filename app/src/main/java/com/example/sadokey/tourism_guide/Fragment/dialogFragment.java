package com.example.sadokey.tourism_guide.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sadokey.tourism_guide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ahmed AboMazin on 9/13/2016.
 */

public class dialogFragment extends DialogFragment
{
    DatabaseReference SuggestionDatabase;
    DatabaseReference userDatabase;
    FirebaseAuth myAuth;
    String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.pop_form,container,false);

        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myproblemdatabase=myDatabase.child("suggestion");
        SuggestionDatabase = myproblemdatabase.push();
        myAuth=FirebaseAuth.getInstance();

        Button btn_publish=(Button)view.findViewById(R.id.btn_PublsihSuggestion);
        Button btn_cancel=(Button)view.findViewById(R.id.btn_suggestion_cancel);
        final EditText Suggestion_subject=(EditText)view.findViewById(R.id.edittext_Suggestion_subject);
        final EditText Suggestion_place=(EditText)view.findViewById(R.id.edittext_Suggestion_place);
        final EditText Suggestion_price=(EditText)view.findViewById(R.id.edittext_Suggestion_price);
        final RadioButton radio_anonymous=(RadioButton)view.findViewById(R.id.radioButton_anonymous);
        final RadioButton radio_exact=(RadioButton)view.findViewById(R.id.radioButton_exact);

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if(radio_anonymous.isChecked())
                {
                    SuggestionDatabase.child("personStatus").setValue("anonymous");
                }
                else if (radio_exact.isChecked())
                {
                    SuggestionDatabase.child("personStatus").setValue("not anonymous");
                }

                user_id = myAuth.getCurrentUser().getUid();
                userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                userDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("username")) {
                            SuggestionDatabase.child("name").setValue(dataSnapshot.child("username").getValue(String.class));
                        }

                        if (dataSnapshot.hasChild("image")) {
                            SuggestionDatabase.child("image").setValue(dataSnapshot.child("image").getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SuggestionDatabase.child("subject").setValue(Suggestion_subject.getText().toString());
                SuggestionDatabase.child("place").setValue(Suggestion_place.getText().toString());
                SuggestionDatabase.child("price").setValue(Suggestion_price.getText().toString());


                dismiss();

                Toast.makeText(getActivity().getBaseContext(), "Your Suggestion will be seen first by our team", Toast.LENGTH_LONG).show();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
