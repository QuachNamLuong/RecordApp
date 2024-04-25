package com.example.test_8_4.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_8_4.Adapters.RecAdapter;
import com.example.test_8_4.ChartActivity;
import com.example.test_8_4.OnSelectListener;
import com.example.test_8_4.R;
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

public class RecordingsFragment extends Fragment implements OnSelectListener {

    private RecyclerView recyclerView;
    private List<File> fileList;
    private RecAdapter recAdapter;

    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VRecorder");
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recordings, container, false);
        recyclerView = view.findViewById(R.id.recycler_records);
        fileList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recAdapter = new RecAdapter(getContext(), fileList, this);
        recyclerView.setAdapter(recAdapter);


        displayFiles();
        return view;
    }

    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycler_records);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        fileList = new ArrayList<>();
        fileList.addAll(findFile(path));
        recAdapter = new RecAdapter(getContext(), fileList, this);
        recyclerView.setAdapter(recAdapter);

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


    @Override
    public void OnSelected(File file) {
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "audio/x-wav");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            displayFiles();
        }
    }
}
