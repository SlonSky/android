package slon.sky.dev.postcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class CardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        GridView grid = (GridView) findViewById(R.id.gridView);
        CardAdapter adapter = new CardAdapter(this);
        grid.setAdapter(adapter);
    }
}
