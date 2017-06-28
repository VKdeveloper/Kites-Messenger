package org.kelkarkul.kitesmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Asgard on 24-06-2017.
 */
public class StorageController extends SQLiteOpenHelper {
    private static final String TABLE_USER = "messenger_user";
    public StorageController(Context context) {
        super(context, "kites_messenger.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,USER_ID TEXT, FNAME TEXT , LNAME TEXT , FULLNAME TEXT ,USER_NUM TEXT,DP_URL TXT)";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<HashMap<String,String>> getUserDet()
    {
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_lite = "select USER_ID,FULLNAME,USER_NUM,DP_URL from "+TABLE_USER;
        Cursor c = db.rawQuery(sql_lite,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String>  k = new HashMap<>();
                k.put("USER_ID",c.getString(0));
                k.put("FULLNAME",c.getString(1));
                k.put("USER_NUM",c.getString(2));
                k.put("DP_URL",c.getString(3));
                // Adding contact to list
                list.add(k);
            } while (c.moveToNext());
        }
        db.close();
        return list;
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
        values.put("USER_STATUS", list.get("USER_STATUS"));
        values.put("USER_NUM",list.get("USER_NUM"));
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        //db.close(); // Closing database connection
        //}
        db.close();
    }
}
