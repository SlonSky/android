package slon.sky.dev.akts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Boolean> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new MarkAdapter(this));

        TextView amount = (TextView) findViewById(R.id.amount);
        int amt = 0;
        for(Boolean b: results) {
            if(b) {
                amt++;
            }
        }
        amount.setText(""+amt);

    }

    class MarkAdapter extends BaseAdapter {

        private Context context;

        public MarkAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item, parent, false);
            }

            final TextView number = (TextView) view.findViewById(R.id.number);
            final TextView result = (TextView) view.findViewById(R.id.result);

            number.setText("Question " + (position+1) + ": ");
            if(results.get(position)) {
                result.setText("Correct");
                result.setTextColor(getResources().getColor(R.color.correct));
            } else {
                result.setText("Wrong");
                result.setTextColor(getResources().getColor(R.color.wrong));
            }

            return view;
        }
    }

    private void init() {
        results = new ArrayList<>();
        results.add(false);
        results.add(true);
        results.add(true);
        results.add(true);
    }
}
