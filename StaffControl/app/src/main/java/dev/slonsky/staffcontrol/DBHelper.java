package dev.slonsky.staffcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Slon on 11.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_LOG = "DB-LOGS";
    private String DB_CREATE = "CREATE TABLE "+ DBContract.Employee.TABLE_NAME +" (\n" +
            DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            DBContract.Employee.COLUMN_NAME_NAME        +" TEXT    NOT NULL,\n" +
            DBContract.Employee.COLUMN_NAME_POSITION    +" TEXT    NOT NULL,\n" +
            DBContract.Employee.COLUMN_NAME_PASSWORD    +" TEXT    NOT NULL\n" +
            ");\n" +

            "CREATE TABLE "+ DBContract.Shift.TABLE_NAME +" (\n" +
            DBContract.Shift.COLUMN_NAME_SHIFT_ID        + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID     + " INTEGER NOT NULL,\n" +
            DBContract.Shift.COLUMN_NAME_DATE            + " DATE    NOT NUll,\n" +
            DBContract.Shift.COLUMN_NAME_START_TIME      + " TIME    NOT NULL,\n" +
            DBContract.Shift.COLUMN_NAME_START_PHOTO_URI + " TEXT NOT NULL, \n" +
            DBContract.Shift.COLUMN_NAME_END_TIME        + " TIME,\n" +
            DBContract.Shift.COLUMN_NAME_END_PHOTO_URI   + " TEXT NOT NULL, \n" +
            DBContract.Shift.COLUMN_NAME_DURATION        + " TIME\n" +
            ");\n" +

            "CREATE TABLE "+ DBContract.Rest.TABLE_NAME +" (\n" +
            DBContract.Rest.COLUMN_NAME_REST_ID    + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            DBContract.Rest.COLUMN_NAME_SHIFT_ID   + " INTEGER NOT NULL,\n" +
            DBContract.Rest.COLUMN_NAME_START_TIME + " TIME    NOT NULL,\n" +
            DBContract.Rest.COLUMN_NAME_END_TIME   + " TIME,\n" +
            DBContract.Rest.COLUMN_NAME_DURATION   + " TIME\n" +
            ");";


    public DBHelper(Context context) {
        super(context, "StaffControlDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DB_LOG, "Create new DB");
//        db.execSQL(DB_CREATE);
        db.execSQL("CREATE TABLE "+ DBContract.Employee.TABLE_NAME +" (\n" +
                DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                DBContract.Employee.COLUMN_NAME_NAME        +" TEXT    NOT NULL,\n" +
                DBContract.Employee.COLUMN_NAME_POSITION    +" TEXT    NOT NULL,\n" +
                DBContract.Employee.COLUMN_NAME_PASSWORD    +" TEXT    NOT NULL\n" +
                ");\n");
        db.execSQL("CREATE TABLE "+ DBContract.Shift.TABLE_NAME +" (\n" +
                DBContract.Shift.COLUMN_NAME_SHIFT_ID        + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID     + " INTEGER NOT NULL,\n" +
                DBContract.Shift.COLUMN_NAME_DATE            + " DATE    NOT NUll,\n" +
                DBContract.Shift.COLUMN_NAME_START_TIME      + " TIME    NOT NULL,\n" +
                DBContract.Shift.COLUMN_NAME_START_PHOTO_URI + " TEXT    NOT NULL, \n" +
                DBContract.Shift.COLUMN_NAME_END_TIME        + " TIME,\n" +
                DBContract.Shift.COLUMN_NAME_END_PHOTO_URI   + " TEXT,\n" +
                DBContract.Shift.COLUMN_NAME_DURATION        + " LONG\n" +
                ");\n");
        db.execSQL("CREATE TABLE "+ DBContract.Rest.TABLE_NAME +" (\n" +
                DBContract.Rest.COLUMN_NAME_REST_ID    + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                DBContract.Rest.COLUMN_NAME_SHIFT_ID   + " INTEGER NOT NULL,\n" +
                DBContract.Rest.COLUMN_NAME_START_TIME + " TIME    NOT NULL,\n" +
                DBContract.Rest.COLUMN_NAME_END_TIME   + " TIME,\n" +
                DBContract.Rest.COLUMN_NAME_DURATION   + " LONG\n" +
                ");");

        Log.d(DB_LOG, "DB created");

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(c.moveToFirst()) {
            do {
                Log.d(DB_LOG, "Table: " + c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        }
        c.close();

        String[] names = {"Андреев Александр Александрович", "Валинуров Денис Юрьевич", "Гаранян Ованес Суренович",
        "Ермохин Макар Андреевич", "Капитонов Александр Евгеньевич", "Коробчинский Георгий Александрович",
        "Шарабин Михаил Романович", "Муравьев Артем Константинович", "Шинкарева Людмила Дмитриевна",
        "Баринов Алексей Игоревич", "Нурматов Асхат Анварбекович"};

        String[] positions = { "технолог", "бухгалтер", "конструктор", "Директор", "механик",
                "инженер","технолог", "бухгалтер", "конструктор", "Директор", "механик", "инженер"};
        ContentValues cv = new ContentValues();
        for(int i = 0 ; i < 10; i++) {
            cv.put(DBContract.Employee.COLUMN_NAME_NAME, names[i]);
            cv.put(DBContract.Employee.COLUMN_NAME_POSITION, positions[i]);
            cv.put(DBContract.Employee.COLUMN_NAME_PASSWORD, i+""+i+""+i+""+i);
            db.insert(DBContract.Employee.TABLE_NAME, null, cv);
        }
Random r = new Random();
        cv = new ContentValues();
        for(int j = 1; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                cv.put(DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID, i + 1);
                cv.put(DBContract.Shift.COLUMN_NAME_DATE, "0"+j+"-08-17");

                String start = "08"+":"+ r.nextInt(5) + "" + r.nextInt(9) +":" + r.nextInt(5) + "" + r.nextInt(9);
                String end = (16+j%3)+":"+ r.nextInt(5) + "" + r.nextInt(9) +":" + r.nextInt(5) + "" + r.nextInt(9);

                cv.put(DBContract.Shift.COLUMN_NAME_START_TIME, start);
                cv.put(DBContract.Shift.COLUMN_NAME_START_PHOTO_URI, "photo " + i +""+j);
                cv.put(DBContract.Shift.COLUMN_NAME_END_TIME, end);
                cv.put(DBContract.Shift.COLUMN_NAME_END_PHOTO_URI, "photo " + j +""+i);
                cv.put(DBContract.Shift.COLUMN_NAME_DURATION, getTimeDifference(start, end));
                db.insert(DBContract.Shift.TABLE_NAME, null, cv);
            }
        }

        cv = new ContentValues();
            for (int i = 0; i < 10; i++) {
                cv.put(DBContract.Rest.COLUMN_NAME_SHIFT_ID, i + 1);

                String start = "12"+":"+ r.nextInt(5) + "" + r.nextInt(9) +":" + r.nextInt(5) + "" + r.nextInt(9);
                String end = (12+i%2)+":"+ r.nextInt(5) + "" + r.nextInt(9) +":" + r.nextInt(5) + "" + r.nextInt(9);

                cv.put(DBContract.Rest.COLUMN_NAME_START_TIME, start);
                cv.put(DBContract.Rest.COLUMN_NAME_END_TIME, end);
                cv.put(DBContract.Shift.COLUMN_NAME_DURATION, getTimeDifference(start, end));
                db.insert(DBContract.Rest.TABLE_NAME, null, cv);
            }

    }

    private long getTimeDifference(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date fTime1 = format.parse(start);
            Date fTime2 = format.parse(end);
            long restMillis = fTime2.getTime() - fTime1.getTime();
            return restMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String makeTimeFromMillis (long millis) {
        // 256 7 847
        //
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}
