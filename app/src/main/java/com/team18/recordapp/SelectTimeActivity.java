package com.team18.recordapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SelectTimeActivity extends AppCompatActivity {
    private ImageButton ibtnTurnBack;
    private TimePicker timePicker;
    private TextView tvSelectedTime;
    private Button btnConfirm;
    private boolean isSetTime;
    private Intent intent;
    private String selectedTime = "00:00";
    private Button btnCancel;
    private EditText edtTimeRecord;
    private String recordTime = "0";
    private File settingFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_time);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setControl();

        setEvent();

    }

    private void setControl(){
        File currentDir = getFilesDir();
        settingFile = new File(currentDir, "setting.txt");
        ibtnTurnBack = findViewById(R.id.ibtn_turn_back);
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvSelectedTime = findViewById(R.id.tv_selected_time);
        btnCancel = findViewById(R.id.btn_cancel);
        edtTimeRecord = findViewById(R.id.edt_time_record);
        edtTimeRecord.setText(selectedTime);
        intent = new Intent(this, MainActivity.class);
        readSettingFile();
    }

    private void setEvent() {
        ibtnTurnBack.setOnClickListener(v -> {
            intent.putExtra("is_set_time", false);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            edtTimeRecord.setText("");
            tvSelectedTime.setText("Thời gian đã chọn:");
            intent.putExtra("is_set_time", false);
            writeSettingFile();
            finish();
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTimeRecord.getText().toString().isEmpty()){
                    edtTimeRecord.setError("Thời gian ghi không được để trống");
                }
                else if (Integer.parseInt(edtTimeRecord.getText().toString()) == 0) {
                    edtTimeRecord.setError("Thời gian ghi phải > 0");
                }
                else {
                    recordTime = edtTimeRecord.getText().toString().trim();
                    selectedTime = String.format(Locale.getDefault(),
                            "%02d:%02d",
                            timePicker.getHour(), timePicker.getMinute());

                    String txtSelectedTime = "Thời gian đã chọn: " + selectedTime;
                    tvSelectedTime.setText(txtSelectedTime);
                    writeSettingFile();
                    isSetTime = true;
                    intent.putExtra("is_set_time", true);
                    if (isSetTime) {
                        intent.putExtra("hour", timePicker.getHour());
                        intent.putExtra("minute", timePicker.getMinute());
                        intent.putExtra("time", Integer.parseInt(edtTimeRecord.getText().toString()));
                    }
                    finish();
                }

            }
        });
    }

    @Override
    public void finish() {
        setResult(SelectTimeActivity.RESULT_OK, intent);
        super.finish();
    }

    private void initSettingFile() {
        try {
            FileOutputStream fos = new FileOutputStream(settingFile);
            String settingContent = "false,00:00,0";
            fos.write(settingContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSettingFile() {
        if (settingFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(settingFile);
                recordTime = (recordTime.isEmpty())? "0" : recordTime;
                String settingContent = isSetTime + "," + selectedTime + "," + recordTime;
                fos.write(settingContent.getBytes());
                fos.close();
            } catch (IOException e) {
                Log.d("IO file setting", "Lỗi khi ghi file setting");
            }
        } else {
            initSettingFile();
        }
    }

    private void readSettingFile() {
        if (settingFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(settingFile));
                String line = br.readLine();
                if (line != null) {
                    String[] settingContent = line.split(",");
                    isSetTime = Boolean.parseBoolean(settingContent[0].trim());
                    selectedTime = settingContent[1].trim();
                    recordTime = settingContent[2].trim();
                    recordTime = (recordTime.equals("0"))? "" : recordTime;
                    edtTimeRecord.setText(recordTime);

                    String txtSelectedTime = "Thời gian đã chọn: " + selectedTime;
                    tvSelectedTime.setText(txtSelectedTime);
                }
                br.close();
            } catch (IOException e) {
                Log.d("IO file setting", "Lỗi khi ọc file setting");
            }
        } else {
            initSettingFile();
        }
    }
}