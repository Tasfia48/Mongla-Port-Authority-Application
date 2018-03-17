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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.Tide;
import com.example.nur.monglaport.Class.TideArrayAdapter;
import com.example.nur.monglaport.Local_Database.Tidedb;
import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Front;
import com.example.nur.monglaport.UI.Ship;
import com.example.nur.monglaport.UI.Tide_schedule;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Admin_page_tide extends AppCompatActivity {
    ListView listView;
    ArrayList<Tide> tides;
    Firebase firebase;
    ImageView add,logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_tide);
        listView = (ListView) findViewById(R.id.listv);
        tides=new ArrayList<Tide>();
        firebase=new Firebase(Config.FIREBASE_URL);
        add=(ImageView) findViewById(R.id.add);
        logout=(ImageView) findViewById(R.id.logout);

        onlinedbtide();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Admin_page_tide.this,Admin_page_tide_update.class);
                in.putExtra("x","add");
                startActivity(in);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Admin_page_tide.this,Tide_schedule.class);
                Toast.makeText(Admin_page_tide.this,"Logout successfully",Toast.LENGTH_SHORT).show();
                startActivity(in);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Tide tide=(Tide) parent.getItemAtPosition(position);
                Intent in=new Intent(Admin_page_tide.this,Admin_page_tide_update.class);
                in.putExtra("date",tide.getDate());
                in.putExtra("river",tide.getRiver());
                in.putExtra("time",tide.getTime());
                in.putExtra("height",tide.getHeight());
                in.putExtra("x","up");
                startActivity(in);

            }
        });

    }

    public void onlinedbtide() {
        firebase.child("tide").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                Tide tide = new Tide();
                tide.setDate(String.valueOf(newPost.get("date")));
                tide.setTime(String.valueOf(newPost.get("time")));
                tide.setRiver(String.valueOf(newPost.get("river")));
                tide.setHeight(String.valueOf(newPost.get("height")));
                tides.add(tide);

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


        firebase.child("tide").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                set_list();
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }





    public void set_list()
    {
        TideArrayAdapter tideArrayAdapter=new TideArrayAdapter(this,tides);
        listView.setAdapter(tideArrayAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_tide.this, Admin_page_tide.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }

}