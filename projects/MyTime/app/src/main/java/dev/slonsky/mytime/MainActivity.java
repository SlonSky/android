package dev.slonsky.mytime;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Chronometer mChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        Button btnStart = (Button) findViewById(R.id.startButton);
        Button btnStop = (Button) findViewById(R.id.stopButton);
        Button btnReset = (Button) findViewById(R.id.resetButton);

        btnStart.setOnClickListener(new TimeListener());
        btnStop.setOnClickListener(new TimeListener());
        btnReset.setOnClickListener(new TimeListener());

    }

    private class TimeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.startButton:
                    onStartClick(v);
                    Toast.makeText(v.getContext(), "Start", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.stopButton:
                    onStopClick(v);
                    break;
                case R.id.resetButton:
                    onResetClick(v);
                    break;
            }
        }
    }

    public void onStartClick(View view){
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void onStopClick(View view){
        mChronometer.stop();
    }

    public void onResetClick(View view) {
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }
}
