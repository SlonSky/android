package slon.sky.dev.fragmentapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Slon on 25.04.2017.
 */

public class TimesAdapter extends BaseAdapter {
    private Context context;
    private List<Model> times;
    private TimeChanger timeChanger;

    public TimesAdapter(Context context, List<Model> times, TimeChanger timeChanger) {
        this.context = context;
        this.times = times;
        this.timeChanger = timeChanger;
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int position) {
        return times.get(position).getContent();
    }

    @Override
    public long getItemId(int position) {
        return times.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_layout, parent, false);
        }

        TextView text = (TextView) view.findViewById(R.id.item_text);
        text.setText(times.get(position).getContent());

        final int p = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChanger.timeChanged(times.get(p).getId());
            }
        });

        return view;
    }

    public interface TimeChanger {
        void timeChanged(int newId);
    }
}
