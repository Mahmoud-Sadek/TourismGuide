package com.example.sadokey.tourism_guide.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.Users;
import com.example.sadokey.tourism_guide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class addMembers extends AppCompatActivity {

    private RecyclerView TeamRecyclerView;
    private DatabaseReference myDatabase;
    static Context mycontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        myDatabase= myApplication.getDatabaseReference().child("Users");
        TeamRecyclerView =(RecyclerView)findViewById(R.id.recycleviewTeam);

        TeamRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        TeamRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Users,AdminViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, AdminViewHolder>
                (Users.class,R.layout.userslist,AdminViewHolder.class,myDatabase) {
            @Override
            protected void populateViewHolder(AdminViewHolder viewHolder,Users model,int position)
            {
                String user_id=getRef(position).getKey();
                viewHolder.setName(model.getUsername());
                viewHolder.setImg(model.getImage());
                viewHolder.checkMember(user_id,model.getUsername(),model.getImage());
            }
        };
        TeamRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        CheckBox checkBox;
        DatabaseReference myTeamDatabase;

        public AdminViewHolder(View itemView) {
            super(itemView);
            myView=itemView;
            myTeamDatabase= FirebaseDatabase.getInstance().getReference().child("Team");
            myTeamDatabase=myTeamDatabase.push();
        }

        void setName(String Name)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_All_userName);
            txt_title.setText(Name);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.imageview_All_userName);
            Picasso.with(mycontext).load(uriImg).fit().centerInside().into(imgview);
        }

        void checkMember(final String user_id, final String username, final String image)
        {
            checkBox=(CheckBox)myView.findViewById(R.id.CheckboxUsers);
            myTeamDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child("id").equals(user_id))
                    {
                        checkBox.setChecked(true);
                    }
                    else
                    {
                        checkBox.setChecked(false);
                    }
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        if (checkBox.isChecked())
                        {
                            myTeamDatabase.child(myTeamDatabase.getRef().getKey()).removeValue();
                            Toast.makeText(myView.getContext(), "Is Checked", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            myTeamDatabase.child("id").setValue(user_id);
                            myTeamDatabase.child("username").setValue(username);
                            myTeamDatabase.child("image").setValue(image);
                            Toast.makeText(myView.getContext(), "Is Not Checked", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
