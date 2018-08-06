package slon.sky.dev.labs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etFirstName = (EditText) findViewById(R.id.firstName);
        final EditText etLastName = (EditText) findViewById(R.id.lastName);

        Button btnRevive = (Button) findViewById(R.id.button);
        btnRevive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this.getBaseContext(), Hello.class);
                intent.putExtra("firstName", etFirstName.getText().toString());
                intent.putExtra("lastName", etLastName.getText().toString());
                startActivity(intent);
            }
        });
    }
}
