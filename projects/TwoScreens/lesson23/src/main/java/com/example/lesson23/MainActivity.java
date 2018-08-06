package com.example.lesson23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    final String TAG = "States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "main - onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "main - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "main - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "main - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "main - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "main - onDestroy");
    }
}
