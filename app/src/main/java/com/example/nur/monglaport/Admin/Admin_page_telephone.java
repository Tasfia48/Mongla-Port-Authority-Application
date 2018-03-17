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
import com.example.nur.monglaport.Class.FpArrayAdapter;
import com.example.nur.monglaport.Class.Fpofficer;
import com.example.nur.monglaport.Class.Teleph;
import com.example.nur.monglaport.Class.TelephoneArrayAdapter;
import com.example.nur.monglaport.Local_Database.fofficerdb;
import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Front;
import com.example.nur.monglaport.UI.Ship;
import com.example.nur.monglaport.UI.Telephone_dir;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Admin_page_telephone extends AppCompatActivity {

    ListView listView;
    Firebase firebase;
    ArrayList<Teleph> telephs;
    ImageView add,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_telephone);
        listView=(ListView)findViewById(R.id.listv);
        add=(ImageView) findViewById(R.id.add);
        logout=(ImageView) findViewById(R.id.logout);
        firebase=new Firebase(Config.FIREBASE_URL);
        telephs=new ArrayList<Teleph>();
        onlinedb();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Admin_page_telephone.this,Admin_page_teldir_update.class);
                in.putExtra("x","add");
                startActivity(in);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Admin_page_telephone.this,Telephone_dir.class);
                Toast.makeText(Admin_page_telephone.this,"Logout successfully",Toast.LENGTH_SHORT).show();
                startActivity(in);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Teleph teleph=(Teleph) parent.getItemAtPosition(position);
                Intent in=new Intent(Admin_page_telephone.this,Admin_page_teldir_update.class);
                in.putExtra("desig",teleph.getDesignation());
                in.putExtra("tele",teleph.getTel());
                in.putExtra("x","up");
                startActivity(in);
            }
        });


    }



    public void set_list()
    {
        TelephoneArrayAdapter telephoneArrayAdapter =new TelephoneArrayAdapter(this,telephs);
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
                Teleph tele=new Teleph();
                tele.setDesignation(String.valueOf(newPost.get("designation")));
                tele.setTel(String.valueOf(newPost.get("tel")));

                telephs.add(tele);
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
                set_list();
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_telephone.this, Admin_page_telephone.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
