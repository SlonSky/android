package dev.slonsky.staffcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private AutoCompleteTextView name;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        name = (AutoCompleteTextView) findViewById(R.id.employee_login);
        password = (EditText) findViewById(R.id.password);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("StaffControl", MODE_PRIVATE);
        String adminPassword = preferences.getString("admin_password", "");
        if(adminPassword.equals("")) {
            preferences.edit().putString("admin_password", "admin").apply();
        }

        Log.d("DEBUG", "!!!!!! " + DBHelper.makeTimeFromMillis(1320000));


/**
 * DB
 */
//        DBHelper dbHelper = new DBHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
////
//        ContentValues cv = new ContentValues();
//        cv.put(DBContract.Shift.COLUMN_NAME_EMPLOYEE_ID, 1);
//        cv.put(DBContract.Shift.COLUMN_NAME_DATE, "date('1997-12-10')");
//        cv.put(DBContract.Shift.COLUMN_NAME_START_TIME, "time('13-03-35')");
//
//
//
//        long rowID = db.insert(DBContract.Shift.TABLE_NAME, null, cv);
//        Toast.makeText(this, "Put, ID: " + rowID, Toast.LENGTH_SHORT).show()

        DBManager.initializeInstance(new DBHelper(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().length() == 0) {
                    Toast.makeText(AuthActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();

                } else if(password.getText().length() == 0) {
                    Toast.makeText(AuthActivity.this, "Введите пароль", Toast.LENGTH_SHORT).show();

                } else if(name.getText().toString().equals("admin")) {
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("StaffControl", MODE_PRIVATE);
                    String adminPassword = preferences.getString("admin_password", "");

                    if(adminPassword.equals(password.getText().toString())) {
                        Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AuthActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AuthTask(name.getText().toString(), password.getText().toString()).execute();
                }
                // 1. check are all the field full
                //      make empty field red
                // 2. if login = admin
                //      and password = admin_password (stored in preferences?)
                //        go (Intent) to admin page
                //
                // 3. get from DB, table employees entry with entered login
                //      if no record
                //        show Toast "No such employee"
                //      else
                //        if md5(entered_password) = record_password
                //          make photo
                //          save photo
                //          go (Intent) to current employee page, send photo uri
                //        else
                //          Toast "wrong password"
                //
            }
        });

        new ListTask().execute();
    }

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == 2) {
            if( grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Не могу продолжить работу без разрешений.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private class ListTask extends AsyncTask<Void, Integer, List<EmployeeAutocompleteAdapter.Employee>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<EmployeeAutocompleteAdapter.Employee> data) {
            super.onPostExecute(data);
            progressBar.setVisibility(View.GONE);

//            ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(AuthActivity.this, android.R.layout.simple_dropdown_item_1line, data);
            EmployeeAutocompleteAdapter adapter = new EmployeeAutocompleteAdapter(AuthActivity.this, R.layout.autocomplete_dropdown, data);
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.employee_login);
            textView.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.animate();
        }

        @Override
        protected List<EmployeeAutocompleteAdapter.Employee> doInBackground(Void... params) {
            publishProgress(0);
            ArrayList<EmployeeAutocompleteAdapter.Employee> data = new ArrayList<>();

            SQLiteDatabase db = DBManager.getInstance().openDB();
            Cursor c = db.query(false,
                                     DBContract.Employee.TABLE_NAME,
                                     new String[]{
                                             DBContract.Employee.COLUMN_NAME_NAME,
                                             DBContract.Employee.COLUMN_NAME_POSITION},
                                     null, null, null, null, null, null);
            if(c.moveToFirst()) {
                do {
                    data.add(new EmployeeAutocompleteAdapter.Employee(
                            c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME)),
                            c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_POSITION))));
                } while (c.moveToNext());
            }

            c.close();
            DBManager.getInstance().closeDB();

            return data;
        }
    }

    private class AuthTask extends AsyncTask<Void, Void, Integer> {

        private static final int NO_ACCOUNT = 1;
        private static final int WRONG_PASSWORD = 2;
        private static final int LOGGED_IN = 3;

        private String login;
        private String position;
        private String password;
        private int id;

        public AuthTask(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            switch (result) {
                case NO_ACCOUNT:
                    Toast.makeText(AuthActivity.this, "Нет такого сотрудника", Toast.LENGTH_SHORT).show();
                    break;
                case WRONG_PASSWORD:
                    Toast.makeText(AuthActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    break;
                case LOGGED_IN:
                    Intent intent = new Intent(AuthActivity.this, EmployeeActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", login);
                    intent.putExtra("position", position);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.animate();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            publishProgress();

            SQLiteDatabase db = DBManager.getInstance().openDB();

            Cursor c = db.query(DBContract.Employee.TABLE_NAME,
                    null,
                    DBContract.Employee.COLUMN_NAME_NAME+" = ?",
                    new String[]{login},
                    null, null, null
            );
            
            if(c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_PASSWORD)).equals(password)) {
                        id = c.getInt(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID));
                        position = c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_POSITION));
                        closeDB(c);
                        return LOGGED_IN;
                    }
                } while (c.moveToNext());
                closeDB(c);
                return WRONG_PASSWORD;
            }
            closeDB(c);
            return NO_ACCOUNT;
        }

        private void closeDB(Cursor c) {
            c.close();
            DBManager.getInstance().closeDB();
        }

    }



//    class Task extends AsyncTask<Void , Integer, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            progressBar.setVisibility(View.GONE);
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            for(int i = 0; i < 20; i ++) {
//                try {
//                    Thread.sleep(100);
//                    publishProgress(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return null;
//        }
//    }
//
//    private void init() {
//        final String PREFS_NAME = "StaffControlPrefsFile";
//        final String PREF_VERSION_CODE_KEY = "version_code";
//        final int NOT_EXISTS = -1;
//
//        int currentVersionCode = BuildConfig.VERSION_CODE;
//
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, NOT_EXISTS);
//
//        if (currentVersionCode == savedVersionCode) {
//            // load DB
//            return;
//        } else if (savedVersionCode == NOT_EXISTS) {
//            // init DB
//            // load DB
//        } else if (currentVersionCode > savedVersionCode) {
//            // load DB
//        }
//
//        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
//    }
}
