package com.siarma.banner;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class BannerActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("pending").apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        final Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);
        final Intent acitivyIntent = new Intent(this, BannerActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 1, acitivyIntent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC, now.getTimeInMillis(), pendingIntent);
        setContentView(R.layout.banner_layout);

        //this.getWindow().setAttributes(params);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        this.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, width * 50 / 320);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        /*ViewGroup.LayoutParams lp = decorView.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(100, 200);
        }
        lp.width = 400;
        lp.height = 200;*/
        decorView.setPadding(0, 0, 0, 0);

        decorView.removeAllViews();
        decorView.addView(LayoutInflater.from(this).inflate(R.layout.banner_layout, null));


        logTrigger(this);
    }

    private void logTrigger(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Set<String> launches = new ConcurrentSkipListSet<>(prefs.getStringSet("lastDate", new HashSet<String>(1)));
        launches.add(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        prefs.edit().putStringSet("lastDate", launches).apply();
    }

}
