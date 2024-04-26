package com.team18.recordapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean isSetTime = false;
    private int hour, minute, time;
    private FragmentRecord fragmentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        fragmentRecord = new FragmentRecord();
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(fragmentRecord, "RECORD");
        vpAdapter.addFragment(new FragmentListRecord(), "LIST RECORD");
        viewPager.setAdapter(vpAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1) {
            Intent intent = new Intent(this, SelectTimeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == R.id.item2) {
//            Intent intent = new Intent(this, ChartActivity.class);
//            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {

            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                isSetTime = data.getBooleanExtra("is_set_time", false);
                if (isSetTime) {
                    hour = data.getIntExtra("hour", -1);
                    minute = data.getIntExtra("minute", -1);
                    time = data.getIntExtra("time", -1);
                    Calendar cal = Calendar.getInstance();
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY); // Lấy giờ hiện tại (theo đồng hồ 24 giờ)
                    int currentMinute = cal.get(Calendar.MINUTE);

                    long hourRemain = (hour - currentHour + 24) % 24;
                    long minuteRemain = (minute - currentMinute + 60) % 60;
                    long timeRemain = hourRemain*60*60*1000 + minuteRemain*60*1000;

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fragmentRecord.recordfollowMinute(time);
                                }
                            });
                        }
                    };
                    timer.schedule(timerTask, timeRemain);
                }

                // Sử dụng kết quả result bằng cách hiện Toast

            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }
    }
}