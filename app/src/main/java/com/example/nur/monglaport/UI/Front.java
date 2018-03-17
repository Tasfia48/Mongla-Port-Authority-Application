package com.example.nur.monglaport.UI;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nur.monglaport.R;

public class Front extends AppCompatActivity {

    LinearLayout history,telephone,navigtion,equipments,officer,ship,location,tide,container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);


        history=(LinearLayout)findViewById(R.id.history);
        telephone=(LinearLayout)findViewById(R.id.telephone);
        navigtion=(LinearLayout)findViewById(R.id.navigation);
        equipments=(LinearLayout)findViewById(R.id.equipments);
        officer=(LinearLayout)findViewById(R.id.officer);
        ship=(LinearLayout)findViewById(R.id.ship);
        location=(LinearLayout)findViewById(R.id.location);
        tide=(LinearLayout)findViewById(R.id.tide);
        container=(LinearLayout)findViewById(R.id.cont);


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,History.class);
                startActivity(intent);
            }
        });

        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Telephone_dir.class);
                startActivity(intent);
            }
        });

        navigtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Navigation.class);
                startActivity(intent);
            }
        });


        officer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,focal_point_off.class);
                startActivity(intent);
            }
        });


        ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Ship.class);
                startActivity(intent);
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Containers.class);
                startActivity(intent);
            }
        });


        equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Equipment.class);
                startActivity(intent);
            }
        });



        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        tide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Front.this,Tide_schedule.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Front.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}