package slon.sky.dev.lab5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;

/**
 * Created by Slon on 09.05.2017.
 */

public class NumberDialog extends DialogFragment {
    private int number;

    static NumberDialog newInstance(int number) {
        NumberDialog d = new NumberDialog();
        Bundle args = new Bundle();
        args.putInt("number", number);
        d.setArguments(args);

        return d;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        number = b.getInt("number");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You pressed number " + number);
        builder.setTitle("Number pressed");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
