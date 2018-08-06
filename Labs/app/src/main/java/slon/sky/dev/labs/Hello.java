package slon.sky.dev.labs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Hello extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Intent intent = getIntent();
        TextView tvMessage = (TextView) findViewById(R.id.textView2);

        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");

        tvMessage.setText("What's up, " + firstName + " " + lastName + "!?");

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

}
