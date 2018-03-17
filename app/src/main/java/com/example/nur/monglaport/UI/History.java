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

import com.example.nur.monglaport.Local_Database.Historydb;
import com.example.nur.monglaport.R;
import com.firebase.client.Firebase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

public class History extends AppCompatActivity {

    TextView tx;
    Firebase firebase;
    String res;
    //SharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Firebase.setAndroidContext(this);

        firebase=new Firebase(com.example.nur.monglaport.Class.Config.FIREBASE_URL);

        tx=(TextView)findViewById(R.id.txt);
        res=new String();

        //checking of internet connection if true then parsing website data else showing local database

        if(isInternetConnected(History.this)==false)
        {
            Toast.makeText(History.this,"You are seeing offline data",Toast.LENGTH_SHORT).show();
            Historydb historydb=new Historydb(History.this);
            try {
                historydb.open();
                res=historydb.getData();
                tx.setText(res);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else

        new doit().execute();


    }

    //using JSOUP to parse data from website http://www.mpa.gov.bd/bn/history
    public class doit extends AsyncTask<Void,Void,Void> {
        String words =new String();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc= Jsoup.connect("http://www.mpa.gov.bd/bn/history").get();
                Elements elements=doc.getElementsByClass("panel-body");

                for (Element x : elements) {
                    words+= x.getElementsByTag("p");
                }
                words=words.replace("<div class=\"panel-body\" style=\"padding: 25px\">","");
                words=words.replace("<p align=\"justify\">","");
                words=words.replace("&nbsp"," ");
                words=words.replace("</p>","\n\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            tx.setText(words);
            //firebase.child("History").setValue(words);

            Historydb historydb =new Historydb(History.this);
            try {
                historydb.open();
                historydb.createEntry(words);
                historydb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    //function to checking internet connection

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //functionality of key down

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            Intent in = new Intent(History.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}