package com.example.nur.monglaport.UI;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.Toast;

import com.example.nur.monglaport.Admin.Admin_login;
import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.Teleph;
import com.example.nur.monglaport.Class.TelephoneArrayAdapter;
import com.example.nur.monglaport.Class.TideArrayAdapter;
import com.example.nur.monglaport.Local_Database.Telephonedb;
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

public class Telephone_dir extends AppCompatActivity {
    ListView listView;
    Firebase firebase;
    ArrayList<Teleph> telephs,ntel;
    ImageView adlog;
    Button sb; AutoCompleteTextView search;
    ArrayList<String> auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_dir);
        listView=(ListView)findViewById(R.id.listv);
        firebase=new Firebase(Config.FIREBASE_URL);
        telephs=new ArrayList<Teleph>();
        adlog=(ImageView) findViewById(R.id.adlog);
        sb=(Button)findViewById(R.id.sb);
        auto = new ArrayList<String>();
        ntel=new ArrayList<Teleph>();
        search = (AutoCompleteTextView) findViewById(R.id.search);

        if(!isInternetConnected(Telephone_dir.this))
        {
            Telephonedb telephonedb=new Telephonedb(Telephone_dir.this);
            try {
                telephonedb.open();
                telephs=telephonedb.getData();
                auto=telephonedb.getDesig();
                telephonedb.close();
                set_list();
                setsearch();
                //Toast.makeText(Telephone_dir.this,Integer.toString(auto.size()),Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Telephonedb telephonedb=new Telephonedb(Telephone_dir.this);
            try {
                telephonedb.open();
                telephonedb.delete_all();
                telephonedb.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            onlinedb();



        }
        adlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Telephone_dir.this, Admin_login.class);
                intent.putExtra("x","telephone");
                startActivity(intent);
            }
        });

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Telephonedb telephonedb=new Telephonedb(Telephone_dir.this);
                try {
                    telephonedb.open();
                    ntel=telephonedb.getDatabyDesing(search.getText().toString());
                    set_listnew();
                    telephonedb.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void set_list()
    {
        TelephoneArrayAdapter telephoneArrayAdapter=new TelephoneArrayAdapter(this,telephs);
        listView.setAdapter(telephoneArrayAdapter);

    }
    public void set_listnew()
    {
        TelephoneArrayAdapter telephoneArrayAdapter=new TelephoneArrayAdapter(this,ntel);
        listView.setAdapter(telephoneArrayAdapter);

    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public void onlinedb()
    {
        firebase.child("telephone").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                Teleph teleph=new Teleph();
                teleph.setDesignation(String.valueOf(newPost.get("designation")));
                teleph.setTel(String.valueOf(newPost.get("tel")));

                telephs.add(teleph);

                Telephonedb telephonedb=new Telephonedb(Telephone_dir.this);
                try {
                    telephonedb.open();
                    telephonedb.createEntry(String.valueOf(newPost.get("designation")),String.valueOf(newPost.get("tel")));
                    telephonedb.close();
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

        firebase.child("telephone").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Telephonedb telephonedb=new Telephonedb(Telephone_dir.this);
                try {
                    telephonedb.open();
                    telephs=telephonedb.getData();
                    auto=telephonedb.getDesig();
                    telephonedb.close();
                    set_list();
                    setsearch();
                   // Toast.makeText(Telephone_dir.this,Integer.toString(auto.size()),Toast.LENGTH_SHORT).show();
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
            Intent in = new Intent(Telephone_dir.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }


}
