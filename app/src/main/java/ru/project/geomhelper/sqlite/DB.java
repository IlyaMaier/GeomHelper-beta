package ru.project.geomhelper.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;

import ru.project.geomhelper.Person;

import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_ACHIEVEMENTS;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_C;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_EXPERIENCE;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_ID;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL_EXPERIENCE;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_NAME;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_TESTS;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_UID;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_WELCOME;
import static ru.project.geomhelper.sqlite.OpenHelper.TABLE_NAME;

public class DB {

    private SQLiteDatabase sqLiteDatabase;

    public DB(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        sqLiteDatabase = openHelper.getWritableDatabase();
    }

    public long signUp(String uid, String name, int welcome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_UID, uid);
        contentValues.put(COLUMN_WELCOME, welcome);
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public long signIn(String uid, String name, int welcome, String c, String tests,
                       String achievements, int experience, String level, int levelExperience) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_UID, uid);
        contentValues.put(COLUMN_WELCOME, welcome);
        contentValues.put(COLUMN_C, c);
        contentValues.put(COLUMN_TESTS, tests);
        contentValues.put(COLUMN_ACHIEVEMENTS, achievements);
        contentValues.put(COLUMN_EXPERIENCE, experience);
        contentValues.put(COLUMN_LEVEL, level);
        contentValues.put(COLUMN_LEVEL_EXPERIENCE, levelExperience);
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void putString(String column, String value) {
        String f = "UPDATE %s SET %s = '%s' WHERE %s = %s";
        sqLiteDatabase.execSQL(String.format(Locale.US, f, TABLE_NAME, column, value, COLUMN_ID, Person.id));
    }

    public void putInt(String column, int value) {
        String f = "UPDATE %s SET %s = %s WHERE %s = %s";
        sqLiteDatabase.execSQL(String.format(Locale.US, f, TABLE_NAME, column, value, COLUMN_ID, Person.id));
    }

    public String getString(int column) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(Person.id)}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String string = cursor.getString(column);
            cursor.close();
            if (string != null && !string.isEmpty())
                return string;
            else return "";
        } else return "{}";
    }

    public int getInt(int column) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(Person.id)}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int integer = cursor.getInt(column);
            cursor.close();
            return integer;
        } else return -1;
    }

}