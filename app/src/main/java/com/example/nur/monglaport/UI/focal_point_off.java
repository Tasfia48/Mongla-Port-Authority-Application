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
import com.example.nur.monglaport.Class.FpArrayAdapter;
import com.example.nur.monglaport.Class.Fpofficer;
import com.example.nur.monglaport.Class.TideArrayAdapter;
import com.example.nur.monglaport.Local_Database.Tidedb;
import com.example.nur.monglaport.Local_Database.fofficerdb;
import com.example.nur.monglaport.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class focal_point_off extends AppCompatActivity {
    ListView listView;
    Firebase firebase;
    ArrayList<Fpofficer> fpofficers,noff;
    ImageView adlog;
    Button sb;
    ArrayList<String> auto;
    AutoCompleteTextView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focal_point_off);
        listView=(ListView)findViewById(R.id.listv);
        firebase=new Firebase(Config.FIREBASE_URL);
        fpofficers=new ArrayList<Fpofficer>();
        adlog=(ImageView) findViewById(R.id.adlog);
        auto = new ArrayList<String>();
        sb=(Button)findViewById(R.id.sb);
        search = (AutoCompleteTextView) findViewById(R.id.search);



        if(!isInternetConnected(focal_point_off.this))
        {

            fofficerdb fofficerdb=new fofficerdb(focal_point_off.this);
            try {
                fofficerdb.open();
                auto=fofficerdb.getService();
                fpofficers=fofficerdb.getData();
                fofficerdb.close();
                setsearch();
                set_list();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            fofficerdb fofficerdb=new fofficerdb(focal_point_off.this);
            try {
                fofficerdb.open();
                fofficerdb.delete_all();
                fofficerdb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            onlinedb();



        }
        adlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(focal_point_off.this,Admin_login.class);
                intent.putExtra("x","foff");
                startActivity(intent);
            }
        });


        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fofficerdb fofficerdb=new fofficerdb(focal_point_off.this);
                try {
                    fofficerdb.open();
                    noff=fofficerdb.getDatabyService(search.getText().toString());
                    auto=fofficerdb.getService();
                    set_listnew();
                    fofficerdb.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void set_list()
    {
        FpArrayAdapter fpArrayAdapter =new FpArrayAdapter(this,fpofficers);
        listView.setAdapter(fpArrayAdapter);

    }


    public void set_listnew()
    {
        FpArrayAdapter fpArrayAdapter =new FpArrayAdapter(this,noff);
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

                fofficerdb fofficerdb=new fofficerdb(focal_point_off.this);
                try {
                    fofficerdb.open();
                    fofficerdb.createEntry(String.valueOf(newPost.get("service")),String.valueOf(newPost.get("officer")));
                    fofficerdb.close();
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

        firebase.child("f_officer").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                fofficerdb fofficerdb=new fofficerdb(focal_point_off.this);
                try {
                    fofficerdb.open();
                    auto=fofficerdb.getService();
                    fpofficers=fofficerdb.getData();
                    fofficerdb.close();
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
            Intent in = new Intent(focal_point_off.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }

}
