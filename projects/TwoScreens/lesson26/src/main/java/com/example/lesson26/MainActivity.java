package com.example.lesson26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnTime = (Button) findViewById(R.id.btnTime);
        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnTime.setOnClickListener(this);
        btnDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        String action;
        switch (view.getId()){
            case R.id.btnTime:
                action = "com.example.intent.action.showtime";
                break;
            case R.id.btnDate:
                action = "com.example.intent.action.showdate";
                break;
            case R.id.btnDateEx:
                action = "com.example.intent.action.showdate";
                break;
            default:
                return;
        }
        startActivity(new Intent(action));
    }
}
