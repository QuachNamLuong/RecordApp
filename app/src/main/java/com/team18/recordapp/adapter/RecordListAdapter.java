package com.team18.recordapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.team18.recordapp.R;
import com.team18.recordapp.Record;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordListAdapter extends ArrayAdapter<Record> {
    ArrayList<Record> records;
    public ArrayList<Integer> recordIDs;
    Context context;
    int layoutResource;
    public RecordListAdapter(@NonNull Context context, int resource, ArrayList<Record> records, ArrayList<Integer> recordIDs) {
        super(context, resource, records);
        this.context = context;
        this.records = records;
        this.layoutResource = resource;
        this.recordIDs = recordIDs;
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

    private void sharingAudio(String sharePath) {
        File file = new File(sharePath);
        Uri uri1 = FileProvider.getUriForFile(context,
                "com.team18.recordapp.fileprovider",
                file);
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent1.setType("audio/mp3");
        intent1.putExtra(Intent.EXTRA_STREAM, uri1);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(Intent.createChooser(intent1, "Share file:"));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        convertView = inflater.inflate(layoutResource,null);
        //Hàm khởi thêm dữ lieu vào các View từ arrayList thông qua position
        TextView tvRecordName = convertView.findViewById(R.id.tv_record_name);
        ImageButton ibtnShare = convertView.findViewById(R.id.ibtn_share);
        ImageButton ibtnDelete = convertView.findViewById(R.id.ibtn_delete);

        tvRecordName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecordedFile(getItem(position).getFilePath());
            }
        });

        ibtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingAudio(getItem(position).getFilePath());
            }
        });

        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(position);
            }
        });

        tvRecordName.setText(getItem(position).getFileName());

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
            if (!records.isEmpty()) {
                records.remove(position);
            }
            notifyDataSetChanged();
        }
    }
}
