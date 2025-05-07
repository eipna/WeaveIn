package com.eipna.weavein.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
                "religion TEXT NOT NULL, " +
                "country TEXT NOT NULL, " +
                "language TEXT NOT NULL, " +
                "hobbies TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "phone_number TEXT NOT NULL UNIQUE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "is_private INTEGER NOT NULL);";

        String createPreferencesTable = "CREATE TABLE preferences(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "language TEXT, " +
                "gender TEXT, " +
                "age TEXT, " +
                "country TEXT, " +
                "religion TEXT, " +
                "hobbies TEXT);";

        String createMatchesTable = "CREATE TABLE matches(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_user_id INTEGER NOT NULL, " +
                "second_user_id INTEGER NOT NULL);";

        String createPhotosTable = "CREATE TABLE photos(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "photo BLOB NOT NULL);";

        db.execSQL(createUserTable);
        db.execSQL(createPreferencesTable);
        db.execSQL(createMatchesTable);
        db.execSQL(createPhotosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE users;");
        db.execSQL("DROP TABLE preferences;");
        db.execSQL("DROP TABLE photos;");
        db.execSQL("DROP TABLE matches;");
        onCreate(db);
    }

    public int login(String email, String password) {
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

    public boolean register(User user) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", user.getFullName());
        values.put("age", user.getAge());
        values.put("gender", user.getGender());
        values.put("email", user.getEmail());
        values.put("phone_number", user.getPhoneNumber());
        values.put("password", user.getPassword());
        values.put("type", user.getType());
        values.put("is_private", user.getIsPrivate());
        values.put("religion", user.getReligion());
        values.put("country", user.getCountry());
        values.put("language", user.getLanguage());
        values.put("hobbies", user.getHobbies());

        long result = database.insert("users", null, values);

        ContentValues preferencesValue = new ContentValues();
        preferencesValue.put("user_id", result);

        database.insert("preferences", null, preferencesValue);
        database.close();

        return result != -1;
    }

    public User getUser(int userID) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            user.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("language")));
            user.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
            user.setHobbies(cursor.getString(cursor.getColumnIndexOrThrow("hobbies")));
            user.setReligion(cursor.getString(cursor.getColumnIndexOrThrow("religion")));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            user.setIsPrivate(cursor.getInt(cursor.getColumnIndexOrThrow("is_private")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));

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
        values.put("language", user.getLanguage());
        values.put("religion", user.getReligion());
        values.put("country", user.getCountry());
        values.put("hobbies", user.getHobbies());
        values.put("type", user.getType());
        values.put("is_private", user.getIsPrivate());
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

    public Cursor getNewUsersForToday(String userType) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM users WHERE type = ? AND date(created_at) = date('now');";
        return database.rawQuery(query, new String[]{userType});
    }

    public Cursor getNewUsersForWeek(String userType) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM users WHERE type = ? AND date(created_at) >= date('now', '-7 days');";
        return database.rawQuery(query, new String[]{userType});
    }

    public Cursor getNewUsersForMonth(String userType) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM users WHERE type = ? AND date(created_at) >= date('now', '-30 days');";
        return database.rawQuery(query, new String[]{userType});
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM users;";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
                user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                user.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("language")));
                user.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
                user.setHobbies(cursor.getString(cursor.getColumnIndexOrThrow("hobbies")));
                user.setReligion(cursor.getString(cursor.getColumnIndexOrThrow("religion")));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                user.setIsPrivate(cursor.getInt(cursor.getColumnIndexOrThrow("is_private")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return userList;
    }

    public ArrayList<Preferences> getAllPreference() {
        ArrayList<Preferences> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM preferences;";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Preferences preferences = new Preferences();
                preferences.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                preferences.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                preferences.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("language")));
                preferences.setReligion(cursor.getString(cursor.getColumnIndexOrThrow("religion")));
                preferences.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
                preferences.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
                preferences.setHobbies(cursor.getString(cursor.getColumnIndexOrThrow("hobbies")));
                preferences.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                list.add(preferences);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public void uploadPhoto(int userID, byte[] photo) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userID);
        values.put("photo", photo);
        database.insert("photos", null, values);
        database.close();
    }

    public ArrayList<Photo> getPhotos(int userID) {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Photo> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM photos WHERE user_id = ?;", new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                list.add(new Photo(ID, user_id, photo));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public void match(int firstUserID, int secondUserID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_user_id", firstUserID);
        values.put("second_user_id", secondUserID);
        database.insert("matches", null, values);
        database.close();
    }

    public ArrayList<User> getOtherUsers(int currentUserID) {
        ArrayList<User> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users;", null);

        if (cursor.moveToFirst()) {
            do {
                int userID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                if (userID == currentUserID) {
                    continue;
                }

                User user = new User();
                user.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
                user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                user.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("language")));
                user.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
                user.setHobbies(cursor.getString(cursor.getColumnIndexOrThrow("hobbies")));
                user.setReligion(cursor.getString(cursor.getColumnIndexOrThrow("religion")));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                user.setIsPrivate(cursor.getInt(cursor.getColumnIndexOrThrow("is_private")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
                list.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }
}