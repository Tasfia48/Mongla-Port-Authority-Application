package com.example.nur.monglaport.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.Tide;
import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Front;
import com.example.nur.monglaport.UI.Ship;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class Admin_page_tide_update extends AppCompatActivity {
    EditText dte,time,height,river;
    Button update,delete,add;
    String string,dt,rv,tm,ht,id;
    Firebase firebase;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_tide_update);
        dte=(EditText)findViewById(R.id.date);
        river=(EditText)findViewById(R.id.river);
        time=(EditText)findViewById(R.id.time);
        height=(EditText)findViewById(R.id.height);
        Firebase.setAndroidContext(this);
        update=(Button)findViewById(R.id.update);
        delete=(Button)findViewById(R.id.delete);
        add=(Button)findViewById(R.id.add);
        firebase=new Firebase(Config.FIREBASE_URL);
        myCalendar = Calendar.getInstance();


        Bundle bundle = getIntent().getExtras();
        string= bundle.getString("x");
        dt=bundle.getString("date");
        rv=bundle.getString("river");
        tm=bundle.getString("time");
        ht=bundle.getString("height");

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Admin_page_tide_update.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(string.equalsIgnoreCase("add"))add.setVisibility(View.VISIBLE);

        else if(string.equalsIgnoreCase("up")) {
            dte.setText(dt);
            river.setText(rv);
            time.setText(tm);
            height.setText(ht);

            firebase.child("tide").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();

                    String f=String.valueOf(newPost.get("date"));
                    String h=String.valueOf(newPost.get("river"));
                    String p=String.valueOf(newPost.get("time"));
                    String q=String.valueOf(newPost.get("height"));
                    if(f.equalsIgnoreCase(dt) && h.equalsIgnoreCase(rv) && p.equalsIgnoreCase(tm) &&q.equalsIgnoreCase(ht))
                    {
                        id=snapshot.getKey();
                        Toast.makeText(Admin_page_tide_update.this,id,Toast.LENGTH_SHORT).show();
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

                Tide tide=new Tide();
                tide.setDate(dte.getText().toString());
                tide.setRiver(river.getText().toString());
                tide.setHeight(height.getText().toString());
                tide.setTime(time.getText().toString());
                if (!dte.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
                    Toast.makeText(Admin_page_tide_update.this,"Invalid format of date",Toast.LENGTH_LONG).show();
                }

                else
                {
                    firebase.child("tide").child(id).setValue(tide);
                    Toast.makeText(Admin_page_tide_update.this,"Data updated successfully",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Admin_page_tide_update.this,Admin_page_tide.class);
                    startActivity(in);


                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebase.child("tide").child(id).removeValue();
                Toast.makeText(Admin_page_tide_update.this,"Data deleted successfully",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Admin_page_tide_update.this,Admin_page_tide.class);
                startActivity(in);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dte.getText().toString().equalsIgnoreCase("") || river.getText().toString().equalsIgnoreCase("")|| height.getText().toString().equalsIgnoreCase("") || time.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Admin_page_tide_update.this,"All field must be filled",Toast.LENGTH_SHORT).show();
                }
                else{
                    Tide tide=new Tide();
                    tide.setDate(dte.getText().toString());
                    tide.setRiver(river.getText().toString());
                    tide.setHeight(height.getText().toString());
                    tide.setTime(time.getText().toString());
                    firebase.child("tide").push().setValue(tide);
                    Toast.makeText(Admin_page_tide_update.this,"Data updated successfully",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Admin_page_tide_update.this,Admin_page_tide.class);
                    startActivity(in);



                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_page_tide_update.this, Admin_page_tide.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
    private void updateLabel() {

        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dte.setText(sdf.format(myCalendar.getTime()));
    }
}
