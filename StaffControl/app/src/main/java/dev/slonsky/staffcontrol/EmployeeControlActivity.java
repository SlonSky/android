package dev.slonsky.staffcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class EmployeeControlActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, EmployeeControlDialog.OnEmployeeControlDialogListener {

    public static final int CM_DELETE_ID = 1;
    public static final int CM_EDIT_ID = 2;

    private ListView employeeList;
    SimpleCursorAdapter adapter;

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_control);

        db = DBManager.getInstance().openDB();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeControlDialog.getInstanceAdd().show(getSupportFragmentManager(), "dialog");
            }

        });

        String[] from = new String[] { DBContract.Employee.COLUMN_NAME_NAME, DBContract.Employee.COLUMN_NAME_POSITION };
        int[] to      = new int[] { R.id.employee_list_name,  R.id.employee_list_position };

        adapter = new SimpleCursorAdapter(this, R.layout.employees_list_item, null, from, to);

        employeeList = (ListView) findViewById(R.id.employeesListView);
        employeeList.setAdapter(adapter);

        registerForContextMenu(employeeList);

        getSupportLoaderManager().initLoader(0, null, this);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
        menu.add(0, CM_EDIT_ID, 0, "Редактировать");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == CM_DELETE_ID) {

            db.delete(DBContract.Employee.TABLE_NAME,
                    DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID + " = ?",
                    new String[]{Long.toString(acmi.id)});


            getSupportLoaderManager().getLoader(0).forceLoad();

            return true;
        } else if(item.getItemId() == CM_EDIT_ID) {
            // get employee with current id
            // open dialog with its data
            // get user input
            // update db
            Cursor c = db.query(DBContract.Employee.TABLE_NAME, null,
                    DBContract.Employee._ID + " = ?",
                    new String[]{Long.toString(acmi.id)},
                    null, null, null);
            if(c.moveToFirst()) {
                String name = c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME));
                String position = c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_POSITION));
                String password = c.getString(c.getColumnIndex(DBContract.Employee.COLUMN_NAME_PASSWORD));

                EmployeeControlDialog dialog = EmployeeControlDialog.getInstanceEdit((int)acmi.id, name, position, password);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
            c.close();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManager.getInstance().closeDB();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new EmployeeLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void editEmployee(int id, String name, String position, String password) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Employee.COLUMN_NAME_NAME, name);
        cv.put(DBContract.Employee.COLUMN_NAME_POSITION, position);
        cv.put(DBContract.Employee.COLUMN_NAME_PASSWORD, password);
        int n = db.update(DBContract.Employee.TABLE_NAME, cv, DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID + " = ?",
                new String[] { Integer.toString(id) });
        if(n > 0) {
            Toast.makeText(this, "Запись отредактирована", Toast.LENGTH_SHORT).show();
            getSupportLoaderManager().getLoader(0).forceLoad();
        }

    }

    @Override
    public void addEmployee(String name, String position, String password) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Employee.COLUMN_NAME_NAME, name);
        cv.put(DBContract.Employee.COLUMN_NAME_POSITION, position);
        cv.put(DBContract.Employee.COLUMN_NAME_PASSWORD, password);

        long n = db.insert(DBContract.Employee.TABLE_NAME, null, cv);
        if(n > 0) {
            Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }

    static class EmployeeLoader extends CursorLoader {

        private SQLiteDatabase db;

        public EmployeeLoader(Context context, SQLiteDatabase db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.query(DBContract.Employee.TABLE_NAME, null, null, null, null, null, null);
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBContract.Employee.COLUMN_NAME_NAME));
                Log.d("DEBUG", "id: " + id + " name: " + name);
            } while(cursor.moveToNext());
            cursor.moveToFirst();
            return cursor;
        }
    }
}
