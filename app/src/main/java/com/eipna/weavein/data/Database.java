package com.eipna.weavein.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "weavein.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "phone_number TEXT NOT NULL UNIQUE);";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE users;");
        onCreate(db);
    }

    public long login(String email, String password) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.moveToFirst()) {
            int userID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            database.close();
            return userID;
        }
        cursor.close();
        database.close();
        return -1;
    }

    public boolean register(String email, String phoneNumber, String password) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("phone_number", phoneNumber);
        values.put("password", password);
        long result = database.insert("users", null, values);
        return result != -1;
    }
}