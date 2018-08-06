package dev.slonsky.staffcontrol;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.TimeZoneFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ShiftControlActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int DATES_LOADER = 1;
    public static final int EMPLOYEES_LOADER = 2;
    public static final int SHIFTS_LOADER = 3;

    private SQLiteDatabase db;

    private TableRow tableHeader;
    private TableLayout firstColumn;
    private TableLayout tableBody;

    private ScrollView firstColumnScroll;
    private ScrollView tableScroll;

    private HorizontalScrollView headerHorScroll;
    private HorizontalScrollView tableHorScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_control_shit);

        // 1. init table header with unique dates
        // 2. go through employees and init
        //      first column with name
        //      table body with shifts
        //      for each cell set tag with id and listener for dialog (details)
        // 3. set scrolling

        tableHeader = (TableRow) findViewById(R.id.table_header_row);
        firstColumn = (TableLayout) findViewById(R.id.first_column_table);
        tableBody = (TableLayout) findViewById(R.id.body_table);

        firstColumnScroll = (ScrollView) findViewById(R.id.table_first_row_scroll);
        tableScroll = (ScrollView) findViewById(R.id.table_body_vertical_scroll);
        headerHorScroll = (HorizontalScrollView) findViewById(R.id.table_header_scroll);
        tableHorScroll = (HorizontalScrollView) findViewById(R.id.table_body_horizontal_scroll);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tableScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    firstColumnScroll.scrollTo(scrollX, scrollY);
                }
            });
            tableHorScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    headerHorScroll.scrollTo(scrollX, scrollY);
                }
            });
            firstColumnScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    tableScroll.scrollTo(scrollX, scrollY);
                }
            });
            headerHorScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    tableHorScroll.scrollTo(scrollX, scrollY);
                }
            });
        }

        DBManager.initializeInstance(new DBHelper(this));
        db = DBManager.getInstance().openDB();

        getSupportLoaderManager().initLoader(DATES_LOADER, null, this);
        getSupportLoaderManager().initLoader(EMPLOYEES_LOADER, null, this);
        getSupportLoaderManager().initLoader(SHIFTS_LOADER, null, this);

        getSupportLoaderManager().getLoader(DATES_LOADER).forceLoad();
    }



    private ArrayList<String> dates = new ArrayList<>();
    private void inflateDates(Cursor data) {
        if(data == null || data.isClosed()) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        if(!data.moveToFirst() || tableHeader.getChildCount() != 0) {
            return;
        }

            do {
                Log.d("DEBUG", "date: " + data.getString(data.getColumnIndex(DBContract.Shift.COLUMN_NAME_DATE)));
                View view = inflater.inflate(R.layout.date, null);
                ((TextView)view.findViewById(R.id.date)).setText(
                        data.getString(data.getColumnIndex(DBContract.Shift.COLUMN_NAME_DATE)));
                dates.add(data.getString(data.getColumnIndex(DBContract.Shift.COLUMN_NAME_DATE)));

                ViewGroup insert = (ViewGroup) tableHeader;
                insert.addView(view);

            } while (data.moveToNext());

            View view = inflater.inflate(R.layout.date, null);
            ((TextView) view.findViewById(R.id.date)).setText("Всего");

            ViewGroup insert = (ViewGroup) tableHeader;
            insert.addView(view);

//        data.close();
        // for every date
        // inflate view
        // attach to row
    }

    private void insertEmployees(Cursor data) {
        if(data == null || data.isClosed()) {
            return;
        }
            if (data.moveToFirst()) {
                LayoutInflater inflater = getLayoutInflater();
                do {
                    Log.d("DEBUG", "employee: " + data.getString(data.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME)));

                    View view = inflater.inflate(R.layout.employee, null);


                        ((TextView) view.findViewById(R.id.employee_table_name)).setText(
                                data.getString(data.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME)));

                    ViewGroup insert = (ViewGroup) firstColumn;
                    insert.addView(view);

                    new ShiftsTask(db, data.getString(data.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME)), data.getInt(data.getColumnIndex(DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID))).execute(data.getInt(data.getColumnIndex(DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID)));
                } while (data.moveToNext());
            }
        }
//        data.close();



    private void insertShifts(Cursor shifts, String name, String work) {
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup rowGroup = (ViewGroup) inflater.inflate(R.layout.row, null);
        if(shifts == null || shifts.isClosed()) {
            return;
        }
        for(String s: dates) {
            boolean found = false;
            if (shifts.moveToFirst()) {
                do {
                    View cell = inflater.inflate(R.layout.cell, null);

                    if (s.equals(shifts.getString(shifts.getColumnIndex(DBContract.Shift.COLUMN_NAME_DATE)))) {
                        ((TextView) cell.findViewById(R.id.start_time)).setText(
                                shifts.getString(shifts.getColumnIndex(DBContract.Shift.COLUMN_NAME_START_TIME))
                        );
                        ((TextView) cell.findViewById(R.id.end_time)).setText(
                                shifts.getString(shifts.getColumnIndex(DBContract.Shift.COLUMN_NAME_END_TIME))
                        );

                        final Cursor c = shifts;
                        final String n = name;
                        final String date = c.getString(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_DATE));
                        final long duration = c.getLong(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_DURATION));
                        final String startTime = c.getString(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_START_TIME));
                        final String startPhoto = c.getString(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_START_PHOTO_URI));
                        final String endTime = c.getString(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_END_TIME));
                        final String endPhoto = c.getString(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_END_PHOTO_URI));
                        final int shiftId = c.getInt(c.getColumnIndex(DBContract.Shift.COLUMN_NAME_SHIFT_ID));
                        cell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                        ShiftInfoDialog.getInstance(n, date, duration, startTime, startPhoto, endTime, endPhoto, shiftId)
//                                .show(getSupportFragmentManager(), "dialog");
                                Intent intent = new Intent(ShiftControlActivity.this, ShiftActivity.class);
                                intent.putExtra("name", n);
                                intent.putExtra("date", date);
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("duration", duration);
                                intent.putExtra("startUri", startPhoto);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("endUri", endPhoto);
                                intent.putExtra("shiftId", shiftId);
                                startActivity(intent);

                            }
                        });
                        rowGroup.addView(cell);
                        found = true;
                        break;
                    }
                } while (shifts.moveToNext());
                if(!found) {
                    View cell = inflater.inflate(R.layout.cell, null);
                    rowGroup.addView(cell);
                }
            }
        }
        View cell = inflater.inflate(R.layout.cell, null);
        ((TextView)cell.findViewById(R.id.start_time)).setText("    ");
        ((TextView)cell.findViewById(R.id.textView9)).setText(" ");
        ((TextView)cell.findViewById(R.id.end_time)).setText(
                work + "      "
        );
        rowGroup.addView(cell);
        // add time
        tableBody.addView(rowGroup);
//        shifts.close();

        // insart into body table table row
        // insert into table row info
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DBManager.getInstance().closeDB();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DATES_LOADER:
                return new DatesLoader(this, db);
            case EMPLOYEES_LOADER:
                return new EmployeesLoader(this, db);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DATES_LOADER:
                // load dates
                inflateDates(data);
                break;
            case EMPLOYEES_LOADER:
                insertEmployees(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class DatesLoader extends CursorLoader {

        SQLiteDatabase db;

        public DatesLoader(Context context, SQLiteDatabase db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return db.query(true, DBContract.Shift.TABLE_NAME,
                    new String[] { DBContract.Shift.COLUMN_NAME_DATE },
                    null, null, null, null,
                    DBContract.Shift.COLUMN_NAME_DATE, null);
        }
    }

    static class EmployeesLoader extends CursorLoader {

        SQLiteDatabase db;

        public EmployeesLoader(Context context, SQLiteDatabase db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor c = db.query(DBContract.Employee.TABLE_NAME,
                    new String[] { DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID, DBContract.Employee.COLUMN_NAME_NAME },
                    null, null, null, null,
                    DBContract.Employee.COLUMN_NAME_NAME, null);
            return c;
        }
    }

    static class ShiftsLoader extends CursorLoader {

        SQLiteDatabase db;
        int id;
        public ShiftsLoader(Context context, SQLiteDatabase db, int id) {
            super(context);
            this.db = db;
            this.id = id;
        }

        @Override
        public Cursor loadInBackground() {
            return db.query(DBContract.Shift.TABLE_NAME,
                    new String[] { DBContract.Shift.COLUMN_NAME_DATE, DBContract.Shift.COLUMN_NAME_START_TIME, DBContract.Shift.COLUMN_NAME_END_TIME },
                    DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ?",
                    new String[] { Integer.toString(id)}, null, null,
                    DBContract.Shift.COLUMN_NAME_DATE, null);
        }
    }

    class ShiftsTask extends AsyncTask<Integer, Void, Cursor> {

        private SQLiteDatabase db;
        private String name;
        private int id;
        private String work;

        public ShiftsTask(SQLiteDatabase db, String name, int id) {
            this.db = db;
            this.name = name;
            this.id = id;
        }

        @Override
        protected Cursor doInBackground(Integer... params) {
            Cursor time = db.query(DBContract.Shift.TABLE_NAME,
                    new String[]{DBContract.Shift.COLUMN_NAME_SHIFT_ID, DBContract.Shift.COLUMN_NAME_DURATION },
                    DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ? AND " + DBContract.Shift.COLUMN_NAME_END_TIME + " IS NOT NULL", new String[] { Integer.toString(id)},
                    null, null, null);

            long worktime = 0;
            if(time.moveToFirst()) {
                do {
                        worktime += time.getLong(time.getColumnIndex(DBContract.Shift.COLUMN_NAME_DURATION));
    Log.d("DEBUG", name + " w+ " + worktime + ", work " + time.getLong(time.getColumnIndex(DBContract.Shift.COLUMN_NAME_DURATION)));

                    Cursor rest = db.query(DBContract.Rest.TABLE_NAME,
                            new String[]{DBContract.Rest.COLUMN_NAME_DURATION},
                            DBContract.Rest.COLUMN_NAME_SHIFT_ID + " = ? AND "
                                    + DBContract.Rest.COLUMN_NAME_END_TIME + " IS NOT NULL",
                            new String[] { Integer.toString(time.getInt(time.getColumnIndex(DBContract.Shift.COLUMN_NAME_SHIFT_ID)))},
                            null, null, null);
                    if(rest.moveToFirst()) {
                        do {
                            worktime -= rest.getLong(rest.getColumnIndex(DBContract.Rest.COLUMN_NAME_DURATION));
                            Log.d("DEBUG", name + " w- " + worktime + ", rest " + rest.getLong(rest.getColumnIndex(DBContract.Rest.COLUMN_NAME_DURATION)));
                        } while (rest.moveToNext());
                    }
                } while(time.moveToNext());
            }
            work = DBHelper.makeTimeFromMillis(worktime); //new SimpleDateFormat("dd-HH:mm:ss").format(new Date(worktime));

            return db.query(DBContract.Shift.TABLE_NAME, null,
                    DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID + " = ?",
                    new String[]{Integer.toString(params[0])}, null, null, DBContract.Shift.COLUMN_NAME_DATE);
        }


        private String getTimeDifference(String start, String end) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            try {
                Date fTime1 = format.parse(start);
                Date fTime2 = format.parse(end);
                long restMillis = fTime2.getTime() - fTime1.getTime();
                return format.format(new Date(restMillis));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            insertShifts(cursor, name, work);
        }
    }


}
