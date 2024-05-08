package com.team18.recordapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.team18.recordapp.R;
import com.team18.recordapp.Record;
import com.team18.recordapp.adapter.RecordListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListRecordFragment extends Fragment {
    private ArrayList<Record> records;
    private ArrayList<Integer> recordIDs;
    RecordListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_record, container, false);
        recordIDs = new ArrayList<>();
        // Initialize recordedFilesList and adapter
        records = new ArrayList<>();
        adapter = new RecordListAdapter(getContext(), R.layout.record_item, records, recordIDs);

        ListView listViewRecordedFiles = view.findViewById(R.id.lviewghiam);
        listViewRecordedFiles.setAdapter(adapter);

        // Load recorded files
        loadRecordedFiles();

        // Set item click listener for play and long click listener for delete confirmation

        return view;
    }

    public static ListRecordFragment newInstance() {
        ListRecordFragment fragment = new ListRecordFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecordedFiles();
    }

    private void loadRecordedFiles() {
        File internalStorageDir = getContext().getFilesDir();
        String subdirectory = "recordings"; // Example subdirectory name
        File recordingsDir = new File(internalStorageDir, subdirectory);
        recordingsDir.mkdirs();
        // Get directory path where recorded files are stored
        String directoryPath = recordingsDir.getAbsolutePath();
        Toast.makeText(getContext(), directoryPath, Toast.LENGTH_SHORT).show();

        // List recorded files in the directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            records.clear();
            for (File file : files) {
                try {
                    wait(500);
                }
                catch (Exception e) {

                }
                Record record = new Record( file.getAbsolutePath());
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

    public void deleteCheckFile() {
        for(int i: recordIDs) {
            deleteRecordedFile(i);
        }
    }

    public ArrayList<String> getCheckFilePaths() {
        ArrayList<String> listPath = new ArrayList<>();
        for(int i:recordIDs) {
            listPath.add(records.get(i).getFilePath());
        }
        return listPath;
    }
}
