package com.slon.lesson34;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etName, etEmail;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (view.getId()){
            case R.id.btnAdd:
                Log.d(LOG_TAG, "insert");
                cv.put("name", name);
                cv.put("email", email);

                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row insered: " + rowID);
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "rows");
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                if(c.moveToFirst()){
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", email = " + c.getString(emailColIndex));
                    } while(c.moveToNext());
                } else {
                    Log.d(LOG_TAG, "0 rows");
                }
                c.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "clear");
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context){
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(LOG_TAG, "oncreate");
            sqLiteDatabase.execSQL("create table mytable (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "email text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
