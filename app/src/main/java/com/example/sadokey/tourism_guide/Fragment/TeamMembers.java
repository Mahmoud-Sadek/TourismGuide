package com.example.sadokey.tourism_guide.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.Users;
import com.example.sadokey.tourism_guide.R;
import com.example.sadokey.tourism_guide.Activity.addMembers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class TeamMembers extends Fragment {

    private RecyclerView TeamRecyclerView;
    private DatabaseReference myDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_member,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add_member)
        {
            startActivity(new Intent(getContext(), addMembers.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_team_members,container,false);

        myDatabase= myApplication.getDatabaseReference().child("Team");
        TeamRecyclerView =(RecyclerView)v.findViewById(R.id.myteamRecyclerView);

        TeamRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        TeamRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        final FirebaseRecyclerAdapter<Users,TeamViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, TeamViewHolder>
                (Users.class,R.layout.list_team,TeamViewHolder.class,myDatabase) {
            @Override
            protected void populateViewHolder(final TeamViewHolder viewHolder, final Users model, final int position)
            {
                viewHolder.setName(model.getUsername());
                viewHolder.setImg(model.getImage());
                viewHolder.setJob(model.getJob());
            }
        };
        TeamRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class TeamViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public TeamViewHolder(View itemView) {
            super(itemView);
            myView=itemView;
        }

        void setName(String Name)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_All_userName);
            txt_title.setText(Name);
        }

        void setJob(String job)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_All_userName);
            txt_title.setText(job);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.imageview_All_userName);
            Picasso.with(myView.getContext()).load(uriImg).fit().centerInside().into(imgview);
        }
    }
}
