package com.example.sadokey.tourism_guide.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Application_config.myApplication;
import com.example.sadokey.tourism_guide.Classes.DrawerListViewItem;
import com.example.sadokey.tourism_guide.Fragment.AboutUs;
import com.example.sadokey.tourism_guide.Fragment.Help;
import com.example.sadokey.tourism_guide.Fragment.HomeFragment;
import com.example.sadokey.tourism_guide.Fragment.Notification;
import com.example.sadokey.tourism_guide.Fragment.TeamMembers;
import com.example.sadokey.tourism_guide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerPane;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Fragment fragment;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthStateListener;
    DatabaseReference myUserDatabase;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent LoginIntent=new Intent(MainActivity.this,Login.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }
            }
        };


        myAuth = FirebaseAuth.getInstance();
        myAuth.addAuthStateListener(myAuthStateListener);

        myUserDatabase = myApplication.getDatabaseReference().child("Users");
        myUserDatabase.keepSynced(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        drawerPane=(RelativeLayout)findViewById(R.id.drawerpane);

        checkUserExist();

        if (myAuth.getCurrentUser()!=null)
        {
            initListView();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        //checkUserExist();
        if (myAuth.getCurrentUser()!=null) {
            myUserDatabase.addValueEventListener(new ValueEventListener() {
                FirebaseAuth myAuth1 = FirebaseAuth.getInstance();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (myAuth1.getCurrentUser() != null) {
                        String user_id = myAuth1.getCurrentUser().getUid();

                        if (dataSnapshot.child(user_id).hasChild("image")) {
                            ImageView Profile_imageView = (ImageView) findViewById(R.id.profile_imgview);
                            Picasso.with(MainActivity.this).load(dataSnapshot.child(user_id).child("image").getValue(String.class))
                                    .fit().centerInside().into(Profile_imageView);
                        }
                        if (dataSnapshot.child(user_id).hasChild("username")) {
                            TextView Profile_textView = (TextView) findViewById(R.id.profile_name);
                            Profile_textView.setText(dataSnapshot.child(user_id).child("username").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        if (myAuth.getCurrentUser()!=null)
            actionBarDrawerToggle.syncState();

        super.onPostCreate(savedInstanceState);
    }

    private void initListView() {
        ListView listView=(ListView)findViewById(R.id.drawerlistView);
        List<DrawerListViewItem> mylist=new ArrayList<DrawerListViewItem>();
        mylist.add(new DrawerListViewItem("Home",R.mipmap.home));
        mylist.add(new DrawerListViewItem("About", R.mipmap.about));
        mylist.add(new DrawerListViewItem("Help", R.mipmap.help));
        mylist.add(new DrawerListViewItem("Notification", R.mipmap.notification));
        mylist.add(new DrawerListViewItem("Team", R.mipmap.team));
        mylist.add(new DrawerListViewItem("Log Out", R.mipmap.lock));

        DrawerListViewAdapter drawerListViewAdapter=new DrawerListViewAdapter(this.getBaseContext(),mylist);
        listView.setAdapter(drawerListViewAdapter);


        final List<Fragment> myFragmentslist=new ArrayList<Fragment>();
        myFragmentslist.add(new HomeFragment());
        myFragmentslist.add(new AboutUs());
        myFragmentslist.add(new Help());
        myFragmentslist.add(new Notification());
        myFragmentslist.add(new TeamMembers());


        fragment = myFragmentslist.get(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, fragment).commit();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 5) {
                    myAuth.signOut();

                } else {
                    fragment = myFragmentslist.get(position);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, fragment).commit();
                }

                drawerLayout.closeDrawer(drawerPane);
            }
        });



        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }

    public class DrawerListViewAdapter extends BaseAdapter {
        List<DrawerListViewItem> mylist;
        Context context;

        public DrawerListViewAdapter(Context context , List<DrawerListViewItem> mylist)
        {
            this.context=context;
            this.mylist=mylist;
        }

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Object getItem(int position) {
            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final View myView=getLayoutInflater().inflate(R.layout.drawer_listview,parent,false);

            ImageView drawer_imageview =(ImageView)myView.findViewById(R.id.drawer_imageView);
            TextView txt_drawer_header =(TextView)myView.findViewById(R.id.txt_drawer_header);

            DrawerListViewItem item =  mylist.get(position);

            drawer_imageview.setImageResource(item.getImgResource());
            txt_drawer_header.setText(item.getHeaderText());

            return myView;
        }
    }

    private void checkUserExist() {

        if(myAuth.getCurrentUser() !=null) {
            final String user_id = myAuth.getCurrentUser().getUid();
            myUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(MainActivity.this, Login.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                    else if (!dataSnapshot.child(user_id).hasChild("phone")) {
                        Intent mainIntent = new Intent(MainActivity.this, loginSetup.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {

            Intent mainIntent = new Intent(MainActivity.this, Login.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }

    }

}
