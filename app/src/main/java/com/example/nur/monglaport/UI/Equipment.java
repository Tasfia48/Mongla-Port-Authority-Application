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

import com.example.nur.monglaport.Class.Config;
import com.example.nur.monglaport.Class.EquipArrayAdapter;
import com.example.nur.monglaport.Class.Equips;
import com.example.nur.monglaport.Local_Database.Equipmentdb;
import com.example.nur.monglaport.R;
import com.firebase.client.Firebase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Equipment extends AppCompatActivity {

    Firebase firebase;
    String res;
    ArrayList<Equips> equipmentlist;
    ArrayList<String> str;
    int i;
    ListView lis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        lis=(ListView)findViewById(R.id.listv);
        firebase = new Firebase(Config.FIREBASE_URL);

        equipmentlist = new ArrayList<Equips>();
        str=new ArrayList<String>();
        res=new String();
        if(isInternetConnected(Equipment.this)==false)
        {
            Toast.makeText(Equipment.this,"You are seeing offline data",Toast.LENGTH_SHORT).show();
            Equipmentdb equipmentdb=new Equipmentdb(Equipment.this);
            try {
                equipmentdb.open();
                equipmentlist=equipmentdb.getData();
                equipmentdb.close();
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
                Document doc = Jsoup.connect("http://www.mpa.gov.bd/bn/handling").get();
                Elements elements = doc.getElementsByClass("panel-body");

                for (Element x : elements) {
                    words += x.getElementsByTag("table");
                }
                words = words.replace("&amp;", "&");
                words = words.replace("&nbsp;", "N/A");


                for (i = 0; i < words.length(); i++) {
                    String x = new String();

                    if (words.charAt(i) == '<' && words.charAt(i + 1) == 't' && words.charAt(i + 2) == 'd') {
                        //res+= Integer.toString(i);
                        while (words.charAt(i) != '>')
                            i++;
                        i++;
                        while (words.charAt(i) != '<') {
                            res += words.charAt(i);
                            x += words.charAt(i);
                            i++;
                        }
                        if (x.equalsIgnoreCase("SL. No.")) {
                            break;
                        }
                        str.add(x);
                    }

                }


                for (i = 0; i < str.size(); i += 5) {
                    Equips equips = new Equips();
                    equips.setSlno(str.get(i));
                    equips.setDesc(str.get(i + 1));
                    equips.setCapacity(str.get(i + 2));
                    equips.setQuantity(str.get(i + 3));
                    equips.setYear(str.get(i + 4));

                    Equipmentdb equipmentdb =new Equipmentdb(Equipment.this);
                    try {
                        equipmentdb.open();
                        equipmentdb.createEntry(str.get(i),str.get(i+1),str.get(i+2),str.get(i+3),str.get(i+4));
                        equipmentdb.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    equipmentlist.add(equips);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
//            Toast.makeText(Equipment.this, Integer.toString(equipmentlist.size()), Toast.LENGTH_SHORT).show();
            set_list();

         firebase.child("Equipment").setValue(res);
        }

    }


public void set_list()
{
    EquipArrayAdapter eq=new EquipArrayAdapter(this,equipmentlist);
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
            Intent in = new Intent(Equipment.this, Front.class);
            startActivity(in);
        }

        return super.onKeyDown(keyCode, event);
    }
}
