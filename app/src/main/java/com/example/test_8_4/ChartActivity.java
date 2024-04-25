package com.example.test_8_4;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ChartActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<Object> fileList;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VRecorder");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        barChart = findViewById(R.id.barChart);
        if (barChart == null) {
            Log.e("ChartActivity", "BarChart is null");
        }

        showChart();
    }


    private void displayBarChart() {
        

        List<BarEntry> entries = getBarEntries();

        BarDataSet dataSet = new BarDataSet(entries, "Recordings");
        BarData data = new BarData(dataSet);

        if (barChart != null) {
            barChart.setData(data);
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.animateY(1000);

            barChart.invalidate();
        } else {
            Log.e("BarChart", "BarChart is null");
        }
    }

    public ArrayList<File> findFile(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files){
            if(singleFile.getName().toLowerCase().endsWith(".amr")){
                arrayList.add(singleFile);
            }
        }
        return arrayList;
    }
    private List<BarEntry> getBarEntries() {
        List<BarEntry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>(); // Danh sách ngày
        List<Integer> counts = new ArrayList<>(); // Danh sách số lượng bản ghi

        // Tạo danh sách ngày và số lượng bản ghi tương ứng
        fileList = new ArrayList<>();
        fileList.addAll(findFile(path));
        for (Object obj : fileList) {
            File file = (File) obj;
            String date = getDateFromFile(file);
            if (!dates.contains(date)) {
                dates.add(date);
                counts.add(1);
            } else {
                int index = dates.indexOf(date);
                counts.set(index, counts.get(index) + 1);
            }
        }

        // Tạo BarEntry với ngày là trục x và số lượng bản ghi là trục y
        for (int i = 0; i < dates.size(); i++) {
            entries.add(new BarEntry(i, counts.get(i)));
        }

        return entries;
    }


    private String getDateFromFile(File file) {
        String fileName = file.getName();
        String dateString = fileName.substring(10, 18);

        String formattedDate = formatDate(dateString);

        return formattedDate;
    }

    private String formatDate(String dateString) {
        // Định dạng chuỗi ngày thành định dạng phù hợp
        // Ví dụ: từ "20220420" thành "2022-04-20"
        String formattedDate = dateString.substring(0, 4) + "-" + dateString.substring(4, 6) + "-" + dateString.substring(6, 8);
        return formattedDate;
    }
    private void showChart() {

        // Hiển thị dữ liệu trên biểu đồ
        displayBarChart();
        barChart.invalidate();
    }
}
