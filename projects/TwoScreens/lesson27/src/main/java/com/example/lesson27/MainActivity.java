package com.example.lesson27;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnTime = (Button) findViewById(R.id.btnTime);
        Button btnDate = (Button) findViewById(R.id.btnDate);

        btnTime.setOnClickListener(this);
        btnDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.btnTime:
                intent = new Intent("com.example.intent.action.showtime");
                startActivity(intent);
                break;
            case R.id.btnDate:
                intent = new Intent("com.example.intent.action.showdate");
                startActivity(intent);
                break;
        }
    }
}
