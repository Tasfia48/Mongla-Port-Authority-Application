package com.example.nur.monglaport.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nur.monglaport.Admin.Admin_page_fpofficer;
import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.ContainerArrayAdapter;
import com.example.nur.monglaport.Class.container;
import com.example.nur.monglaport.Local_Database.Containersdb;
import com.example.nur.monglaport.R;
import com.firebase.client.Firebase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Containers extends AppCompatActivity {
    int i,j;
    ListView lis;
    Firebase firebase;
    ArrayList<container> containers;
    ArrayList<String> str;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_containers);


        lis=(ListView)findViewById(R.id.lst);
        firebase = new Firebase(Config.FIREBASE_URL);

        containers = new ArrayList<container>();
        str=new ArrayList<String>();
        res=new String();

        if(isInternetConnected(Containers.this)==false)
        {
            Toast.makeText(Containers.this,"You are seeing offline data",Toast.LENGTH_SHORT).show();
            Containersdb containersdb=new Containersdb(Containers.this);
            try {
                containersdb.open();
                containers=containersdb.getData();
                containersdb.close();
                set_list();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        else
            new doit().execute();
        //Toast.makeText(Equipment.this, Integer.toString(equipmentlist.size()), Toast.LENGTH_SHORT).show();


    }

    public class doit extends AsyncTask<Void, Void, Void> {
        String words = new String();

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect("http://www.mpa.gov.bd/bng/emptyconrainerreport").get();
                Elements elements = doc.getElementsByClass("table table-striped table-bordered table-hover");

                for (Element x : elements) {
                    words += x.getElementsByTag("tbody");
                }
                words = words.replace("&amp;", "&");

                for (i = 0; i < words.length(); i++) {
                    String x = new String();

                    if (words.charAt(i) == '<' && words.charAt(i + 1) == 't' && words.charAt(i + 2) == 'd') {
                        //res+= Integer.toString(i);
                        while (words.charAt(i) != '>')
                            i++;
                        i++;
                        if (words.charAt(i) == '<') {
                            x = " ";

                        }
                        while (words.charAt(i) != '<') {
                            res += words.charAt(i);
                            x += words.charAt(i);
                            i++;
                        }
                        str.add(x);
                    }

                }


                for (i = 0; i < str.size(); i += 10) {

                    container container=new container();
                    container.setSlno(str.get(i));
                    container.setA_name(str.get(i+1));
                    container.setPhn(str.get(i+2));
                    container.setEm(str.get(i+3));
                    container.setTwd(str.get(i+4));
                    container.setTwr(str.get(i+5));
                    container.setFord(str.get(i+6));
                    container.setForr(str.get(i+7));
                    container.setTd(str.get(i+8));
                    container.setTr(str.get(i+9));


                    Containersdb containersdb=new Containersdb(Containers.this);
                    try {
                        containersdb.open();
                        containersdb.createEntry(str.get(i),str.get(i+1),str.get(i+2),str.get(i+3),str.get(i+4),str.get(i+5),str.get(i+6),str.get(i+7),str.get(i+8),str.get(i+9));
                        containersdb.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    containers.add(container);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
                return null;
            }


        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);

            res=Integer.toString(words.length());

//            Toast.makeText(Containers.this, Integer.toString(containers.size()), Toast.LENGTH_SHORT).show();

            set_list();

        }

    }


    public void set_list()
    {
        ContainerArrayAdapter eq=new ContainerArrayAdapter(this,containers);
        lis.setAdapter(eq);

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
            Intent in = new Intent(Containers.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }

}
