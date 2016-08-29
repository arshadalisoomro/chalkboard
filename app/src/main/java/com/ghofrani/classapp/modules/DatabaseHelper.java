package com.ghofrani.classapp.modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghofrani.classapp.model.DatabaseContract;
import com.ghofrani.classapp.model.Homework;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

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
            + DatabaseContract.ClassInfo.COLUMN_LOCATION + " TEXT,"
            + DatabaseContract.ClassInfo.COLUMN_COLOR + " INTEGER)";

    private static final String CREATE_HOMEWORK = "create table "
            + DatabaseContract.Homework.TABLE_NAME + " ("
            + DatabaseContract.Homework.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Homework.COLUMN_NAME + " TEXT,"
            + DatabaseContract.Homework.COLUMN_CLASS + " TEXT,"
            + DatabaseContract.Homework.COLUMN_DATE_TIME + " TEXT,"
            + DatabaseContract.Homework.COLUMN_ATTACH + " BOOLEAN)";

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
        db.execSQL(CREATE_HOMEWORK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1 && newVersion == 2) {

            db.execSQL(CREATE_HOMEWORK);

        }

    }

    public boolean insertClassIntoDay(String[] classToInsert, int day) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        long result;

        switch (day) {

            case Calendar.SUNDAY:

                contentValues.put(DatabaseContract.Sunday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Sunday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Sunday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Sunday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.MONDAY:

                contentValues.put(DatabaseContract.Monday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Monday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Monday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Monday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.TUESDAY:

                contentValues.put(DatabaseContract.Tuesday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Tuesday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Tuesday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Tuesday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.WEDNESDAY:

                contentValues.put(DatabaseContract.Wednesday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Wednesday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Wednesday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Wednesday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.THURSDAY:

                contentValues.put(DatabaseContract.Thursday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Thursday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Thursday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Thursday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.FRIDAY:

                contentValues.put(DatabaseContract.Friday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Friday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Friday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Friday.TABLE_NAME, null, contentValues);

                break;

            case Calendar.SATURDAY:

                contentValues.put(DatabaseContract.Saturday.COLUMN_CLASS, classToInsert[0]);
                contentValues.put(DatabaseContract.Saturday.COLUMN_START_TIME, classToInsert[1]);
                contentValues.put(DatabaseContract.Saturday.COLUMN_END_TIME, classToInsert[2]);

                result = sqLiteDatabase.insert(DatabaseContract.Saturday.TABLE_NAME, null, contentValues);

                break;

            default:

                result = -1;

                break;

        }

        sqLiteDatabase.close();

        return result != -1;

    }

    public void removeClassOutOfDay(int day, String name, String startTime) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        switch (day) {

            case Calendar.SUNDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Sunday.TABLE_NAME + " where "
                        + DatabaseContract.Sunday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Sunday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.MONDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Monday.TABLE_NAME + " where "
                        + DatabaseContract.Monday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Monday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.TUESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Tuesday.TABLE_NAME + " where "
                        + DatabaseContract.Tuesday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Tuesday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.WEDNESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Wednesday.TABLE_NAME + " where "
                        + DatabaseContract.Wednesday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Wednesday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.THURSDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Thursday.TABLE_NAME + " where "
                        + DatabaseContract.Thursday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Thursday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.FRIDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Friday.TABLE_NAME + " where "
                        + DatabaseContract.Friday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Friday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

            case Calendar.SATURDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Saturday.TABLE_NAME + " where "
                        + DatabaseContract.Saturday.COLUMN_CLASS + "='"
                        + name + "' and "
                        + DatabaseContract.Saturday.COLUMN_START_TIME + "='"
                        + startTime + "'");

                break;

        }

        sqLiteDatabase.close();

    }

    public void deleteAllClassesOfDay(int day) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        switch (day) {

            case Calendar.SUNDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Sunday.TABLE_NAME);

                break;

            case Calendar.MONDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Monday.TABLE_NAME);

                break;

            case Calendar.TUESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Tuesday.TABLE_NAME);

                break;

            case Calendar.WEDNESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Wednesday.TABLE_NAME);

                break;

            case Calendar.THURSDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Thursday.TABLE_NAME);

                break;

            case Calendar.FRIDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Friday.TABLE_NAME);

                break;

            case Calendar.SATURDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Saturday.TABLE_NAME);

                break;

        }

        sqLiteDatabase.close();

    }

    public boolean checkIfClassExists(String classToCheck) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + classToCheck + "' COLLATE NOCASE", null);

        boolean exists = cursor.moveToNext();

        cursor.close();

        return exists;

    }

    public boolean addClass(String[] classInfo) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        long result;

        contentValues.put(DatabaseContract.ClassInfo.COLUMN_NAME, classInfo[0]);
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_TEACHER, classInfo[1]);
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_LOCATION, classInfo[2]);
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_COLOR, classInfo[3]);

        result = sqLiteDatabase.insert(DatabaseContract.ClassInfo.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();

        return result != -1;

    }

    public String getClassLocation(String className) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + className + "'", null);

        if (cursor.moveToNext()) {

            String resultString = cursor.getString(3);

            cursor.close();

            sqLiteDatabase.close();

            return resultString;

        } else {

            cursor.close();

            sqLiteDatabase.close();

            return null;

        }

    }

    public String getClassTeacher(String className) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + className + "'", null);

        if (cursor.moveToNext()) {

            String resultString = cursor.getString(2);

            cursor.close();

            sqLiteDatabase.close();

            return resultString;

        } else {

            cursor.close();

            sqLiteDatabase.close();

            return null;

        }

    }

    public int getClassColor(String className) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                + DatabaseContract.ClassInfo.COLUMN_NAME + "='"
                + className + "'", null);

        if (cursor.moveToNext()) {

            String resultString = cursor.getString(4);

            cursor.close();

            sqLiteDatabase.close();

            return Integer.parseInt(resultString);

        } else {

            cursor.close();

            sqLiteDatabase.close();

            return 0;

        }

    }

    public Cursor getClasses(int day) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        switch (day) {

            case Calendar.SUNDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Sunday.TABLE_NAME + " ORDER BY ROWID", null);

                break;

            case Calendar.MONDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Monday.TABLE_NAME + " ORDER BY ROWID", null);

                break;

            case Calendar.TUESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Tuesday.TABLE_NAME + " ORDER BY ROWID", null);

                break;

            case Calendar.WEDNESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Wednesday.TABLE_NAME, null);

                break;

            case Calendar.THURSDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Thursday.TABLE_NAME + " order by rowid", null);

                break;

            case Calendar.FRIDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Friday.TABLE_NAME + " order by rowid", null);

                break;

            case Calendar.SATURDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Saturday.TABLE_NAME + " order by rowid", null);

                break;

            default:

                cursor = null;

                break;

        }

        return cursor;

    }

    public boolean insertHomework(Homework homework) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        long result;

        contentValues.put(DatabaseContract.Homework.COLUMN_NAME, homework.getName());
        contentValues.put(DatabaseContract.Homework.COLUMN_CLASS, homework.getClassName());
        contentValues.put(DatabaseContract.Homework.COLUMN_DATE_TIME, homework.getDateTime().toString());
        contentValues.put(DatabaseContract.Homework.COLUMN_ATTACH, homework.isAttach() ? 1 : 0);

        result = sqLiteDatabase.insert(DatabaseContract.Homework.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();

        return result != -1;

    }

    public Cursor getHomework() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Homework.TABLE_NAME + " order by rowid", null);

    }

    public Cursor getClasses() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.ClassInfo.TABLE_NAME + " order by rowid", null);

    }

}