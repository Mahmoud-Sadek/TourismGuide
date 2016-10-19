package com.example.sadokey.tourism_guide.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.Suggestion;
import com.example.sadokey.tourism_guide.R;
import com.example.sadokey.tourism_guide.Activity.ProblemPreview;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class SuggestionFragment extends android.support.v4.app.Fragment {
    private RecyclerView problemRecyclerView;
    private DatabaseReference myDatabase;
    static Context mycontext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_see_problem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_see_problem)
        {
            startActivity(new Intent(getContext(), ProblemPreview.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_suggestion_fragment, container, false);

        Button button =(Button)v.findViewById(R.id.btn_add_problem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialogFragment mydialogFragment=new dialogFragment();
                mydialogFragment.show(getActivity().getFragmentManager(),null);
            }
        });

        mycontext=getContext();
        myDatabase= myApplication.getDatabaseReference().child("suggestion");
        problemRecyclerView =(RecyclerView)v.findViewById(R.id.recycleview_suggestion);

        problemRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        problemRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        

        return v;
    }



    @Override
    public void onStart() {
        super.onStart();

            final FirebaseRecyclerAdapter<Suggestion,problemViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Suggestion, problemViewHolder>
                    (Suggestion.class,R.layout.list_suggestion,problemViewHolder.class,myDatabase) {
                @Override
                protected void populateViewHolder(final problemViewHolder viewHolder, final Suggestion model, final int position)
                {
                    viewHolder.setsubject(model.getSubject());
                    viewHolder.setName(model.getName());
                    viewHolder.setImg(model.getImage());
                    viewHolder.setPlace(model.getPlace());
                    viewHolder.setPrice(model.getPrice());
                }
            };
        problemRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    private static class problemViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public problemViewHolder(View itemView) {
            super(itemView);
            myView=itemView;
        }

        void setName(String Name)
        {
            TextView txt_title=(TextView)myView.findViewById(R.id.txt_suggested_pesron_name);
            if (Name!=null)
                txt_title.setText(Name);
            else
                txt_title.setText("Some one");
        }

        void setsubject(String Subject)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_suggested_subject);
            txt_Desc.setText(Subject);
        }

        void setImg(String uriImg)
        {
            ImageView imgview=(ImageView)myView.findViewById(R.id.imageView_Suggested_pesron);
            if (uriImg!=null)
                Picasso.with(mycontext).load(uriImg).fit().centerInside().into(imgview);
            else
                imgview.setImageResource(R.mipmap.person);
        }

        void setPlace(String Place)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_suggested_place);
            txt_Desc.setText(Place);
        }

        void setPrice(String Price)
        {
            TextView txt_Desc=(TextView)myView.findViewById(R.id.txt_suggested_price);
            txt_Desc.setText(Price);
        }
    }
}