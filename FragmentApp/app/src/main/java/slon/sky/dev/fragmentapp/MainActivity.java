package slon.sky.dev.fragmentapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TimesAdapter.TimeChanger, BattlesAdapter.BattleChanger{

    private Data data;
    private ListView timesList;
    private ListView battlesList;
    private TextView details;

    private BattlesAdapter battlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new Data();

        timesList = (ListView) findViewById(R.id.times_list);
        timesList.setAdapter(new TimesAdapter(this, data.getTimes(), this));

        battlesList = (ListView) findViewById(R.id.battles_list);
        battlesAdapter = new BattlesAdapter(this, new ArrayList<Model>(), this);
        battlesList.setAdapter(battlesAdapter);

        details = (TextView) findViewById(R.id.details);
    }

    @Override
    public void timeChanged(int newId) {
        battlesAdapter.setBattles(data.getBattlesByTime(newId));
        battlesAdapter.notifyDataSetChanged();
    }

    @Override
    public void battleChanged(int newId) {
        if(data.getDetailsOfBattle(newId) == null) {
            return;
        }
        String text = data.getDetailsOfBattle(newId).getContent();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            details.setText(text);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("details", text);
            startActivity(intent);
        }
    }
}
