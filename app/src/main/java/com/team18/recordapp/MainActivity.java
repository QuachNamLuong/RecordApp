package com.team18.recordapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.team18.recordapp.fragment.ListRecordFragment;
import com.team18.recordapp.fragment.LogInFragment;
import com.team18.recordapp.fragment.RecordFragment;
import com.team18.recordapp.fragment.ScheduleFragment;
import com.team18.recordapp.util.Clock;

public class MainActivity extends AppCompatActivity {
    private RecordFragment recordFragment = new RecordFragment();
    private DrawerLayout drawerLayout;
    private ImageButton btn_menu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        setEvent();
        replaceFragment(recordFragment);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.close();
            return;
        }
        super.onBackPressed();
    }

    private void setEvent() {
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (itemId == R.id.record_item) {
                    replaceFragment(recordFragment);
                }
                else if (itemId == R.id.list_record_item) {
                    replaceFragment(ListRecordFragment.newInstance());
                }
                else if (itemId == R.id.schedule_item) {
                    replaceFragment(ScheduleFragment.newInstance());
                }
                else if (itemId == R.id.statistics_item) {
                    replaceFragment(ListRecordFragment.newInstance());
                }
                else if (itemId == R.id.account_item) {
                    replaceFragment(ListRecordFragment.newInstance());
                }
                else if (itemId == R.id.login_item) {
                    replaceFragment(LogInFragment.newInstance());
                }
                else if (itemId == R.id.logout_item) {
                    replaceFragment(ListRecordFragment.newInstance());
                }
                drawerLayout.close();

                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setControl() {
        drawerLayout = findViewById(R.id.drawer_layout);
        btn_menu = findViewById(R.id.btn_menu);
        navigationView = findViewById(R.id.nav_view);
    }
}