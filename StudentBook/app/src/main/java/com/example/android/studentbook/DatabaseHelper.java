package com.example.android.studentbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by IOT on 3/22/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "StudentBook.db";
    private static final String TABLE_NAME = "StudentInfo";
    private static final String COL0 = "id";
    private static final String COL1 = "name";
    private static final String COL2 = "date_of_birth";
    private static final String COL3 = "gender";
    private static final String COL4 = "email_id";
    private static final String COL5 = "contact";
    private static final String COL6 = "username";
    private static final String COL7 = "password";

    private static final String TABLE_CREATE = "create table " + TABLE_NAME +
            " (id integer primary key AUTOINCREMENT, " +
            "name text not null, " +
            "date_of_birth text not null, " +
            "gender text not null, " +
            "email_id text not null, " +
            "contact text not null, " +
            "username text not null, " +
            "password text not null);";
    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_query = "drop table if exists " + TABLE_NAME;
        db.execSQL(drop_query);
        this.onCreate(db);

    }

    public void insert(Student student){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL1, student.name);
        values.put(COL2, student.dateofbirth);
        values.put(COL3, student.gender);
        values.put(COL4, student.emailAdress);
        values.put(COL5, student.contact);
        values.put(COL6, student.username);
        values.put(COL7, student.password);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPassword (String username){
        db = this.getReadableDatabase();
        String tempUsername, tempPassword ="";
        String serachQuery = "select username, password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(serachQuery, null);

        if(cursor.moveToFirst()){
            do{
                tempUsername = cursor.getString(0);
               if(tempUsername.equals(username)){
                   tempPassword = cursor.getString(1);
                   break;
               }

            }while (cursor.moveToNext());
        }
        return tempPassword;
    }


    public  Cursor getAllData(){
        db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public String[] getInfo(String username) {
        db = this.getReadableDatabase();
        String[] res = new String[6];
        String tempUsername, tempPassword ="";
        String serachQuery = "select username, name, date_of_birth, gender, email_id, contact, password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(serachQuery, null);

        if(cursor.moveToFirst()){
            do{
                tempUsername = cursor.getString(0);
                if(tempUsername.equals(username)){
                    res[0] = cursor.getString(1);
                    res[1] = cursor.getString(2);
                    res[2] = cursor.getString(3);
                    res[3] = cursor.getString(4);
                    res[4] = cursor.getString(5);
                    res[5] = cursor.getString(6);
                    break;
                }

            }while (cursor.moveToNext());
        }
        return res;
    }

    public byte[] getImage(String username) {
        db = this.getReadableDatabase();
        byte[] res = null;
        String tempUsername;
//         String serachQuery = "select image, username from " + TABLE_NAME;
        String serachQuery = "select image from " + TABLE_NAME + " where username = '" + username + "' ;";
        Cursor cursor = db.rawQuery(serachQuery, null);

        res = cursor.getBlob(0);
//        if(cursor.moveToFirst()){
//            do{
//                if(!cursor.isNull(cursor.getColumnIndex("username"))) {
//                    tempUsername = cursor.getString(cursor.getColumnIndex("username"));
//
//                    if (tempUsername.equals(username)) {
//                        res = cursor.getBlob(cursor.getColumnIndex("image"));
//                        break;
//                    }
//                }
//                else{
//                    Log.e("image", "null");
//                }
//
//            }while (cursor.moveToNext());
//        }
        return res;
    }


}

