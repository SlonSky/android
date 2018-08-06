package dev.slonsky.staffcontrol;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Slon on 12.08.2017.
 */

public class DBManager {
    private static DBManager instance;

    private static DBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private int mOpenCounter=0;

    public static synchronized void initializeInstance(DBHelper helper) {
        if (instance == null) {
            instance = new DBManager();
            mHelper = helper;
        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DBManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDB() {
        mOpenCounter++;
        if (mOpenCounter == 1) {
            mDatabase = mHelper.getWritableDatabase();

            Log.d("DB-LOG", "Open connection");
        }
        Log.d("DB-LOG", "Conenctions: " + mOpenCounter);
        return mDatabase;
    }

    public synchronized void closeDB() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            mHelper.close();
            Log.d("DB-LOG", "Close");
        }
        if(mOpenCounter < 0) {
            mOpenCounter = 0;
        }
        Log.d("DB-LOG", "Conenctions: " + mOpenCounter);
    }

}
