package com.ghofrani.classapp.modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghofrani.classapp.model.DatabaseContract;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import org.joda.time.LocalTime;

import java.util.ArrayList;
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
            + DatabaseContract.ClassInfo.COLUMN_LOCATION + " TEXT,"
            + DatabaseContract.ClassInfo.COLUMN_TEACHER + " TEXT,"
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
    }

    public void insertClassesIntoDayStandard(ArrayList<StandardClass> classesToInsert, int day) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch (day) {

            case Calendar.SUNDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Sunday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Sunday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Sunday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Sunday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.MONDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Monday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Monday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Monday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Monday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.TUESDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Tuesday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Tuesday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Tuesday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Tuesday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.WEDNESDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Wednesday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Wednesday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Wednesday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Wednesday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.THURSDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Thursday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Thursday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Thursday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Thursday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.FRIDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Friday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Friday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Friday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Friday.TABLE_NAME, null, contentValues);

                }

                break;

            case Calendar.SATURDAY:

                for (int i = 0; i < classesToInsert.size(); i++) {

                    contentValues.clear();

                    contentValues.put(DatabaseContract.Saturday.COLUMN_CLASS, classesToInsert.get(i).getName());
                    contentValues.put(DatabaseContract.Saturday.COLUMN_START_TIME, classesToInsert.get(i).getStartTime().toString());
                    contentValues.put(DatabaseContract.Saturday.COLUMN_END_TIME, classesToInsert.get(i).getEndTime().toString());

                    sqLiteDatabase.insert(DatabaseContract.Saturday.TABLE_NAME, null, contentValues);

                }

                break;

        }

        sqLiteDatabase.close();

    }

    public void removeClassOutOfDay(int day, String classToRemove, LocalTime startTime) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        switch (day) {

            case Calendar.SUNDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Sunday.TABLE_NAME + " where "
                        + DatabaseContract.Sunday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Sunday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.MONDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Monday.TABLE_NAME + " where "
                        + DatabaseContract.Monday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Monday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.TUESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Tuesday.TABLE_NAME + " where "
                        + DatabaseContract.Tuesday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Tuesday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.WEDNESDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Wednesday.TABLE_NAME + " where "
                        + DatabaseContract.Wednesday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Wednesday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.THURSDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Thursday.TABLE_NAME + " where "
                        + DatabaseContract.Thursday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Thursday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.FRIDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Friday.TABLE_NAME + " where "
                        + DatabaseContract.Friday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Friday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

                break;

            case Calendar.SATURDAY:

                sqLiteDatabase.execSQL("delete from "
                        + DatabaseContract.Saturday.TABLE_NAME + " where "
                        + DatabaseContract.Saturday.COLUMN_CLASS + "='"
                        + classToRemove + "' and "
                        + DatabaseContract.Saturday.COLUMN_START_TIME + "='"
                        + startTime.toString() + "'");

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

    public void deleteAllHomework() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("delete from " + DatabaseContract.Homework.TABLE_NAME);

        sqLiteDatabase.close();

    }

    public void addClass(SlimClass classToAdd) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.ClassInfo.COLUMN_NAME, classToAdd.getName());
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_LOCATION, classToAdd.getLocation());
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_TEACHER, classToAdd.getTeacher());
        contentValues.put(DatabaseContract.ClassInfo.COLUMN_COLOR, classToAdd.getColor());

        sqLiteDatabase.insert(DatabaseContract.ClassInfo.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();

    }

    public String getClassLocation(String className) {

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

    public String getClassTeacher(String className) {

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

    public Cursor getClassesCursor(int day) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        switch (day) {

            case Calendar.SUNDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Sunday.TABLE_NAME + " order by rowid", null);

                break;

            case Calendar.MONDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Monday.TABLE_NAME + " order by rowid", null);

                break;

            case Calendar.TUESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Tuesday.TABLE_NAME + " order by rowid", null);

                break;

            case Calendar.WEDNESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Wednesday.TABLE_NAME + " order by rowid", null);

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

    public void addHomework(ArrayList<Homework> homeworkToAdd) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < homeworkToAdd.size(); i++) {

            contentValues.clear();

            contentValues.put(DatabaseContract.Homework.COLUMN_NAME, homeworkToAdd.get(i).getName());
            contentValues.put(DatabaseContract.Homework.COLUMN_CLASS, homeworkToAdd.get(i).getClassName());
            contentValues.put(DatabaseContract.Homework.COLUMN_DATE_TIME, homeworkToAdd.get(i).getDateTime().toString());
            contentValues.put(DatabaseContract.Homework.COLUMN_ATTACH, homeworkToAdd.get(i).isAttach() ? 1 : 0);

            sqLiteDatabase.insert(DatabaseContract.Homework.TABLE_NAME, null, contentValues);

        }

        sqLiteDatabase.close();

    }

    public Cursor getHomeworkCursor() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Homework.TABLE_NAME + " order by rowid", null);

    }

    public Cursor getClassesCursor() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.ClassInfo.TABLE_NAME + " order by rowid", null);

    }

}