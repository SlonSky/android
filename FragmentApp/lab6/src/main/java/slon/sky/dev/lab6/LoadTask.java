package slon.sky.dev.lab6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Slon on 09.05.2017.
 */

public class LoadTask extends AsyncTask<String, Bitmap, Void> {
    private GridAdapter adapter;
    private Context context;

    public LoadTask(Context context, GridAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(String... params) {
        for (String param : params) {
            URL url = null;
            try {
                url = new URL(param);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                publishProgress(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        adapter.getImages().add(values[0]);
        adapter.notifyDataSetChanged();
    }
}
