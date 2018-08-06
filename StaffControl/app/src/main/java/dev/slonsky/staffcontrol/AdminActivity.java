package dev.slonsky.staffcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Class c = null;
            switch (v.getId()) {
                case R.id.employees:
                    c = EmployeeControlActivity.class;
                    break;
                case R.id.shifts:
                    c = ShiftControlActivity.class;
                    break;
                case R.id.exit:
                    c = AuthActivity.class;
                    break;
            }
            if (c != null) {
                Intent i = new Intent(AdminActivity.this, c);
                startActivity(i);
                if(c == AuthActivity.class) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.employees).setOnClickListener(listener);
        findViewById(R.id.shifts).setOnClickListener(listener);
        findViewById(R.id.exit).setOnClickListener(listener);

        DBManager.initializeInstance(new DBHelper(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        DBManager.initializeInstance(new DBHelper(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                Intent intent = new Intent(AdminActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.settings:
                ChangePasswordDialog dialog = ChangePasswordDialog.newInstance(true);
                dialog.show(getSupportFragmentManager(), "dialog");
                break;

            case R.id.clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Удаление всех записей");
                builder.setMessage("Вы дейстивтельно хотите удалить все записи (кроме записей сотрудников)?");
                builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = DBManager.getInstance().openDB();
                        db.delete(DBContract.Shift.TABLE_NAME, null,null);
                        db.delete(DBContract.Rest.TABLE_NAME, null,null);
                        DBManager.getInstance().closeDB();
                    }
                });
                builder.create().show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }




}
