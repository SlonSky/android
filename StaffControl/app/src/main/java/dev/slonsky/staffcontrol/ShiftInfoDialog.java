//package dev.slonsky.staffcontrol;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.widget.SimpleCursorAdapter;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
///**
// * Created by Slon on 17.08.2017.
// */
//
//public class ShiftInfoDialog extends DialogFragment {
//
//    private String name;
//    private String date;
//    private String startTime;
//    private String startUri;
//    private String endTime;
//    private String endUri;
//    private int shiftId;
//
//    public static ShiftInfoDialog getInstance(String name, String date, String duration, String startTime, String startUri,
//                                              String endTime, String endUri, int shiftId) {
//        ShiftInfoDialog dialog = new ShiftInfoDialog();
//
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        args.putString("date", date);
//        args.putString("startTime", startTime);
//        args.putString("duration", duration);
//        args.putString("startUri", startUri);
//        args.putString("endTime", endTime);
//        args.putString("endUri", endUri);
//        args.putInt("shiftId", shiftId);
//
//        dialog.setArguments(args);
//        return dialog;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.shift_info_dialog, null);
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Данные смены");
//
//        ((TextView)view.findViewById(R.id.shift_info_employee_name)).setText(args.getString("name"));
//        ((TextView)view.findViewById(R.id.shift_info_date)).setText(args.getString("date"));
//        ((TextView)view.findViewById(R.id.duration)).setText(DBHelper.makeTimeFromMillis(args.getLong("duration")));
//        ((TextView)view.findViewById(R.id.shift_info_start_time)).setText(args.getString("startTime"));
//        ((TextView)view.findViewById(R.id.shift_info_end_time)).setText(args.getString("endTime"));
//
//
////        Picasso.with(getContext()).load(args.getString("startUri")).into((ImageView)view.findViewById(R.id.start_time_photo));
////        Picasso.with(getContext()).load(args.getString("endUri")).into((ImageView)view.findViewById(R.id.end_time_photo));
//
//        ListView rests = (ListView) view.findViewById(R.id.rest_list);
//
//        String[] from = { DBContract.Rest.COLUMN_NAME_START_TIME, DBContract.Rest.COLUMN_NAME_END_TIME };
//        int[] to = {R.id.rest_start, R.id.rest_end};
//        SQLiteDatabase db = DBManager.getInstance().openDB();
//
//        rests.setAdapter(new SimpleCursorAdapter(getContext(), R.layout.rest_item,
//                db.query(DBContract.Rest.TABLE_NAME, null, DBContract.Rest.COLUMN_NAME_SHIFT_ID + " = ?",
//                        new String[] { Integer.toString(args.getInt("shiftId"))}, null, null, null),
//                        from ,to));
//        builder.setView(view);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        return builder.create();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        DBManager.getInstance().closeDB();
//    }
//}
