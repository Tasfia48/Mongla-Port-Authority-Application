package com.example.nur.monglaport.Local_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nur.monglaport.Class.container;

import java.sql.SQLException;
import java.util.ArrayList;

public class Containersdb {

    public static final String KEY_ROWID="_SLNO";
    public static final String KEY_AGENT="Agent";
    public static final String KEY_PHONE="Phone";
    public static final String KEY_EMAIL="Email";
    public static final String KEY_20D="T20D";
    public static final String KEY_20R="T20R";
    public static final String KEY_40D="T40D";
    public static final String KEY_40R="T40R";
    public static final String KEY_TD="TD";
    public static final String KEY_TR="TR";

    private static final String DATABASE_NAME="Contnerd23";
    private static final String DATABASE_TABLE="Conttb345";
    private static final int DATABASE_VERSION=1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper{


        public DbHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" +
                    KEY_ROWID + " TEXT, "+
                    KEY_AGENT + " TEXT, "+
                    KEY_PHONE + " TEXT, "+
                    KEY_EMAIL + " TEXT, "+
                    KEY_20D+" TEXT, "+
                    KEY_20R+" TEXT, "+
                    KEY_40D+" TEXT, "+
                    KEY_40R+" TEXT, "+
                    KEY_TD+" TEXT, "+
                    KEY_TR+" TEXT);"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);

        }
    }

    public Containersdb(Context c)
    {
        ourContext=c;
    }


    public Containersdb open() throws SQLException
    {
        ourHelper=new DbHelper(ourContext);
        ourDatabase=ourHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        ourHelper.close();
    }

    public void createEntry(String slno, String A_name, String phn, String em, String twd,String twr, String ford,String forr, String td,String tr)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_ROWID,slno);
        cv.put(KEY_AGENT,A_name);
        cv.put(KEY_PHONE, phn);
        cv.put(KEY_EMAIL,em);
        cv.put(KEY_20D,twd);
        cv.put(KEY_20R,twr);
        cv.put(KEY_40D,ford);
        cv.put(KEY_40R,forr);
        cv.put(KEY_TD,td);
        cv.put(KEY_TR,tr);


        if(!match(slno,A_name,phn,em,twd,twr,ford,forr,td,tr) || isTableExists(ourDatabase,DATABASE_TABLE)==false )
            ourDatabase.insert(DATABASE_TABLE, null, cv);
    }




    public boolean match(String slno, String A_name, String phn, String em, String twd,String twr, String ford,String forr, String td,String tr) {

        String[] columns = new String[]{KEY_ROWID, KEY_AGENT,KEY_PHONE,KEY_EMAIL,KEY_20D,KEY_20R,KEY_40D,KEY_40R,KEY_TD,KEY_TR};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        int iSL = c.getColumnIndex(KEY_ROWID);
        int iAge = c.getColumnIndex(KEY_AGENT);
        int iPhn= c.getColumnIndex(KEY_PHONE);
        int iEm= c.getColumnIndex(KEY_EMAIL);
        int iTwd= c.getColumnIndex(KEY_20D);
        int iTwr= c.getColumnIndex(KEY_20R);
        int iFord= c.getColumnIndex(KEY_40D);
        int iForr= c.getColumnIndex(KEY_40R);
        int iTd= c.getColumnIndex(KEY_TD);
        int iTr= c.getColumnIndex(KEY_TR);



        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iSL).equalsIgnoreCase(slno) && c.getString(iAge).equalsIgnoreCase(A_name)  && c.getString(iPhn).equalsIgnoreCase(phn) && c.getString(iEm).equalsIgnoreCase(em)  && c.getString(iTwd).equalsIgnoreCase(twd)&& c.getString(iTwr).equalsIgnoreCase(twr)&& c.getString(iFord).equalsIgnoreCase(ford)&& c.getString(iForr).equalsIgnoreCase(forr) && c.getString(iTd).equalsIgnoreCase(td) && c.getString(iTr).equalsIgnoreCase(tr)    )
            {
                return true;
            }


        }
        return false;
    }




    public ArrayList<container> getData()
    {

        String[] columns=new String[]{ KEY_ROWID,KEY_AGENT,KEY_PHONE,KEY_EMAIL,KEY_20D,KEY_20R,KEY_40D,KEY_40R,KEY_TD,KEY_TR};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        ArrayList<container> containers=new ArrayList<container>();

        int slno=c.getColumnIndex(KEY_ROWID);
        int iAge = c.getColumnIndex(KEY_AGENT);
        int iPhn= c.getColumnIndex(KEY_PHONE);
        int iEm= c.getColumnIndex(KEY_EMAIL);
        int iTwd= c.getColumnIndex(KEY_20D);
        int iTwr= c.getColumnIndex(KEY_20R);
        int iFord= c.getColumnIndex(KEY_40D);
        int iForr= c.getColumnIndex(KEY_40R);
        int iTd= c.getColumnIndex(KEY_TD);
        int iTr= c.getColumnIndex(KEY_TR);


        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {


            container container=new container();
            container.setSlno(c.getString(slno));
            container.setA_name(c.getString(iAge));
            container.setPhn(c.getString(iPhn));
            container.setEm(c.getString(iEm));
            container.setTwd(c.getString(iTwd));
            container.setTwr(c.getString(iTwr));
            container.setFord(c.getString(iFord));
            container.setForr(c.getString(iForr));
            container.setTd(c.getString(iTd));
            container.setTr(c.getString(iTr));
            containers.add(container);
            }

        return containers;
    }

    boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

}
