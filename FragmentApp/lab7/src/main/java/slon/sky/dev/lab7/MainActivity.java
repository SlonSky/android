package slon.sky.dev.lab7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mValue;
    TextView mResult;
    Button factorial;
    Button fibonacci;
    Button exponent;

    ResultReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factorial = (Button) findViewById(R.id.factorial);
        fibonacci = (Button) findViewById(R.id.fibonacci);
        exponent = (Button) findViewById(R.id.exponent);

        mResult = (TextView) findViewById(R.id.answer);
        mValue = (EditText) findViewById(R.id.editX);

        factorial.setOnClickListener(new CalcListener());
        fibonacci.setOnClickListener(new CalcListener());
        exponent.setOnClickListener(new CalcListener());

        receiver = new ResultReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CalculationService.TASK_BEGIN);
        filter.addAction(CalculationService.TASK_FINISH);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    class CalcListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String type = "";
            switch(v.getId()) {
                case R.id.factorial:
                    type = CalculationService.FACTORIAL;
                    break;
                case R.id.fibonacci:
                    type = CalculationService.FIBONACCI;
                    break;
                case R.id.exponent:
                    type = CalculationService.EXPONENT;
                    break;
            }

            Intent intent = new Intent(MainActivity.this, CalculationService.class);
            intent.putExtra("type", type);
            intent.putExtra("x", Integer.parseInt(mValue.getText().toString()));
            startService(intent);
        }
    }

    class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(CalculationService.TASK_BEGIN)) {
                factorial.setEnabled(false);
                fibonacci.setEnabled(false);
                exponent.setEnabled(false);
                Toast.makeText(context, "Ожидайте", Toast.LENGTH_SHORT).show();
            } else if(intent.getAction().equals(CalculationService.TASK_FINISH)) {
                factorial.setEnabled(true);
                fibonacci.setEnabled(true);
                exponent.setEnabled(true);

                String result = intent.getStringExtra("result");
                mResult.setText(result);
            }
        }
    }
}
