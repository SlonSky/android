package dev.slonsky.staffcontrol;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        Bundle args = getIntent().getExtras();

        ((TextView)findViewById(R.id.shift_info_employee_name)).setText(args.getString("name"));
        ((TextView)findViewById(R.id.shift_info_date)).setText(args.getString("date"));
        ((TextView)findViewById(R.id.duration)).setText(DBHelper.makeTimeFromMillis(args.getLong("duration")));
        ((TextView)findViewById(R.id.shift_info_start_time)).setText(args.getString("startTime"));
        ((TextView)findViewById(R.id.shift_info_end_time)).setText(args.getString("endTime"));


        Picasso.with(this).load(args.getString("startUri")).into((ImageView)findViewById(R.id.start_time_photo));
        Picasso.with(this).load(args.getString("endUri")).into((ImageView)findViewById(R.id.end_time_photo));

        ListView rests = (ListView) findViewById(R.id.rest_list);

        String[] from = { DBContract.Rest.COLUMN_NAME_START_TIME, DBContract.Rest.COLUMN_NAME_END_TIME };
        int[] to = {R.id.rest_start, R.id.rest_end};
        SQLiteDatabase db = DBManager.getInstance().openDB();

        rests.setAdapter(new SimpleCursorAdapter(this, R.layout.rest_item,
                db.query(DBContract.Rest.TABLE_NAME, null, DBContract.Rest.COLUMN_NAME_SHIFT_ID + " = ?",
                        new String[] { Integer.toString(args.getInt("shiftId"))}, null, null, null),
                from ,to));
    }

    @Override
    protected void onStop() {
        super.onStop();
        DBManager.getInstance().closeDB();
    }
}
