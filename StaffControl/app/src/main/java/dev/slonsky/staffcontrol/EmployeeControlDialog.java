package dev.slonsky.staffcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Slon on 16.08.2017.
 */

public class EmployeeControlDialog extends DialogFragment {
    private int id;
    private EditText name;
    private EditText position;
    private EditText password;

    private String nameText;
    private String positionText;
    private String passwordText;

    private OnEmployeeControlDialogListener listener;

    private boolean edit;

    public static EmployeeControlDialog getInstanceAdd() {
        EmployeeControlDialog fragment = new EmployeeControlDialog();

        Bundle b = new Bundle();
        b.putBoolean("edit", false);

        fragment.setArguments(b);
        return fragment;
    }

    public static EmployeeControlDialog getInstanceEdit(int id, String name, String position, String password) {
        EmployeeControlDialog fragment = new EmployeeControlDialog();

        Bundle b = new Bundle();
        b.putInt("id", id);
        b.putString("name", name);
        b.putString("position", position);
        b.putString("password", password);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        edit = b.getBoolean("edit", true);
        if(edit) {
            id = b.getInt("id");
            nameText = b.getString("name");
            positionText = b.getString("position");
            passwordText = b.getString("password");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.employee_edit_dialog, null);


        name = (EditText) dialog.findViewById(R.id.edit_name);
        position = (EditText) dialog.findViewById(R.id.edit_position);
        password = (EditText) dialog.findViewById(R.id.edit_password);

        if(edit) {
            name.setText(nameText);
            position.setText(positionText);
            password.setText(passwordText);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (name.getText().toString().length() == 0 || position.getText().toString().length() == 0
                        || password.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Нужно заполнить все поля", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit) {
                    listener.editEmployee(id, name.getText().toString(), position.getText().toString(), password.getText().toString());
                } else {
                    listener.addEmployee(name.getText().toString(), position.getText().toString(), password.getText().toString());
                }
            }
        });

        builder.setView(dialog);
        builder.setTitle( edit ? "Редактировать запись" : "Добавить запись" );
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnEmployeeControlDialogListener) context;
    }



    public interface OnEmployeeControlDialogListener {
        void editEmployee(int id, String name, String position, String password);
        void addEmployee(String name, String position, String password);
    }
}
