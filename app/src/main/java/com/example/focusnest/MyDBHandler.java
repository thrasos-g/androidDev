package com.example.focusnest;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class MyDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id", NAME = "name", STUDYTIME = "studyTime", BREAKSMALL = "breakSmall", BREAKLARGE = "breakLarge", ALARM = "alarm", DONOTDISTURB = "doNotDisturb";
    public static final String STREAK = "steak", POMODOROSCOMPLETED = "pomodorosCompleted", POMODOROCYCLESCOMPLETED = "pomodoroCyclesCompleted", TOTALSTUDYSECONDS = "totalStudySeconds", TOTALBREAKSECONDS = "totalBreakSeconds";

    //Constructor
    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Δημιουργία του σχήματος της ΒΔ
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +

                NAME + " TEXT," +
                STUDYTIME + " INTEGER," +
                BREAKSMALL + " INTEGER," +
                BREAKLARGE + " INTEGER," +
                ALARM + " INTEGER," +
                DONOTDISTURB + " INTEGER," +

                STREAK + " INTEGER," +
                POMODOROSCOMPLETED + " INTEGER," +
                POMODOROCYCLESCOMPLETED + " INTEGER," +
                TOTALSTUDYSECONDS + " INTEGER," +
                TOTALBREAKSECONDS + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert default users only once, on DB creation
        ContentValues casual = new ContentValues();
        casual.put(NAME, "Casual");
        casual.put(STUDYTIME, 25);
        casual.put(BREAKSMALL, 5);
        casual.put(BREAKLARGE, 15);
        casual.put(ALARM, 0);
        casual.put(DONOTDISTURB, 0);
        casual.put(STREAK, 0);
        casual.put(POMODOROSCOMPLETED, 0);
        casual.put(POMODOROCYCLESCOMPLETED, 0);
        casual.put(TOTALSTUDYSECONDS, 0);
        casual.put(TOTALBREAKSECONDS, 0);
        db.insert(TABLE_USERS, null, casual);

        ContentValues tryhard = new ContentValues();
        tryhard.put(NAME, "Tryhard");
        tryhard.put(STUDYTIME, 45);
        tryhard.put(BREAKSMALL, 3);
        tryhard.put(BREAKLARGE, 10);
        tryhard.put(ALARM, 1);
        tryhard.put(DONOTDISTURB, 1);
        tryhard.put(STREAK, 0);
        tryhard.put(POMODOROSCOMPLETED, 0);
        tryhard.put(POMODOROCYCLESCOMPLETED, 0);
        tryhard.put(TOTALSTUDYSECONDS, 0);
        tryhard.put(TOTALBREAKSECONDS, 0);
        db.insert(TABLE_USERS, null, tryhard);
    }

    //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    //Μέθοδος για προσθήκη ενός user στη ΒΔ
    public void addProduct(User user) {
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(STUDYTIME, user.getStudyTime());
        values.put(BREAKSMALL, user.getBreakSmall());
        values.put(BREAKLARGE, user.getBreakLarge());
        values.put(ALARM, user.getAlarm());
        values.put(DONOTDISTURB, user.getDoNotDisturb());
        values.put(STREAK, user.getStreak());
        values.put(POMODOROSCOMPLETED, user.getPomodorosCompleted());
        values.put(POMODOROCYCLESCOMPLETED, user.getPomodoroCyclesCompleted());
        values.put(TOTALSTUDYSECONDS, user.getTotalStudySeconds());
        values.put(TOTALBREAKSECONDS, user.getTotalBreakSeconds());


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    //Μέθοδος για user προϊόντος βάσει ονομασίας του
    public User findUser(String name) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.set_id(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));

            user.setStudyTime(Integer.parseInt(cursor.getString(2)));
            user.setBreakSmall(Integer.parseInt(cursor.getString(3)));
            user.setBreakLarge(Integer.parseInt(cursor.getString(4)));
            user.setAlarm(Integer.parseInt(cursor.getString(5)));
            user.setDoNotDisturb(Integer.parseInt(cursor.getString(6)));

            user.setStreak(Integer.parseInt(cursor.getString(7)));
            user.setPomodorosCompleted(Integer.parseInt(cursor.getString(8)));
            user.setPomodoroCyclesCompleted(Integer.parseInt(cursor.getString(9)));
            user.setTotalStudySeconds(Integer.parseInt(cursor.getString(10)));
            user.setTotalBreakSeconds(Integer.parseInt(cursor.getString(11)));

            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    //Μέθοδος για διαγραφή user βάσει ονομασίας του
    public boolean deleteProduct(String name) {
        boolean result = false;
        User user = findUser(name);
        if (user != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USERS, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.get_id())});
            result = true;
            db.close();
        }
        return result;
    }

    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.set_id(cursor.getInt(0));
                user.setName(cursor.getString(1));

                user.setStudyTime(cursor.getInt(2));
                user.setBreakSmall(cursor.getInt(3));
                user.setBreakLarge(cursor.getInt(4));
                user.setAlarm(cursor.getInt(5));
                user.setDoNotDisturb(cursor.getInt(6));

                user.setStreak(cursor.getInt(7));
                user.setPomodorosCompleted(cursor.getInt(8));
                user.setPomodoroCyclesCompleted(cursor.getInt(9));
                user.setTotalStudySeconds(cursor.getInt(10));
                user.setTotalBreakSeconds(cursor.getInt(11));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;

    }
}