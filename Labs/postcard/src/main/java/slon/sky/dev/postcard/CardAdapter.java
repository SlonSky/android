package slon.sky.dev.postcard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Slon on 11.04.2017.
 */

public class CardAdapter extends BaseAdapter {

    private ArrayList<String> titles;
    private ArrayList<String> messages;
    private ArrayList<Drawable> images;
    private Context context;

    public CardAdapter(Context context) {
        this.context = context;

        titles = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.titles)));
        messages = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.descriptions)));
        images = new ArrayList<>();

        for(String title: titles) {
            String uri = "@drawable/" + title.toLowerCase();
            int res = context.getResources().getIdentifier(uri, null, context.getPackageName());
            images.add(context.getResources().getDrawable(res));
        }
    }

    @Override
    public int getCount() {
        return titles.size();
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
            view = inflater.inflate(R.layout.card, parent, false);
        }

        final TextView title = (TextView) view.findViewById(R.id.card_title);
        final ImageView image = (ImageView) view.findViewById(R.id.card_image);

        title.setText(titles.get(position));
        image.setImageDrawable(images.get(position));

        final int pos = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CardInfoActivity.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("message", messages.get(pos));
                intent.putExtra("imageUri", "@drawable/" + title.getText().toString().toLowerCase());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
