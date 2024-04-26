package com.team18.recordapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordListAdapter extends ArrayAdapter<Record> {
    ArrayList<Record> records;
    Context context;
    int layoutResource;
    public RecordListAdapter(@NonNull Context context, int resource, ArrayList<Record> records) {
        super(context, resource, records);
        this.context = context;
        this.records = records;
        this.layoutResource = resource;
    }

    @Nullable
    @Override
    public Record getItem(int position) {
        return records.get(position);
    }

    @Override
    public int getCount() {
        return records.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        convertView = inflater.inflate(layoutResource,null);
//Hàm khởi thêm dữ lieu vào các View từ arrayList thông qua position
        TextView tvRecordName = convertView.findViewById(R.id.tv_record_name);
        tvRecordName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecordedFile(getItem(position).getFilePath());
            }
        });
        tvRecordName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        });
        tvRecordName.setText(getItem(position).getFileName());
        CheckBox chkRecordItem = convertView.findViewById(R.id.chk_record_item);

        return convertView;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            notifyDataSetChanged();
        }
    }
}
