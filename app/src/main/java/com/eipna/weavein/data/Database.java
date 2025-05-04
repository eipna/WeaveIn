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
                "full_name TEXT NOT NULL UNIQUE, " +
                "age INTEGER NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "phone_number TEXT NOT NULL UNIQUE);";

        String createPreferencesTable = "CREATE TABLE preferences(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "language TEXT, " +
                "gender TEXT, " +
                "age TEXT, " +
                "country TEXT, " +
                "religion TEXT, " +
                "hobbies TEXT);";

        db.execSQL(createUserTable);
        db.execSQL(createPreferencesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE users;");
        db.execSQL("DROP TABLE preferences;");
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

    public boolean register(String fullName, int age, String gender, String email, String phoneNumber, String password, String type) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("age", age);
        values.put("gender", gender);
        values.put("email", email);
        values.put("phone_number", phoneNumber);
        values.put("password", password);
        values.put("type", type);

        long result = database.insert("users", null, values);

        ContentValues preferencesValue = new ContentValues();
        preferencesValue.put("user_id", result);

        database.insert("preferences", null, preferencesValue);
        database.close();

        return result != -1;
    }

    public User getUser(long userID) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("age")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
            );
            cursor.close();
            database.close();
            return user;
        }
        cursor.close();
        database.close();
        return null;
    }

    public Preferences getPreferences(int userID) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM preferences WHERE user_id = ?", new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            Preferences preferences;
            do {
                preferences = new Preferences();
                preferences.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                preferences.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                preferences.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("language")));
                preferences.setReligion(cursor.getString(cursor.getColumnIndexOrThrow("religion")));
                preferences.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
                preferences.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
                preferences.setHobbies(cursor.getString(cursor.getColumnIndexOrThrow("hobbies")));
                preferences.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            } while (cursor.moveToNext());
            cursor.close();
            database.close();
            return preferences;
        }
        cursor.close();
        database.close();
        return null;
    }

    public boolean updateProfile(User user) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", user.getFullName());
        values.put("age", user.getAge());
        values.put("gender", user.getGender());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("phone_number", user.getPhoneNumber());
        long result = database.update("users", values, "id = ?", new String[]{String.valueOf(user.getID())});
        database.close();

        return result != -1;
    }

    public void updatePreferences(Preferences preferences) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", preferences.getUserID());
        values.put("language", preferences.getLanguage());
        values.put("religion", preferences.getReligion());
        values.put("country", preferences.getCountry());
        values.put("gender", preferences.getGender());
        values.put("age", preferences.getAge());
        values.put("hobbies", preferences.getHobbies());
        database.update("preferences", values, "user_id = ?", new String[]{String.valueOf(preferences.getUserID())});
        database.close();
    }
}