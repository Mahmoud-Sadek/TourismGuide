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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.offers;
import com.example.sadokey.tourism_guide.R;
import com.example.sadokey.tourism_guide.Activity.AddOffers;
import com.example.sadokey.tourism_guide.Activity.OffersEditDeleteProcess;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OffersFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private DatabaseReference myDatabase;
    private DatabaseReference myOffersDatabase;
    private DatabaseReference myLikeDatabase;
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
        View v = inflater.inflate(R.layout.activity_offers_fragment, container, false);
        mycontext=getContext();

        myDatabase= myApplication.getDatabaseReference();
        myOffersDatabase =myDatabase.child("Offers");
        myLikeDatabase=myDatabase.child("Likes");
        myUserDatabase=myDatabase.child("Users");
        myAuth=FirebaseAuth.getInstance();

        newsRecyclerView =(RecyclerView)v.findViewById(R.id.recycleView_offers);
        newsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
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

        FirebaseRecyclerAdapter<offers,postViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<offers, postViewHolder>
                (offers.class,R.layout.list_offers,postViewHolder.class, myOffersDatabase) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, offers model, int position)
            {
                final String post_id=getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setTime(model.getTime());
                viewHolder.setImg(model.getImage());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setLikeButton(post_id);


                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), OffersEditDeleteProcess.class);
                        intent.putExtra("post_id",post_id);
                        startActivity(intent);
                    }
                });

                viewHolder.btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    check=true;

                    myLikeDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(check)
                            {
                                if (dataSnapshot.child(post_id).hasChild(myAuth.getCurrentUser().getUid()))
                                {
                                    myLikeDatabase.child(post_id).child(myAuth.getCurrentUser().getUid()).removeValue();
                                    check=false;
                                }
                                else {
                                    //just add any value but we will make a good use of it in next short update :)
                                    myLikeDatabase.child(post_id).child(myAuth.getCurrentUser().getUid()).setValue("Random Value");
                                    check=false;
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
            newsRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class  postViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        ImageButton btn_like;
        TextView txt_like_counter;

        DatabaseReference myLikeDatabase;
        FirebaseAuth myAuth;

        public postViewHolder(View itemView) {
            super(itemView);

            myView=itemView;
            btn_like=(ImageButton)myView.findViewById(R.id.imgbtn_likes);
            txt_like_counter=(TextView)myView.findViewById(R.id.txt_offerLikecounter);


            myLikeDatabase=FirebaseDatabase.getInstance().getReference().child("offersLikes");
            myLikeDatabase.keepSynced(true);
            myAuth=FirebaseAuth.getInstance();

        }
        void setLikeButton(final String postKey)
        {
            myLikeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postKey).hasChild(myAuth.getCurrentUser().getUid()))
                    {
                        btn_like.setImageResource(R.mipmap.like);
                    }
                    else
                    {
                        btn_like.setImageResource(R.mipmap.notlike);
                    }

                    txt_like_counter.setText(String.valueOf(dataSnapshot.child(postKey).getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void setPrice(String Price)
        {
            TextView txt_price=(TextView)myView.findViewById(R.id.txt_OffersPrice);
            txt_price.setText(Price);
        }

        void setTitle(String Title)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_OffersTitle);
            txt_title.setText(Title);
        }

        void setDesc(String Desc)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_OffersDetails);
            txt_Desc.setText(Desc);
        }

        void setTime(String time)
        {
            TextView txt_time=(TextView)myView.findViewById(R.id.txt_time);
            txt_time.setText(time);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.offers_imageview);
            Picasso.with(mycontext).load(uriImg).fit().centerInside().into(imgview);
        }
    }
}
