package com.example.mapy.util.audio;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.mapy.models.PlayMedia;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

public class AudioRecording implements PlayMedia {

    public static final String TAG = "addpoi";
    private MediaRecorder mediaRecorder;
    private Context context;

    String path;

    boolean isPlaying = false,isPause=false;

    public AudioRecording(MediaRecorder mediaRecorder, Context context) {
        this.mediaRecorder = mediaRecorder;
        this.context = context;
    }


    @Override
    public int startRecording() {

        if(!isPlaying){
            // start Playing

            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            path = getPath();
            Log.d("addpoi", "Path Of Music: "+path);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.d(TAG, "Recording Turned On");
                return 1;
            } catch (Exception e) {
                Log.d(TAG, "Error with StartRecording, message: " + e.getMessage());
                e.printStackTrace();
                return -1;
            }


        }
        else{
            // Already Playing
            return 0;



        }



    }

    @Override
    public void stopRecording() {
        mediaRecorder.release();
    }

    @Override
    public void pauseplayRecording() {

            if(!isPause){
                // Pause the Music
                isPause = true;
                mediaRecorder.stop();
            }
            else{
                // start Playing
                isPause = false;
                mediaRecorder.start();
            }


    }

    @Override
    public String getPath() {


        ContextWrapper contextWrapper = new ContextWrapper(context);
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        File file = new File(music,  "srec.mp3");
        return file.getPath();

    }
}
