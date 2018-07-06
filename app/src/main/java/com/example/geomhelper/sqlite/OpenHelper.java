package com.example.geomhelper.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "geomhelper.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "user_data";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_EXPERIENCE = "experience";
    public static final String COLUMN_LEVEL_EXPERIENCE = "level_experience";
    public static final String COLUMN_LEADERBOARDPLACE = "leaderboardplace";
    public static final String COLUMN_WELCOME = "welcome";
    public static final String COLUMN_C = "c";
    public static final String COLUMN_TESTS = "tests";
    public static final String COLUMN_ACHIEVEMENTS = "achievements";
    public static final String COLUMN_VK = "vk";
    public static final String COLUMN_FACEBOOK = "facebook";
    public static final String COLUMN_GOOGLE = "google";
    public static final String COLUMN_DAY_NIGHT = "day_night";
    public static final String COLUMN_SETTINGS = "settings";
    public static final String COLUMN_IMAGE_LEADERS = "image_leaders";
    public static final String COLUMN_DESC = "_desc";

    public static final int NUM_COLUMN_ID = 0;
    public static final int NUM_COLUMN_UID = 1;
    public static final int NUM_COLUMN_NAME = 2;
    public static final int NUM_COLUMN_LEVEL = 3;
    public static final int NUM_COLUMN_EXPERIENCE = 4;
    public static final int NUM_COLUMN_LEVEL_EXPERIENCE = 5;
    public static final int NUM_COLUMN_LEADERBOARDPLACE = 6;
    public static final int NUM_COLUMN_WELCOME = 7;
    public static final int NUM_COLUMN_C = 8;
    public static final int NUM_COLUMN_TESTS = 9;
    public static final int NUM_COLUMN_ACHIEVEMENTS = 10;
    public static final int NUM_COLUMN_VK = 11;
    public static final int NUM_COLUMN_FACEBOOK = 12;
    public static final int NUM_COLUMN_GOOGLE = 13;
    public static final int NUM_COLUMN_DAY_NIGHT = 14;
    public static final int NUM_SETTINGS = 15;
    public static final int NUM_IMAGE_LEADERS = 16;
    public static final int NUM_DESC = 17;

    OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_UID + " TEXT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_LEVEL + " TEXT," +
                COLUMN_EXPERIENCE + " INTEGER," +
                COLUMN_LEVEL_EXPERIENCE + " INTEGER," +
                COLUMN_LEADERBOARDPLACE + " INTEGER," +
                COLUMN_WELCOME + " INTEGER," +
                COLUMN_C + " TEXT," +
                COLUMN_TESTS + " TEXT," +
                COLUMN_ACHIEVEMENTS + " TEXT," +
                COLUMN_VK + " INTEGER," +
                COLUMN_FACEBOOK + " INTEGER," +
                COLUMN_GOOGLE + " INTEGER," +
                COLUMN_DAY_NIGHT + " TEXT," +
                COLUMN_SETTINGS + " INTEGER," +
                COLUMN_IMAGE_LEADERS + " INTEGER," +
                COLUMN_DESC + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}