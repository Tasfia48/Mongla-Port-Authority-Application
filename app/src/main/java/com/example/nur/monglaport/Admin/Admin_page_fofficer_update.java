package com.example.nur.monglaport.Admin;
import android.content.Intent;
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
import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Front;
import com.example.nur.monglaport.UI.Ship;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class Admin_page_fofficer_update extends AppCompatActivity {
    EditText service,prov;
    Button update,delete,add;
    Firebase firebase;
    String string,ser,off,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_fofficer_update);
        Firebase.setAndroidContext(this);
        firebase=new Firebase(Config.FIREBASE_URL);

        service=(EditText)findViewById(R.id.service);
        prov=(EditText)findViewById(R.id.provider);

        update=(Button)findViewById(R.id.update);
        delete=(Button)findViewById(R.id.delete);
        add=(Button)findViewById(R.id.add);

        Bundle bundle = getIntent().getExtras();
        string= bundle.getString("x");
        ser=bundle.getString("service");
        off=bundle.getString("officer");

        if(string.equalsIgnoreCase("add"))add.setVisibility(View.VISIBLE);

        else if(string.equalsIgnoreCase("up")) {
            service.setText(ser);
            prov.setText(off);



            firebase.child("fofficer").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                    String f = String.valueOf(newPost.get("service"));
                    String h = String.valueOf(newPost.get("officer"));
                    if (f.equalsIgnoreCase(ser) && h.equalsIgnoreCase(off)) {
                        id = snapshot.getKey();
                        Toast.makeText(Admin_page_fofficer_update.this, id, Toast.LENGTH_SHORT).show();
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

            firebase.child("fofficer").addListenerForSingleValueEvent(new ValueEventListener() {
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

                Fpofficer fpofficer=new Fpofficer();
                fpofficer.setService(service.getText().toString());
                fpofficer.setOfficer(prov.getText().toString());
                firebase.child("fofficer").child(id).setValue(fpofficer);

                Toast.makeText(Admin_page_fofficer_update.this,"Data updated successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_fofficer_update.this,Admin_page_fpofficer.class);
                startActivity(in);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               firebase.child("fofficer").child(id).removeValue();

                Toast.makeText(Admin_page_fofficer_update.this,"Data deleted successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_fofficer_update.this,Admin_page_fpofficer.class);
                startActivity(in);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prov.getText().toString().equalsIgnoreCase("") || service.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Admin_page_fofficer_update.this,"All field must be filled",Toast.LENGTH_SHORT).show();
                }
                else {
                    Fpofficer fpofficer = new Fpofficer();
                    fpofficer.setOfficer(prov.getText().toString());
                    fpofficer.setService(service.getText().toString());
                    firebase.child("fofficer").push().setValue(fpofficer);

                    Toast.makeText(Admin_page_fofficer_update.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Admin_page_fofficer_update.this, Admin_page_fpofficer.class);
                    startActivity(in);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_fofficer_update.this, Admin_page_fpofficer.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
