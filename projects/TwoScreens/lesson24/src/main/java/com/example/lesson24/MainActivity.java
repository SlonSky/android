package com.example.lesson24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "Main activity";

    Button secondAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        secondAct = (Button) findViewById(R.id.button);
        secondAct.setOnClickListener(this);

        Log.d(TAG, "create");
    }

    @Override
    public void onClick(View view){
        if(view.getId() == secondAct.getId()){
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);

            Log.d(TAG, "button CLICKED!");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
    }
}
