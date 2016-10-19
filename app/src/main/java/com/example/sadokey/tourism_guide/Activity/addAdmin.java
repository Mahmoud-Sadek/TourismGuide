package com.example.sadokey.tourism_guide.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.Users;
import com.example.sadokey.tourism_guide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class addAdmin extends AppCompatActivity {

    private RecyclerView AdminRecyclerView;
    private DatabaseReference myDatabase;
    static Context mycontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        myDatabase= myApplication.getDatabaseReference().child("Admin");
        AdminRecyclerView =(RecyclerView)findViewById(R.id.recycleviewAdmin);

        AdminRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        AdminRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Users,AdminViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, AdminViewHolder>
                (Users.class,R.layout.userslist,AdminViewHolder.class,myDatabase) {
            @Override
            protected void populateViewHolder(final AdminViewHolder viewHolder, final Users model, final int position)
            {
                viewHolder.setName(model.getUsername());
                viewHolder.setImg(model.getImage());
            }
        };
        AdminRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public AdminViewHolder(View itemView) {
            super(itemView);
            myView=itemView;
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
    }
}
