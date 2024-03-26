package com.team18.recordapp;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AudioRecorder {
    private final Context context;
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private boolean isRecording = false;
    private ArrayList<byte[]> capturedAudioData; // Store captured audio data in a temporary buffer

    public AudioRecorder(String outputFile, Context context) {
        this.outputFile = outputFile;
        this.context = context;  // Store the context for later use
    }

    public void start() {
        if (!isRecording) {
            try {
                if (mediaRecorder == null) {
                    mediaRecorder = new MediaRecorder();
                    // ... (other recorder configuration)
                }

                // Initialize capturedAudioData for storing audio chunks
                capturedAudioData = new ArrayList<>();

                mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    @Override
                    public void onError(MediaRecorder mr, int what, int extra) {
                        // Handle recording errors
                        stop(); // Stop recording in case of error
                        Toast.makeText(context, "Recording error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                // Configure MediaRecorder to capture audio data and store in capturedAudioData
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(outputFile);

                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (isRecording) {
            isRecording = false;
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void save() {
        if (capturedAudioData != null && !capturedAudioData.isEmpty()) {
            try {
                // Create the output file
                File file = new File(outputFile);
                FileOutputStream fos = new FileOutputStream(file);

                // Write the captured audio data to the file
                for (byte[] dataChunk : capturedAudioData) {
                    fos.write(dataChunk);
                }

                fos.close();

                Toast.makeText(context, "Audio recorded and saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error saving audio file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No audio data captured", Toast.LENGTH_SHORT).show();
        }
    }
}
