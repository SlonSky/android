package com.example.lesson24;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Slon on 18.08.2016.
 */
public class SecondActivity extends android.app.Activity implements View.OnClickListener{

    final String TAG = "Activity two";

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        back = (Button) findViewById(R.id.button2);
        back.setOnClickListener(this);

        Log.d(TAG, "create");
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == back.getId()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Log.d(TAG, "BACK CLICKED!");
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
