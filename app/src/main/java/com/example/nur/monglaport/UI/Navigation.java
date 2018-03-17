package com.example.nur.monglaport.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nur.monglaport.Local_Database.Navigationdb;
import com.example.nur.monglaport.R;
import com.firebase.client.Firebase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

public class Navigation extends AppCompatActivity {
    TextView tx;
    Firebase firebase;
    String res;
    int cnt=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Firebase.setAndroidContext(this);

        firebase=new Firebase(com.example.nur.monglaport.Class.Config.FIREBASE_URL);
        res=new String();

        tx=(TextView)findViewById(R.id.txt);

        if(isInternetConnected(Navigation.this)==false)
        {
            Toast.makeText(Navigation.this,"You are seeing offline data",Toast.LENGTH_SHORT).show();
             Navigationdb navigationdb=new Navigationdb(Navigation.this);
            try {
                navigationdb.open();
                res=navigationdb.getData();
                tx.setText(res);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else

            new doit().execute();



    }


    public class doit extends AsyncTask<Void,Void,Void> {
        String words =new String();
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc= Jsoup.connect("http://www.mpa.gov.bd/bn/navigation").get();
                Elements elements=doc.getElementsByClass("panel-body");

                for (Element x : elements) {
                    words+= x.getElementsByTag("li");
                }
                words=words.replace("<li>","@");

                //words=words.replace("<p align=\"justify\">","");
                //words=words.replace("&nbsp"," ");
                words=words.replace("</li>","\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            tx.setText(words);
//            firebase.child("Navigation").setValue(words);

            Navigationdb navigationdb =new Navigationdb(Navigation.this);
            try {
                navigationdb.open();
                navigationdb.createEntry(words);
                navigationdb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(Navigation.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
