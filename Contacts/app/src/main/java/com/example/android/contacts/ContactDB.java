package com.example.android.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "MyContact.db";
    private static final String TABLE_NAME = "GeneralContact";
    private static final String COL0 = "id";
    private static final String COL1 = "name";
    private static final String COL2 = "phone";
    private static final String COL3 = "photo_url";
    private static final String COL4 = "email_id";
    private static final String COL5 = "updateDate";
    Context context;
    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table " + TABLE_NAME +
            " (id integer primary key AUTOINCREMENT, " +
            "name text not null, " +
            "phone text not null, " +
            "photo_url text, " +
            "email_id text, " +
            "updateDate text);";


    public ContactDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        this.db = sqLiteDatabase;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_query = "drop table if exists " + TABLE_NAME;
        sqLiteDatabase.execSQL(drop_query);
        this.onCreate(sqLiteDatabase);
    }

    public void insert(List<Contact> myContactList){
        db = this.getWritableDatabase();
        FragmentContacts.hasDB = true;
        ContentValues values = null;

        for(int i=0; i<myContactList.size(); i++) {
            values = new ContentValues();
            Log.d("name",  myContactList.get(i).name);
            values.put(COL1, myContactList.get(i).name);
            values.put(COL2, myContactList.get(i).phone);
            values.put(COL3, myContactList.get(i).photoURI);
            values.put(COL4, myContactList.get(i).emailID);
            values.put(COL5, myContactList.get(i).updateDate);

            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    public List<Contact> getAllData(){
        db = this.getWritableDatabase();
        List <Contact> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select name, phone, photo_url from " + TABLE_NAME, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(0);
                String phone  = cursor.getString(1);
                String photo = cursor.getString(2);
                Contact contact = new Contact(name,phone,photo);
                res.add(contact);
            }while (cursor.moveToNext());
        }

        return res;
    }
    public boolean doesTableExist() {
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void deleteAllfromTable(){
        db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
