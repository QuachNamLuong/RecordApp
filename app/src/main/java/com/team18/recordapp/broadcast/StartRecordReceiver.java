package com.team18.recordapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.team18.recordapp.MainActivity;
import com.team18.recordapp.service.RecordingService;
import com.team18.recordapp.util.Clock;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class StartRecordReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == "schedule_record") {
            Intent newIntent = new Intent(context, MainActivity.class);
            int time = intent.getIntExtra("time", -1);
            newIntent.putExtra("time", time);
            context.startActivity(newIntent);
        }
    }
}
