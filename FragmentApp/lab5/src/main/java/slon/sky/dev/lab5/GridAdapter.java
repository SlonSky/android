package slon.sky.dev.lab5;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Slon on 09.05.2017.
 */

public class GridAdapter extends BaseAdapter {

    Context context;
    List<Integer> numbers;
    List<Integer> colors;

    public GridAdapter(Context context, int size) {
        this.context = context;
        numbers = new ArrayList<>();
        colors = new ArrayList<>();

        Random rand = new Random();
        for(int i = 0; i < size; i++) {
            numbers.add(rand.nextInt(99));
            colors.add(Color.rgb(55+rand.nextInt(200),55+rand.nextInt(200),55+rand.nextInt(200)));
        }
    }

    @Override
    public int getCount() {
        return numbers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        TextView text = (TextView) view.findViewById(R.id.text);

        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(colors.get(position));
        d.setStroke(2, Color.BLACK);

        image.setBackground(d);
        text.setText(Integer.toString(numbers.get(position)));

        final int number = numbers.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberDialog dialog = NumberDialog.newInstance(number);
                final Activity activity = (Activity) context;
                dialog.show(activity.getFragmentManager(), "dialog");
            }
        });

        return view;
    }
}
