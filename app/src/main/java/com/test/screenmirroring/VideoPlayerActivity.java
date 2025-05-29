package com.test.screenmirroring;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.video_view);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Example: Play a sample video from the internet
        // In a real app, you would likely use an Intent to pick a local video file
        // For demonstration, we'll use a sample video URL.
        // Replace with actual video handling logic (e.g., file picker)
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"; // Sample video
        try {
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Autoplay when prepared
                    videoView.start();
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(VideoPlayerActivity.this, "Error playing video", Toast.LENGTH_SHORT).show();
                    return true; // Indicates the error was handled
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error setting video source", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // You might want to add logic here to select a local video file instead
        // Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);

    }

    // Handle the result from the file picker if implemented
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     super.onActivityResult(requestCode, resultCode, data);
    //     if (requestCode == REQUEST_CODE_PICK_VIDEO && resultCode == RESULT_OK && data != null) {
    //         Uri selectedVideoUri = data.getData();
    //         if (selectedVideoUri != null) {
    //             videoView.setVideoURI(selectedVideoUri);
    //             videoView.start();
    //         }
    //     }
    // }
}

