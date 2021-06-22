package com.example.hikertracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class yourActivityRunOnStartup extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);


            Intent i1 = new Intent(context, GoogleService.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i1.putExtra("Time", String.valueOf(24));
            i1.putExtra("BoolTrue", false);
            context.startService(i1);

        }
    }

}


