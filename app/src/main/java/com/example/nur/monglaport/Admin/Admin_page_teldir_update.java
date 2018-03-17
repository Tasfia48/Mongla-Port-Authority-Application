package com.example.nur.monglaport.Admin;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.Fpofficer;
import com.example.nur.monglaport.Class.Teleph;
import com.example.nur.monglaport.Class.Tide;
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
import java.util.Map;

public class Admin_page_teldir_update extends AppCompatActivity {
    EditText desig,teleno;
    Button update,delete,add;
    Firebase firebase;
    String des,tl,string,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_teldir_update);

        Bundle bundle = getIntent().getExtras();
        string= bundle.getString("x");
        des=bundle.getString("desig");
        tl=bundle.getString("tele");


        Firebase.setAndroidContext(this);
        firebase=new Firebase(Config.FIREBASE_URL);
        desig=(EditText)findViewById(R.id.desig);
        teleno=(EditText)findViewById(R.id.teleno);

        update=(Button)findViewById(R.id.update);
        delete=(Button)findViewById(R.id.delete);
        add=(Button)findViewById(R.id.add);

        if(string.equalsIgnoreCase("add"))add.setVisibility(View.VISIBLE);

        else if(string.equalsIgnoreCase("up")) {
            desig.setText(des);
            teleno.setText(tl);
            firebase.child("telephone").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                    String f = String.valueOf(newPost.get("designation"));
                    String h = String.valueOf(newPost.get("tel"));
                    if (f.equalsIgnoreCase(des) && h.equalsIgnoreCase(tl)) {
                        id = snapshot.getKey();
                        Toast.makeText(Admin_page_teldir_update.this, id, Toast.LENGTH_SHORT).show();
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
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }

                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teleph teleph=new Teleph();
                teleph.setDesignation(desig.getText().toString());
                teleph.setTel(teleno.getText().toString());

                firebase.child("telephone").child(id).setValue(teleph);

                Toast.makeText(Admin_page_teldir_update.this,"Data updated successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_teldir_update.this,Admin_page_telephone.class);
                startActivity(in);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.child("telephone").child(id).removeValue();

                Toast.makeText(Admin_page_teldir_update.this,"Data deleted successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_teldir_update.this,Admin_page_telephone.class);
                startActivity(in);


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desig.getText().toString().equalsIgnoreCase("") || teleno.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Admin_page_teldir_update.this,"All field must be filled",Toast.LENGTH_SHORT).show();

                }
                Teleph teleph=new Teleph();
                teleph.setDesignation(desig.getText().toString());
                teleph.setTel(teleno.getText().toString());
                firebase.child("telephone").push().setValue(teleph);

                Toast.makeText(Admin_page_teldir_update.this,"Data added successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_teldir_update.this,Admin_page_telephone.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_teldir_update.this, Admin_page_telephone.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
