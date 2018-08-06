package com.example.lesson27;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        Intent intent = getIntent();
        String action = intent.getAction();
        String format = "", textInfo = "";

        if(action.equals("com.example.intent.action.showtime")){
            format = "HH:mm:ss";
            textInfo = "Time: ";
        } else if(action.equals("com.example.intent.action.showdate")){
            format = "dd.MM.yyyy";
            textInfo = "Date: ";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateTime = sdf.format(new Date(System.currentTimeMillis()));

        TextView tvDate = (TextView) findViewById(R.id.tvInfo);
        tvDate.setText(textInfo + dateTime);
    }
}
