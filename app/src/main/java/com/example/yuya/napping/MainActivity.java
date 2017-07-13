package com.example.yuya.napping;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.yuya.napping.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ALARM_TIME = "alarm_time";
    Button button;
    PreferenceUtil pref;
    TextView mTextField;
    RadioGroup radioTimeGroup;
    RadioButton radioTimeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextField = (TextView) findViewById(R.id.timer1);

        pref = new PreferenceUtil(this);
        setupViews();
        setListeners();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupViews() {
        button = (Button) findViewById(R.id.button);
        //alarmSwitch = (Switch) findViewById(R.id.alarm_switch);
        radioTimeGroup = (RadioGroup)findViewById(R.id.radioGroup);


    }

    private void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = radioTimeGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioTimeButton = (RadioButton) findViewById(selectedId);
                CharSequence mins = radioTimeButton.getText();
                //register(100000);

                CountDownTimer aCounter = new CountDownTimer(10000 , 1000) {
                    public void onTick(long millisUntilFinished) {
                        mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        mTextField.setText("Finished");
                        Intent startActivityIntent = new Intent(MainActivity.this, PlaySoundActivity.class);
                        //startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startActivityIntent);
                    }
                };
                aCounter.start();

            }

        });
    }
    // Register Broadcast
    private void register(long alarmTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }
        // Preserve
        pref.setLong(ALARM_TIME, alarmTimeMillis);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setClass(this, AlarmReceiver.class);
        // 複数のアラームを登録する場合はPendingIntent.getBroadcastの第二引数を変更する
        // 第二引数が同じで第四引数にFLAG_CANCEL_CURRENTがセットされている場合、2回以上呼び出されたときは
        // あとからのものが上書きされる
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
