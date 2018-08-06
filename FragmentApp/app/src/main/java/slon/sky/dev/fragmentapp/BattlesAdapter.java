package slon.sky.dev.fragmentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Slon on 26.04.2017.
 */

public class BattlesAdapter extends BaseAdapter {
    private Context context;
    private List<Model> battles;
    private BattleChanger battleChanger;

    public BattlesAdapter(Context context, List<Model> battles, BattleChanger battleChanger) {
        this.context = context;
        this.battles = battles;
        this.battleChanger = battleChanger;
    }

    @Override
    public int getCount() {
        return battles.size();
    }

    @Override
    public Object getItem(int position) {
        return battles.get(position).getContent();
    }

    @Override
    public long getItemId(int position) {
        return battles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_layout, parent, false);
        }

        TextView text = (TextView) view.findViewById(R.id.item_text);
        text.setText(battles.get(position).getContent());

        final int p = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                battleChanger.battleChanged(battles.get(p).getId());
            }
        });

        return view;
    }

    public void setBattles(List<Model> battles) {
        this.battles = battles;
    }

    public interface BattleChanger {
        void battleChanged(int newId);
    }
}
