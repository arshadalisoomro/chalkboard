package com.ghofrani.classapp.modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghofrani.classapp.model.DatabaseContract;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_SUNDAY = "create table "
            + DatabaseContract.Sunday.TABLE_NAME + " ("
            + DatabaseContract.Sunday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Sunday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Sunday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Sunday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_MONDAY = "create table "
            + DatabaseContract.Monday.TABLE_NAME + " ("
            + DatabaseContract.Monday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Monday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Monday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Monday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_TUESDAY = "create table "
            + DatabaseContract.Tuesday.TABLE_NAME + " ("
            + DatabaseContract.Tuesday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Tuesday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Tuesday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Tuesday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_WEDNESDAY = "create table "
            + DatabaseContract.Wednesday.TABLE_NAME + " ("
            + DatabaseContract.Wednesday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Wednesday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Wednesday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Wednesday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_THURSDAY = "create table "
            + DatabaseContract.Thursday.TABLE_NAME + " ("
            + DatabaseContract.Thursday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Thursday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Thursday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Thursday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_FRIDAY = "create table "
            + DatabaseContract.Friday.TABLE_NAME + " ("
            + DatabaseContract.Friday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Friday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Friday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Friday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_SATURDAY = "create table "
            + DatabaseContract.Saturday.TABLE_NAME + " ("
            + DatabaseContract.Saturday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Saturday.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Saturday.COLUMN_START_TIME + " TEXT,"
            + DatabaseContract.Saturday.COLUMN_END_TIME + " TEXT)";

    private static final String CREATE_CLASSINFO = "create table "
            + DatabaseContract.ClassInfo.TABLE_NAME + " ("
            + DatabaseContract.ClassInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.ClassInfo.COLUMN_NAME + " TEXT,"
            + DatabaseContract.ClassInfo.COLUMN_TEACHER + " TEXT,"
            + DatabaseContract.ClassInfo.COLUMN_LOCATION + " TEXT)";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SUNDAY);
        db.execSQL(CREATE_MONDAY);
        db.execSQL(CREATE_TUESDAY);
        db.execSQL(CREATE_WEDNESDAY);
        db.execSQL(CREATE_THURSDAY);
        db.execSQL(CREATE_FRIDAY);
        db.execSQL(CREATE_SATURDAY);
        db.execSQL(CREATE_CLASSINFO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertClassIntoDay(String[] classToInsert, int day) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        long result;

        switch (day) {

            case Calendar.SUNDAY:

                contentValues.put(DatabaseContract.Sunday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Sunday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Sunday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Sunday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.MONDAY:

                contentValues.put(DatabaseContract.Monday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Monday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Monday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Monday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.TUESDAY:

                contentValues.put(DatabaseContract.Tuesday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Tuesday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Tuesday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Tuesday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.WEDNESDAY:

                contentValues.put(DatabaseContract.Wednesday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Wednesday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Wednesday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Wednesday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.THURSDAY:

                contentValues.put(DatabaseContract.Thursday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Thursday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Thursday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Thursday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.FRIDAY:

                contentValues.put(DatabaseContract.Friday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Friday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Friday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Friday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.SATURDAY:

                contentValues.put(DatabaseContract.Saturday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Saturday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Saturday.COLUMN_END_TIME, classToInsert[2]);

                result = db.insert(DatabaseContract.Saturday.TABLE_NAME, null, contentValues);

                break;

            default:

                result = -1;

                break;

        }

        return result != -1;

    }

    public boolean addClass(String[] classInfo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        long result;

        contentValues.put(DatabaseContract.ClassInfo.COLUMN_NAME, classInfo[0]);
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_TEACHER, classInfo[1]);
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_LOCATION, classInfo[2]);

        result = db.insert(DatabaseContract.ClassInfo.TABLE_NAME, null, contentValues);

        return result != -1;

    }

    public String getClassLocation(String className) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + className + "'", null);

        if (result.moveToNext()) {

            String resultString = result.getString(3);

            result.close();

            return resultString;

        } else {

            return null;

        }

    }

    public String getClassTeacher(String className) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + className + "'", null);

        if (result.moveToNext()) {

            String resultString = result.getString(2);

            result.close();

            return resultString;

        } else {

            return null;

        }

    }

    public Cursor getClasses(int day) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultSet;

        switch (day) {

            case Calendar.SUNDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Sunday.TABLE_NAME, null);

                break;

            case Calendar.MONDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Monday.TABLE_NAME, null);

                break;

            case Calendar.TUESDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Tuesday.TABLE_NAME, null);

                break;

            case Calendar.WEDNESDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Wednesday.TABLE_NAME, null);

                break;

            case Calendar.THURSDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Thursday.TABLE_NAME, null);

                break;

            case Calendar.FRIDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Friday.TABLE_NAME, null);

                break;

            case Calendar.SATURDAY:

                resultSet = db.rawQuery("select * from " + DatabaseContract.Saturday.TABLE_NAME, null);

                break;

            default:

                resultSet = null;

                break;

        }

        return resultSet;

    }

    public Cursor getClasses() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + DatabaseContract.ClassInfo.TABLE_NAME, null);

    }

}