package slon.sky.dev.lab6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Slon on 09.05.2017.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bitmap> images;
    private ArrayList<Integer> numbers;
    private ArrayList<String> names;

    public GridAdapter(Context context, ArrayList<Integer> numbers, ArrayList<String> names) {
        this.context = context;
        images = new ArrayList<>();
        this.numbers = numbers;
        this.names = names;
    }

    @Override
    public int getCount() {
        return images.size();
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

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView num = (TextView) view.findViewById(R.id.number);
        TextView name = (TextView) view.findViewById(R.id.name);

        image.setImageBitmap(images.get(position));
        num.setText(Integer.toString(numbers.get(position)));
        name.setText(names.get(position));

        return view;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }
}
