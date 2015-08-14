package com.siarma.banner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by siarma on 8/7/15.
 */
public class UnlockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Broadcast received. Uptime: " + SystemClock.uptimeMillis());
        if (startedTodayCount(context) < 3) {
            final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, 1);
            final Intent acitivyIntent = new Intent(context, BannerActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, acitivyIntent, PendingIntent.FLAG_ONE_SHOT);
            am.set(AlarmManager.RTC, now.getTimeInMillis(), pendingIntent);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pending", true).apply();
        } else {
            Log.d("UnlockReceiver", "Skipping banner");
        }
    }

    private int startedTodayCount(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Set<String> launches = prefs.getStringSet("lastDate", Collections.<String>emptySet());
        final Calendar now = Calendar.getInstance();
        final Iterator<String> iter = launches.iterator();
        while (iter.hasNext()) {
            final String launch = iter.next();
            Date date = null;
            try {
                date =new SimpleDateFormat("yyyyMMddHHmmss").parse(launch);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (now.getTimeInMillis() - cal.getTimeInMillis() > 24 * 3600 * 1000) {
                iter.remove();
            }
        }
        prefs.edit().putStringSet("lastDate", launches).apply();

        return launches.size();
    }
}
