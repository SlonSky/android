package dev.slonsky.staffcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EmployeeActivity extends AppCompatActivity implements Camera.PictureCallback {

    private TextView mEmployeeName;
    private TextView mEmployeePosition;
    private Button   mOpenShift;
    private Button   mCloseShift;
    private Button   mOpenRest;
    private Button   mCloseRest;
    private Button   mLogOut;

    private ProgressBar progressBar;

    // TODO: get it from bundle
    private int    employee_id;
    private String employee_name;
    private String employee_position;
    private String employee_password;

    private PhotoCapture photoCapture;
    private File photoFile;

    private static final String ACTION_OPENED_SHIFT = "открыл смену";
    private static final String ACTION_CLOSED_SHIFT = "закрыл смену";
    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        mEmployeeName = (TextView) findViewById(R.id.employee_name);
        mEmployeePosition = (TextView) findViewById(R.id.employee_position);

        mOpenShift    = (Button) findViewById(R.id.open_shift);
        mCloseShift   = (Button) findViewById(R.id.close_shift);
        mOpenRest     = (Button) findViewById(R.id.open_rest);
        mCloseRest    = (Button) findViewById(R.id.close_rest);
        mLogOut       = (Button) findViewById(R.id.btn_log_out);

        progressBar = (ProgressBar) findViewById(R.id.progressBarEmployee);

        employee_id       = getIntent().getIntExtra("id", -1);
        employee_name     = getIntent().getStringExtra("name");
        employee_position = getIntent().getStringExtra("position");
        employee_password = getIntent().getStringExtra("password");

        mEmployeeName.setText(employee_name);
        mEmployeePosition.setText(employee_position);


        View.OnClickListener logoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeActivity.this, AuthActivity.class);
                EmployeeActivity.this.startActivity(intent);
                finish();
            }
        };

        mLogOut.setOnClickListener(logoutListener);

        mOpenShift.setOnClickListener(new ButtonListener(this));
        mCloseShift.setOnClickListener(new ButtonListener(this));
        mOpenRest.setOnClickListener(new ButtonListener(this));
        mCloseRest.setOnClickListener(new ButtonListener(this));

        mOpenShift.setEnabled(false);
        mCloseShift.setEnabled(false);
        mOpenRest.setEnabled(false);
        mCloseRest.setEnabled(false);


        new ButtonSetterTask().execute();

        DBManager.initializeInstance(new DBHelper(this));
        makePhoto("TEST");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                Intent intent = new Intent(EmployeeActivity.this, AuthActivity.class);
                EmployeeActivity.this.startActivity(intent);
                finish();
                break;
            case R.id.settings:


                ChangePasswordDialog dialog = ChangePasswordDialog.newInstance(employee_password, employee_id, false);

                dialog.show(getSupportFragmentManager(), "dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {

            FileOutputStream fos = new FileOutputStream(photoFile);
            fos.write(data);
            fos.close();

//            Picasso.with(this).invalidate(photoFile);
//            Picasso.with(this).load(photoFile).into(image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(action.equals(ACTION_OPENED_SHIFT)) {
            // open acync
            new EmployeeActionTask(EmployeeActionTask.OPEN_SHIFT).execute();
        } else if (action.equals(ACTION_CLOSED_SHIFT)) {
            // open acync
            new EmployeeActionTask(EmployeeActionTask.CLOSE_SHIFT).execute();
        } else {
            Log.d("DEBUG", "Something wrong with picture callback!");
        }
        action = "";
        photoCapture.releaseCam();
    }

    private class ButtonListener implements View.OnClickListener {

        private Context context;

        public ButtonListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.open_shift:

                    // TODO
                    // make photo
                    // save photo, send next uri
                    // make record in Shifts table
                    // with no end_time
                    // currents employee and date
                    // current time
                    //
                    mOpenShift.setEnabled(false);
                    mCloseShift.setEnabled(true);
                    mOpenRest.setEnabled(true);
                    mCloseRest.setEnabled(false);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    action = ACTION_OPENED_SHIFT;
                    makePhoto(ACTION_OPENED_SHIFT);
                    Toast.makeText(EmployeeActivity.this, "Смена открыта", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.close_shift:
                    // TODO
                    // update record in SHifts
                    // for current employee
                    // where is no end time
                    // asign current time to end time


                    mOpenShift.setEnabled(true);
                    mCloseShift.setEnabled(false);
                    mOpenRest.setEnabled(false);
                    mCloseRest.setEnabled(false);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    action = ACTION_CLOSED_SHIFT;
                    makePhoto(ACTION_CLOSED_SHIFT);
                    Toast.makeText(EmployeeActivity.this, "Смена закрыта", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.open_rest:
                    // TODO
                    // similar to shifts, in Rest table
                    // find current unclosed shift, get its id
                    // make rest for this id with no end_time

                    mOpenRest.setEnabled(false);
                    mCloseRest.setEnabled(true);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    new EmployeeActionTask(EmployeeActionTask.OPEN_REST).execute();
                    break;
                case R.id.close_rest:
                    // TODO
                    // find shift with no end_time for current employee
                    // find rest with no end_time for current shift
                    // update end_time

                    mOpenRest.setEnabled(true);
                    mCloseRest.setEnabled(false);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    new EmployeeActionTask(EmployeeActionTask.CLOSE_REST).execute();
                    break;
            }
        }
    }

    private void makePhoto(String action) {
        try {
            photoCapture = new PhotoCapture(this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DEBUG", "SOmething wring with camera in makePhoto");
        }
        if(photoCapture != null) {
            photoFile = photoCapture.capturePhoto(employee_name, action);
            return;
        }
        Log.d("DEBUG", "PhotoCapture is null!");

    }


    private class EmployeeActionTask extends AsyncTask<Void, Void, Void> {
        public static final int OPEN_SHIFT = 1;
        public static final int CLOSE_SHIFT = 2;
        public static final int OPEN_REST = 3;
        public static final int CLOSE_REST = 4;
        private int action;

        SQLiteDatabase db;

        public EmployeeActionTask(int action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            db = DBManager.getInstance().openDB();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            DBManager.getInstance().closeDB();
        }

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();

            ContentValues cv;
            Cursor cursor;

            switch (action) {
                case OPEN_SHIFT:
                    cv = new ContentValues();
                    cv.put(DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID, employee_id);
                    cv.put(DBContract.Shift.COLUMN_NAME_DATE, getCurrentDate());
                    cv.put(DBContract.Shift.COLUMN_NAME_START_TIME, getCurrentTime());
                    cv.put(DBContract.Shift.COLUMN_NAME_START_PHOTO_URI, photoFile.toURI().toString());
                    long id = db.insert(DBContract.Shift.TABLE_NAME, null, cv);
                    Log.d("DEBUG", "Inserted, id:"+id);
                    break;

                case CLOSE_SHIFT:
                    Log.d("DEBUG", "closing shift");
                    cursor = getCurrentShift(db);

                    if(!cursor.moveToFirst()) {
                        Log.d("DEBUG", "Close shift error! shift Row not found");
                        break;
                    }

                    String startTime = cursor.getString(cursor.getColumnIndex(DBContract.Shift.COLUMN_NAME_START_TIME));
                    long timeDif = getTimeDifference(startTime, getCurrentTime());
                    if(timeDif == 0) {
                        Log.d("DEBUG", "Time error while closing shift");
                        break;
                    }

                    cursor.close();

                    cv = new ContentValues();
                    cv.put(DBContract.Shift.COLUMN_NAME_DURATION, timeDif);
                    cv.put(DBContract.Shift.COLUMN_NAME_END_TIME, getCurrentTime());
                    cv.put(DBContract.Shift.COLUMN_NAME_END_PHOTO_URI, photoFile.toURI().toString());

                    int res = db.update(DBContract.Shift.TABLE_NAME, cv,
                            DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ? AND " + DBContract.Shift.COLUMN_NAME_DATE + " = ?",
                            new String[]{ Integer.toString(employee_id), getCurrentDate() });

                    Log.d("DEBUG", "Updated, affected:"+res);
                    break;

                case OPEN_REST:

                    cursor = getCurrentShift(db);

                    if(!cursor.moveToFirst()) {
                        Log.d("DEBUG", "Open rest error! shift Row not found");
                        break;
                    }

                    int shiftId = cursor.getInt(cursor.getColumnIndex(DBContract.Shift.COLUMN_NAME_SHIFT_ID));

                    cursor.close();

                    cv = new ContentValues();
                    cv.put(DBContract.Rest.COLUMN_NAME_SHIFT_ID, shiftId);
                    cv.put(DBContract.Rest.COLUMN_NAME_START_TIME, getCurrentTime());

                    long restId = db.insert(DBContract.Rest.TABLE_NAME, null, cv);

                    Log.d("DEBUG", "Inserted rest, id:"+restId);
                    break;

                case CLOSE_REST:
                    cursor = getCurrentRest(db);
                    if(cursor == null || !cursor.moveToFirst()) {
                        Log.d("DEBUG", "Close rest error! Rest Row not found");
                        break;
                    }

                    String startRestTime = cursor.getString(cursor.getColumnIndex(DBContract.Rest.COLUMN_NAME_START_TIME));
                    long restTimeDif = getTimeDifference(startRestTime, getCurrentTime());
                    if(restTimeDif == 0) {
                        Log.d("DEBUG", "Time error while closing rest");
                        break;
                    }

                    int restID = cursor.getInt(cursor.getColumnIndex(DBContract.Rest.COLUMN_NAME_REST_ID));

                    cursor.close();

                    cv = new ContentValues();
                    cv.put(DBContract.Rest.COLUMN_NAME_DURATION, restTimeDif);
                    cv.put(DBContract.Rest.COLUMN_NAME_END_TIME, getCurrentTime());

                    int result = db.update(DBContract.Rest.TABLE_NAME, cv,
                            DBContract.Rest.COLUMN_NAME_REST_ID + " = ?",
                            new String[]{ Integer.toString(restID)});

                    Log.d("DEBUG", "Updated, affected:" + result);
                    break;

            }
            return null;
        }

        private Cursor getCurrentShift(SQLiteDatabase db) {
            return db.query(DBContract.Shift.TABLE_NAME,
                    null,
                    DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ? AND " + DBContract.Shift.COLUMN_NAME_DATE + " = ? "
                    + " AND " + DBContract.Shift.COLUMN_NAME_END_TIME + " IS NULL",
                    new String[]{ Integer.toString(employee_id),  getCurrentDate()},
                    null, null, null);
        }

        private Cursor getCurrentRest(SQLiteDatabase db) {
            Cursor c = getCurrentShift(db);
            if(!c.moveToFirst()) {
                return null;
            }

            int shiftId = c.getInt(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_SHIFT_ID));
            c.close();

            return db.query(DBContract.Rest.TABLE_NAME,
                    null,
                    DBContract.Rest.COLUMN_NAME_SHIFT_ID + " = ? AND " + DBContract.Rest.COLUMN_NAME_END_TIME + " IS NULL",
                    new String[]{ Integer.toString(shiftId) },
                    null, null, null);
        }

        private String getCurrentDate() {
            return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
        }

        private String getCurrentTime() {
            return new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
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


    }

    private class ButtonSetterTask extends AsyncTask<Void, Void, Void> {

        private boolean shiftOpened;
        private boolean restOpened;
        private SQLiteDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = DBManager.getInstance().openDB();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DBManager.getInstance().closeDB();

            mOpenShift.setEnabled(!shiftOpened);
            mCloseShift.setEnabled(shiftOpened);
            mOpenRest.setEnabled(!restOpened && shiftOpened);
            mCloseRest.setEnabled(restOpened && shiftOpened);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor c = getCurrentShift(db);
            shiftOpened = c.moveToFirst();
            c.close();

            c = getCurrentRest(db);
            restOpened = (c == null || c.moveToFirst());
            if (c != null) {
                c.close();
            }
            return null;
        }

        private Cursor getCurrentShift(SQLiteDatabase db) {
            return db.query(DBContract.Shift.TABLE_NAME,
                    null,
                    DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ? AND " + DBContract.Shift.COLUMN_NAME_DATE + " = ? "
                            + " AND " + DBContract.Shift.COLUMN_NAME_END_TIME + " IS NULL",
                    new String[]{ Integer.toString(employee_id),  getCurrentDate()},
                    null, null, null);
        }

        private String getCurrentDate() {
            return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
        }

        private Cursor getCurrentRest(SQLiteDatabase db) {
            Cursor c = getCurrentShift(db);
            if(!c.moveToFirst()) {
                return null;
            }

            int shiftId = c.getInt(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_SHIFT_ID));
            c.close();

            return db.query(DBContract.Rest.TABLE_NAME,
                    null,
                    DBContract.Rest.COLUMN_NAME_SHIFT_ID + " = ? AND " + DBContract.Rest.COLUMN_NAME_END_TIME + " IS NULL",
                    new String[]{ Integer.toString(shiftId) },
                    null, null, null);
        }
    }
}
