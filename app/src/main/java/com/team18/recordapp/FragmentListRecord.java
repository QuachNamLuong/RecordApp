package com.team18.recordapp;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.media.MediaPlayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentListRecord extends Fragment {
    private ArrayList<Record> records;
    RecordListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_record, container, false);

        // Initialize recordedFilesList and adapter
        records = new ArrayList<>();
        adapter = new RecordListAdapter(getContext(), R.layout.record_item, records);

        ListView listViewRecordedFiles = view.findViewById(R.id.lviewghiam);
        listViewRecordedFiles.setAdapter(adapter);

        // Load recorded files
        loadRecordedFiles();

        // Set item click listener for play and long click listener for delete confirmation

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecordedFiles();
    }

    private void loadRecordedFiles() {
        // Get directory path where recorded files are stored
        String directoryPath = getActivity().getExternalCacheDir().getAbsolutePath();

        // List recorded files in the directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            records.clear();
            for (File file : files) {
                Record record = new Record(file.getName(), file.getAbsolutePath());
                records.add(record);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void playRecordedFile(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                // Xử lý ngoại lệ khi không thể phát nhạc
            }
        } else {
            // Xử lý khi file không tồn tại
        }
    }



    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xoá file này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Delete" button, delete the recorded file
                        deleteRecordedFile(position);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Cancel" button, dismiss the dialog
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteRecordedFile(int position) {
        String filePath = records.get(position).getFilePath();
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            // Remove the file path from the list
            records.remove(position);
            // Update the ListView
            adapter.notifyDataSetChanged();
        }
    }
}
