package com.example.nur.monglaport.Admin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.FpArrayAdapter;
import com.example.nur.monglaport.Class.Fpofficer;
import com.example.nur.monglaport.Class.Tide;
import com.example.nur.monglaport.Local_Database.fofficerdb;
import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.focal_point_off;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Admin_page_fpofficer extends AppCompatActivity {
        ListView listView;
        Firebase firebase;
        ArrayList<Fpofficer> fpofficers;
        ImageView add,logout;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_page_fpofficer);
            listView=(ListView)findViewById(R.id.listv);
            add=(ImageView) findViewById(R.id.add);
            logout=(ImageView) findViewById(R.id.logout);

            firebase=new Firebase(Config.FIREBASE_URL);
            fpofficers=new ArrayList<Fpofficer>();

            onlinedb();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(Admin_page_fpofficer.this,Admin_page_fofficer_update.class);
                    in.putExtra("x","add");
                    startActivity(in);
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(Admin_page_fpofficer.this,focal_point_off.class);
                    Toast.makeText(Admin_page_fpofficer.this,"Logout successfully",Toast.LENGTH_SHORT).show();
                    startActivity(in);
                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Fpofficer fpofficer=(Fpofficer) parent.getItemAtPosition(position);
                    Intent in=new Intent(Admin_page_fpofficer.this,Admin_page_fofficer_update.class);
                    in.putExtra("service",fpofficer.getService());
                    in.putExtra("officer",fpofficer.getOfficer());
                    in.putExtra("x","up");
                    startActivity(in);
                }
            });


        }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_fpofficer.this, Admin_page_fpofficer.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }




    public void set_list()
    {
        FpArrayAdapter fpArrayAdapter =new FpArrayAdapter(this,fpofficers);
        listView.setAdapter(fpArrayAdapter);

    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public void onlinedb()
    {
        firebase.child("fofficer").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                Fpofficer fpofficer=new Fpofficer();
                fpofficer.setService(String.valueOf(newPost.get("service")));
                fpofficer.setOfficer(String.valueOf(newPost.get("officer")));
                fpofficers.add(fpofficer);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child("f_officer").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                set_list();
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

}
