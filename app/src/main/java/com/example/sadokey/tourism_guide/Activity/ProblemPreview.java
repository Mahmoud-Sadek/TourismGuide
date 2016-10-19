package com.example.sadokey.tourism_guide.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadokey.tourism_guide.Classes.Suggestion;
import com.example.sadokey.tourism_guide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProblemPreview extends AppCompatActivity {

    RecyclerView recyclerView;
    static DatabaseReference myDatabase;
    DatabaseReference myApproveProblemDatabase;
    static Context mycontext;
    static String problem_id;

    String subject="";
    String name="";
    String image="";
    String Person_status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_preview);

        mycontext=this;

        myDatabase= FirebaseDatabase.getInstance().getReference().child("problems");

        myApproveProblemDatabase= FirebaseDatabase.getInstance().getReference().child("approveProblems");
        myApproveProblemDatabase=myApproveProblemDatabase.push();

        recyclerView =(RecyclerView)findViewById(R.id.recycleview_approve_list);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Suggestion,problemViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Suggestion, problemViewHolder>
                (Suggestion.class,R.layout.problem_approve,problemViewHolder.class,myDatabase) {
            @Override
            protected void populateViewHolder(final problemViewHolder viewHolder, final Suggestion model, final int position)
            {

                subject=model.getSubject();
                name=model.getName();
                image=model.getImage();


                viewHolder.setsubject(subject);
                viewHolder.setName(name);
                viewHolder.setImg(image);
                viewHolder.setPersonStatus(Person_status);
                viewHolder.setProblemStatus(model.getSuggestionStatus());



                viewHolder.btn_approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            problem_id = getRef(position).getKey();
                            Log.d("Position is ", String.valueOf(position));
                            if (Person_status.equals("not anonymous")) {
                                myApproveProblemDatabase.child("name").setValue(name);
                                myApproveProblemDatabase.child("image").setValue(image);
                            }
                            myApproveProblemDatabase.child("subject").setValue(subject);
                            myDatabase.child(problem_id).removeValue();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ProblemPreview.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewHolder.btn_disapprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        problem_id = getRef(position).getKey();
                        myDatabase.child(problem_id).removeValue();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class problemViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        Button btn_approve;
        Button btn_disapprove;

        public problemViewHolder(View itemView) {
            super(itemView);
            myView=itemView;

            btn_approve = (Button)myView.findViewById(R.id.btn_approve);
            btn_disapprove = (Button)myView.findViewById(R.id.btn_disapprove);
        }


        void setName(String Name)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_SuggestedPlaceName);
            txt_title.setText(Name);
        }

        void setsubject(String Subject)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_ApproveProblem_problem_subject);
            txt_Desc.setText(Subject);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.imageView_ApproveProblem_pesron);
            Picasso.with(mycontext).load(uriImg).fit().centerInside().into(imgview);
        }

        void setProblemStatus(String status)
        {
            TextView txt_person_status=(TextView)myView.findViewById(R.id.txt_ApproveProblem_status);
            txt_person_status.setText(status);
        }
        void setPersonStatus(String status)
        {
            TextView txt_person_status=(TextView)myView.findViewById(R.id.txt_ApprovePerson_status);
            txt_person_status.setText(status);
        }


    }
}
