package com.example.nur.monglaport.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Front;
import com.example.nur.monglaport.UI.Ship;

public class Admin_login extends AppCompatActivity {
    EditText name,pass;
    Button login;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        name=(EditText)findViewById(R.id.name);
        pass=(EditText)findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);

        Bundle bundle = getIntent().getExtras();
        string= bundle.getString("x");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equalsIgnoreCase("a") && pass.getText().toString().equalsIgnoreCase("a"))
                {
                    if(string.equalsIgnoreCase("tide"))
                    {
                        Intent in = new Intent(Admin_login.this,Admin_page_tide.class);
                        startActivity(in);

                    }
                        else if((string.equalsIgnoreCase("telephone")))
                        {

                            Intent in = new Intent(Admin_login.this,Admin_page_telephone.class);
                            startActivity(in);

                }

                    else if((string.equalsIgnoreCase("foff")))
                    {
                        Intent in = new Intent(Admin_login.this,Admin_page_fpofficer.class);
                        startActivity(in);

                    }

                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Admin_login.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
