package com.example.nur.monglaport.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.nur.monglaport.Admin.Admin_login;
import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.Tide;
import com.example.nur.monglaport.Class.TideArrayAdapter;
import com.example.nur.monglaport.Local_Database.Tidedb;
import com.example.nur.monglaport.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Tide_schedule extends AppCompatActivity {
    ListView listView;
    Firebase firebase;
    ArrayList<Tide> tides,stides;
    ArrayList<String> auto;
    ImageView adlog;
    Button sb;
    AutoCompleteTextView search;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tide);
        listView = (ListView) findViewById(R.id.listv);
        firebase = new Firebase(Config.FIREBASE_URL);
        tides = new ArrayList<Tide>();
        stides = new ArrayList<Tide>();
        auto = new ArrayList<String>();
        sb=(Button)findViewById(R.id.sb);
        search = (AutoCompleteTextView) findViewById(R.id.search);

        setsearch();



        adlog = (ImageView) findViewById(R.id.adlog);

        if (!isInternetConnected(Tide_schedule.this)) {
            Tidedb tidedb = new Tidedb(Tide_schedule.this);
            try {
                tidedb.open();
                tides = tidedb.getData();
                auto=tidedb.getDate();
                tidedb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            Tidedb tidedb = new Tidedb(Tide_schedule.this);

            try {
                tidedb.open();
                tidedb.delete_all();
                tidedb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            onlinedb();

        }


        adlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tide_schedule.this, Admin_login.class);
                intent.putExtra("x", "tide");
                startActivity(intent);
            }
        });

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tidedb tidedb=new Tidedb(Tide_schedule.this);
                try {
                    tidedb.open();
                    stides=tidedb.getbyData(search.getText().toString());
                    set_listnew();
                    tidedb.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }



    public void set_list()
    {
        TideArrayAdapter tideArrayAdapter=new TideArrayAdapter(this,tides);
        listView.setAdapter(tideArrayAdapter);

    }

    public void set_listnew()
    {
        TideArrayAdapter tideArrayAdapter=new TideArrayAdapter(this,stides);
        listView.setAdapter(tideArrayAdapter);

    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public void onlinedb()
    {
        firebase.child("tide").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                Tide tide=new Tide();
                tide.setDate(String.valueOf(newPost.get("date")));
                tide.setTime(String.valueOf(newPost.get("time")));
                tide.setRiver(String.valueOf(newPost.get("river")));
                tide.setHeight(String.valueOf(newPost.get("height")));
                auto.add(String.valueOf(newPost.get("date")));
                tides.add(tide);

                Tidedb tidedb=new Tidedb(Tide_schedule.this);
                try {
                    tidedb.open();
                    tidedb.createEntry(String.valueOf(newPost.get("date")),String.valueOf(newPost.get("river")),String.valueOf(newPost.get("time")),String.valueOf(newPost.get("height")));
                    tidedb.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
                Tidedb tidedb=new Tidedb(Tide_schedule.this);
                try {
                    tidedb.open();
                    auto=tidedb.getDate();
                    tides=tidedb.getData();
                    tidedb.close();
                    setsearch();
                    set_list();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });


    }

    public void setsearch()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,auto);
        search.setAdapter(adapter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Tide_schedule.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
