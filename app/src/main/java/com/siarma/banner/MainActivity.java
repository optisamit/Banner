package com.siarma.banner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onStop() {
        super.onStop();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pending", false)) {
            final AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            final Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, 5);
            final Intent acitivyIntent = new Intent(this, BannerActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 1, acitivyIntent, PendingIntent.FLAG_ONE_SHOT);
            am.set(AlarmManager.RTC, now.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Intent acitivyIntent = new Intent(this, BannerActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, acitivyIntent, PendingIntent.FLAG_ONE_SHOT);
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
