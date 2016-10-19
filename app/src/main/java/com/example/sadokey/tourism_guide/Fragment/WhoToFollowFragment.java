package com.example.sadokey.tourism_guide.Fragment;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Activity.AddOffers;
import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.company;
import com.example.sadokey.tourism_guide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class WhoToFollowFragment extends Fragment {
    private RecyclerView ComapnyRecyclerView;
    private DatabaseReference myDatabase;
    private DatabaseReference myCompanyDatabase;
    private DatabaseReference myFollowingDatabase;
    private DatabaseReference myUserDatabase;
    private FirebaseAuth myAuth;

    private static Context mycontext;
    private boolean check=false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_offes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(getContext(), AddOffers.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_places_fragment, container, false);
        mycontext=getContext();

        myDatabase= myApplication.getDatabaseReference();
        myCompanyDatabase =myDatabase.child("company");
        myFollowingDatabase =myDatabase.child("following");
        myUserDatabase=myDatabase.child("Users");
        myAuth=FirebaseAuth.getInstance();

        ComapnyRecyclerView =(RecyclerView)v.findViewById(R.id.recyclerviewCompanies);
        ComapnyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        ComapnyRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);


        /*
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        */

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<company,postViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<company, postViewHolder>
                (company.class,R.layout.list_company,postViewHolder.class, myCompanyDatabase) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, company model, int position)
            {
                final String post_id=getRef(position).getKey();

                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImg(model.getImage());
                viewHolder.setFollowButton(post_id);

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(getContext(), companyProfile.class);
                        intent.putExtra("post_id",post_id);
                        startActivity(intent);*/
                    }
                });

                viewHolder.btn_Following.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check = true;

                        myFollowingDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (check) {
                                    if (dataSnapshot.child(post_id).hasChild(myAuth.getCurrentUser().getUid())) {
                                        myFollowingDatabase.child(post_id).child(myAuth.getCurrentUser().getUid()).removeValue();
                                        check = false;
                                    } else {
                                        //just add any value but we will make a good use of it in next short update :)
                                        myFollowingDatabase.child(post_id).child(myAuth.getCurrentUser().getUid()).setValue(myAuth.getCurrentUser().getDisplayName());
                                        check = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        if(myAuth.getCurrentUser()!=null)
            ComapnyRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class  postViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        Button btn_Following;
        TextView txt_following_counter;

        DatabaseReference myCompanyDatabase;
        FirebaseAuth myAuth;

        public postViewHolder(View itemView) {
            super(itemView);

            myView=itemView;
            btn_Following =(Button)myView.findViewById(R.id.btn_follow);
            txt_following_counter =(TextView)myView.findViewById(R.id.txt_followersCounter);


            myCompanyDatabase = FirebaseDatabase.getInstance().getReference().child("following");
            myCompanyDatabase.keepSynced(true);
            myAuth=FirebaseAuth.getInstance();

        }
        void setFollowButton(final String postKey)
        {
            myCompanyDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postKey).hasChild(myAuth.getCurrentUser().getUid())) {
                        btn_Following.setText("Following");
                    } else {
                        btn_Following.setText("Follow");
                    }

                    txt_following_counter.setText(String.valueOf(dataSnapshot.child(postKey).getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        void setName(String Title)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_companyName);
            txt_title.setText(Title);
        }

        void setDesc(String Desc)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_companyDetails);
            txt_Desc.setText(Desc);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.imgView_companyLogo);
            Picasso.with(mycontext).load(uriImg).fit().centerInside().into(imgview);
        }
    }
}

