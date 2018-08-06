package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final int MENU_RESET_ID = 1;
    final int MENU_QUIT_ID = 2;

    EditText etNum1;
    EditText etNum2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    TextView tvResult;

    String operation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNum1 = (EditText) findViewById(R.id.etNum1);
        etNum2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        tvResult = (TextView) findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnMult.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        float num1 = 0f;
        float num2 = 0f;
        float result = 0;

        if(TextUtils.isEmpty(etNum1.getText().toString())
                || TextUtils.isEmpty(etNum2.getText().toString())){
            return;
        }

        num1 = Float.parseFloat(etNum1.getText().toString());
        num2 = Float.parseFloat(etNum2.getText().toString());

        Log.d("Num", "onClick: " + num1 + " " + num2);

        switch (view.getId()){
            case R.id.btnAdd:
                operation = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                operation = "-";
                result = num1 - num2;
                break;
            case R.id.btnDiv:
                operation = "/";
                result = num1 / num2;
                break;
            case R.id.btnMult:
                operation = "*";
                result = num1 * num2;
                break;
        }

        if(!divNull(num2, operation)) {
            tvResult.setText(num1 + " " + operation + " " + num2 + " = " + result);
        }
    }

    public boolean divNull(float num, String operation){
        if(num == 0 && operation.equals("/")) {
            Toast.makeText(this, "Делить на ноль нельзя", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_RESET_ID, 0, "Reset");
        menu.add(0, MENU_QUIT_ID, 0, "Quit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_RESET_ID:
                etNum1.setText("");
                etNum2.setText("");
                tvResult.setText("");
                break;
            case MENU_QUIT_ID:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
