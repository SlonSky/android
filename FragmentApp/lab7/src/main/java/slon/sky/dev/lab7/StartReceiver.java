package slon.sky.dev.lab7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Slon on 09.05.2017.
 */

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        }
    }
}
