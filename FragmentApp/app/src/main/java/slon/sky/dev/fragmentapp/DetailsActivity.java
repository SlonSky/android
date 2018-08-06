package slon.sky.dev.fragmentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String details = getIntent().getStringExtra("details");
        if(details != null) {
            TextView tv = (TextView) findViewById(R.id.details);
            tv.setText(details);
        }
    }
}
