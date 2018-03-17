
package com.example.nur.monglaport.Local_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nur.monglaport.Class.Fpofficer;

import java.sql.SQLException;
import java.util.ArrayList;

public class fofficerdb {

    public static final String KEY_SERVICE="service";
    public static final String KEY_OFFICER="officer";



    private static final String DATABASE_NAME="monglafocoff";
    private static final String DATABASE_TABLE="mfopiou";
    private static final int DATABASE_VERSION=1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" +
                    KEY_SERVICE + " TEXT, "+
                    KEY_OFFICER + " TEXT) ;"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);

        }
    }

    public fofficerdb(Context c)
    {
        ourContext=c;
    }


    public fofficerdb open() throws SQLException
    {
        ourHelper=new DbHelper(ourContext);
        ourDatabase=ourHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        ourHelper.close();
    }

    public void createEntry(String service, String officer)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_SERVICE,service);
        cv.put(KEY_OFFICER,officer);

        if(!match(service,officer) || isTableExists(ourDatabase,DATABASE_TABLE)==false )
            ourDatabase.insert(DATABASE_TABLE, null, cv);
    }




    public boolean match(String service, String officer) {

        String[] columns = new String[]{KEY_SERVICE, KEY_OFFICER};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        int iSer = c.getColumnIndex(KEY_SERVICE);
        int iOff = c.getColumnIndex(KEY_OFFICER);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iSer).equalsIgnoreCase(service) && c.getString(iOff).equalsIgnoreCase(officer)   )
            {
                return true;
            }


        }
        return false;
    }




    public ArrayList<Fpofficer> getData()
    {

        String[] columns=new String[]{ KEY_SERVICE, KEY_OFFICER};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        ArrayList<Fpofficer> fpofficers=new ArrayList<Fpofficer>();
        int i=0;
        int iSer = c.getColumnIndex(KEY_SERVICE);
        int iOff = c.getColumnIndex(KEY_OFFICER);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            Fpofficer fpofficer=new Fpofficer();
            fpofficer.setService(c.getString(iSer));
            fpofficer.setOfficer(c.getString(iOff));
            fpofficers.add(fpofficer);
        }

        return fpofficers;
    }



    public ArrayList<String> getService()
    {

        String[] columns=new String[]{ KEY_SERVICE};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        ArrayList<String> fservices=new ArrayList<String>();
        int i=0;
        int iSer = c.getColumnIndex(KEY_SERVICE);


        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            fservices.add(c.getString(iSer));
        }

        return fservices;
    }


    public ArrayList<Fpofficer> getDatabyService(String x)
    {

        String[] columns=new String[]{ KEY_SERVICE, KEY_OFFICER};
        Cursor c=ourDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        ArrayList<Fpofficer> fpofficers=new ArrayList<Fpofficer>();
        int i=0;
        int iSer = c.getColumnIndex(KEY_SERVICE);
        int iOff = c.getColumnIndex(KEY_OFFICER);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            if(c.getString(iSer).equalsIgnoreCase(x))
            {
                Fpofficer fpofficer=new Fpofficer();
                fpofficer.setService(c.getString(iSer));
                fpofficer.setOfficer(c.getString(iOff));
                fpofficers.add(fpofficer);

            }
        }

        return fpofficers;
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
    public void delete_all(){
        ourDatabase.delete(DATABASE_TABLE, null, null);
    }
}
