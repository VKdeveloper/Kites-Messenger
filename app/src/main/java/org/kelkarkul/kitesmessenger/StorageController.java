package org.kelkarkul.kitesmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asgard on 24-06-2017.
 */
class StorageController extends SQLiteOpenHelper {
    private static final String TABLE_USER = "messenger_user";
    private static final String TABLE_MSG = "messenger_messages";
    protected StorageController(Context context) {
        super(context, "kites_messenger.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,USER_ID TEXT, FNAME TEXT , LNAME TEXT , FULLNAME TEXT ,USER_NUM TEXT,DP_URL TEXT)";
        String CREATE_USER_MSG = "CREATE TABLE " + TABLE_MSG + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,USER_ID TEXT, MSG TEXT,MSG_STAT TEXT)";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<HashMap<String,String>> getUserDet()
    {
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select ID,FULLNAME,USER_NUM,DP_URL from "+TABLE_USER;
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String>  k = new HashMap<>();
                k.put("ID",c.getString(0));
                k.put("FULLNAME",c.getString(1));
                k.put("USER_NUM",c.getString(2).trim());
                k.put("DP_URL",c.getString(3));
                // Adding contact to list
                list.add(k);
            } while (c.moveToNext());
        }
        db.close();
        return list;
    }
    public String getUser(String n)
    {
        String user = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select ID from "+TABLE_USER+" where USER_NUM = '"+n+"'";
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
                user = c.getString(0);
                // Adding contact to list
            } while (c.moveToNext());
        }
        db.close();
        return user;
    }

    public void insertUser(HashMap<String,String> list)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //for(int i=0 ; i < list.size(); i++) {
        values.put("USER_ID", list.get("USER_ID"));
        values.put("FNAME", list.get("FNAME"));
        values.put("LNAME", list.get("LNAME"));
        values.put("FULLNAME", list.get("FULLNAME"));
        values.put("DP_URL", list.get("DP_URL"));
        values.put("USER_NUM",list.get("USER_NUM"));
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        //db.close(); // Closing database connection
        //}
        db.close();
    }

    public void insertMsg(HashMap<String,String> list)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //for(int i=0 ; i < list.size(); i++) {
        values.put("USER_ID", list.get("USER_ID"));
        values.put("MSG", list.get("MSG"));
        values.put("MSG_STAT","N");
        // Inserting Row
        db.insert(TABLE_MSG, null, values);
        //db.close(); // Closing database connection
        //}
        db.close();
    }

    public String getMsg(String u)
    {
        String user ="";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select MSG from "+TABLE_MSG+" where USER_ID = '"+u+"'";
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
                user = c.getString(0);
                // Adding contact to list
            } while (c.moveToNext());
        }
        db.close();
        return user;
    }

    public ArrayList<HashMap<String,String>> getConv(String n)
    {
        ArrayList<HashMap<String,String>> map = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select MSG,MSG_STAT from "+TABLE_MSG+" where USER_ID = '"+n+"'";
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String>  k = new HashMap<>();
                k.put("MSG",c.getString(0));
                k.put("MSG_STAT",c.getString(1));
                map.add(k);
                // Adding contact to list
            } while (c.moveToNext());
        }
        db.close();
        return map;
    }

    public String getMsgStat(String n)
    {
        String map = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select MSG_STAT from "+TABLE_MSG+" where USER_ID = '"+n+"'";
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
               // HashMap<String,String>  k = new HashMap<>();
                map = c.getString(0);
                // Adding contact to list
            } while (c.moveToNext());
        }
        db.close();
        return map;
    }
}
