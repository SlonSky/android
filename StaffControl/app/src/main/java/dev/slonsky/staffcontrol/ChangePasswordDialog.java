package dev.slonsky.staffcontrol;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Slon on 14.08.2017.
 */

public class ChangePasswordDialog extends DialogFragment{

    private boolean isAdmin;
    private int id;
    private String password;

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newRepeatPassword;


    public static ChangePasswordDialog newInstance(boolean isAdmin) {
        return newInstance("", -1, isAdmin);
    }

    public static ChangePasswordDialog newInstance(String password, int id, boolean isAdmin) {

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle args = new Bundle();
        args.putString("password", password);
        args.putInt("id", id);
        args.putBoolean("isAdmin", isAdmin);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        password = b.getString("password", "");
        id = b.getInt("id", -1);
        isAdmin = b.getBoolean("isAdmin", false);
        if(isAdmin) {
            SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("StaffControl", MODE_PRIVATE);
            password = preferences.getString("admin_password", "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View menuView = inflater.inflate(R.layout.password_dialog, null);

        oldPassword = (EditText) menuView.findViewById(R.id.old_password);
        newPassword = (EditText) menuView.findViewById(R.id.new_password);
        newRepeatPassword = (EditText) menuView.findViewById(R.id.new_password_copy);

        builder.setView(menuView);
        builder.setTitle("Сменить пароль");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                 if(newPassword.getText().toString()
                         .equals(newRepeatPassword.getText().toString())) {

                     if(oldPassword.getText().toString().equals(password)) {

                         String password = newPassword.getText().toString();

                         if(isAdmin) {
                             notifyChanged(changeAdminPassword(password));
                         } else {
                             new ChangePasswordTask(getContext()).execute(password);
                         }
                     } else {
                         Toast.makeText(getContext(), "Неверный старый пароль", Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Toast.makeText(getContext(), "Новый пароль не совпадает с повторно введенным", Toast.LENGTH_SHORT).show();
                 }
            }
        });

        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
        });


        return builder.create();
    }

    private class ChangePasswordTask extends AsyncTask<String, Void, Void> {

        private Context context;
        private SQLiteDatabase db;
        private int res;

        public ChangePasswordTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = DBManager.getInstance().openDB();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DBManager.getInstance().closeDB();

            notifyChanged(res > 0);
        }

        @Override
        protected Void doInBackground(String... params) {
            ContentValues cv = new ContentValues();
            cv.put(DBContract.Employee.COLUMN_NAME_PASSWORD, params[0]);

            res = db.update(DBContract.Employee.TABLE_NAME,
                    cv,
                    DBContract.Employee.COLUMN_NAME_EMPLOYEE_ID + " = ?",
                    new String[]{ Integer.toString(id)});
            return null;
        }
    }

    private void notifyChanged(boolean changed) {
        String mes = changed ? "Пароль изменен" : "Пароль не был изменен";
        Toast.makeText(getActivity(), mes, Toast.LENGTH_SHORT).show();
    }


    private boolean changeAdminPassword(String newPassword) {
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("StaffControl", MODE_PRIVATE);
        preferences.edit().putString("admin_password", newPassword).apply();
        return true;
    }
}
