package dev.easyplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import dev.easyplay.data.Song;
import dev.easyplay.data.Video;
import dev.easyplay.database.DatabaseHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MediaPlayerController extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekbar;
    private Button btn_playPause;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();

    public  int oneTimeOnly = 0;
    private TextView currentTime, maxTime;
    private float x;
    private float y;
    private float WWidth;
    private float WHeight;
    private float OrigineX;
    private float OrigineY;
    private int maxVolume = 10;
    private int lightaccurate = 10;
    private int soundaccurate = 10;
    private float actualVolume = maxVolume/2;
    private Song song;
    private Video videoItem;
    private String[]playlist;
    private float nbunituse = 0;
    private int index = 0; //Reset quand changement de playlist à gérer
    private Button btn_next;
    private Button btn_previous;
    private SurfaceView video;
    private DatabaseHelper dbHelper;
    SurfaceHolder sH;
    int objectType;
    int objectId;


    /**
     *
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_controller);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(true);
        btn_playPause = (Button) findViewById(R.id.playpause);
        currentTime = (TextView) findViewById(R.id.currentSongTime);
        maxTime = (TextView) findViewById(R.id.maxSongTime);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_next = (Button) findViewById(R.id.next);
        video = (SurfaceView) findViewById(R.id.fullscreen_content);

        try {
            dbHelper = new DatabaseHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mVisible = true;
        mControlsView = (FrameLayout) findViewById(R.id.fullscreen_content_controls);
        mContentView = (FrameLayout) findViewById(R.id.fullscreen_content_view);

        // Set up the user interaction to manually show or hide the system UI.
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        btn_playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer ==null) {
                    final String videoPath = getIntent().getExtras().getString("videoPath");
                    LaunchNewMedia(videoPath);


                    sH = video.getHolder();
                    mediaPlayer.setDisplay(sH);

                }

                if (mediaPlayer.isPlaying()) {
                    Toast.makeText(getApplicationContext(), "Stop playing sound", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    btn_playPause.setText(">");
                } else {

                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    btn_playPause.setText("| |");
                }
                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);

                    oneTimeOnly = 1;
                }

            }
        });

        String playlistName = getIntent().getExtras().getString("playlistName");
        if (playlistName == null || playlistName.length() < 1) {

            String videoName = getIntent().getExtras().getString("videoName");
            objectType = getIntent().getExtras().getInt("objectType");
            objectId = getIntent().getExtras().getInt("objectId");
            if (objectType == 0) {
                try {
                    song = dbHelper.getSongById(objectId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    videoItem = dbHelper.getVideoById(objectId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Set up the user interaction to manually show or hide the system UI.
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggle();
                }
            });

            btn_playPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mediaPlayer == null) {
                        if (objectType == 0) {
                            LaunchNewMedia(song.mSongPath);
                        }
                        else {
                            LaunchNewMedia(videoItem.mVideoPath);
                        }



                        sH = video.getHolder();
                        mediaPlayer.setDisplay(sH);

                    }

                    if (mediaPlayer.isPlaying()) {
                        Toast.makeText(getApplicationContext(), "Stop playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                        btn_playPause.setText(">");
                    } else {

                        Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                        btn_playPause.setText("| |");
                    }
                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);

                        oneTimeOnly = 1;
                    }

                }
            });
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mediaPlayer.reset();
                    mediaPlayer = null;

                    if (objectType == 0) {
                        try {
                            LaunchNewMedia(dbHelper.getSongById(objectId + 1).mSongPath);
                            objectId += 1;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            LaunchNewMedia(dbHelper.getVideoById(objectId + 1).mVideoPath);
                            objectId += 1;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                    if (objectType == 0) {
                        try {
                            LaunchNewMedia(dbHelper.getSongById(objectId - 1).mSongPath);
                            objectId -= 1;
                        } catch (SQLException e) {
                            try {
                                LaunchNewMedia(dbHelper.getSongById(1).mSongPath);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            LaunchNewMedia(dbHelper.getVideoById(objectId - 1).mVideoPath);
                            objectId -= 1;
                        } catch (SQLException e) {
                            try {
                                LaunchNewMedia(dbHelper.getVideoById(1).mVideoPath);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }
                }

            });
        } else {
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggle();
                }
            });
            String songs = getIntent().getExtras().getString("songs");
            playlist = songs.split(";");
            System.out.println(playlist);
            btn_playPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mediaPlayer == null) {
                        LaunchNewMedia(playlist[0]);
                        sH = video.getHolder();
                        mediaPlayer.setDisplay(sH);
                    }

                    if (mediaPlayer.isPlaying()) {
                        Toast.makeText(getApplicationContext(), "Stop playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                        btn_playPause.setText(">");
                    } else {
                        Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                        btn_playPause.setText("| |");
                    }
                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);

                        oneTimeOnly = 1;
                    }

                }
            });
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    // Si Playlist != null
                    mediaPlayer.reset();
                    mediaPlayer = null;
                    index++;
                    if (index > playlist.length - 1){
                        index = 0;
                    }
                    LaunchNewMedia(playlist[index]);
                }
            });


            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                    index--;
                    if (index < 0){
                        index = playlist.length - 1;
                    }
                    LaunchNewMedia(playlist[index]);
                }

            });
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar arg0) {
                int maxmusicTime = mediaPlayer.getDuration();
                int maxseekbar = seekbar.getMax();
                int newprogress_music = (maxmusicTime / maxseekbar) * seekbar.getProgress();
                mediaPlayer.pause();
                mediaPlayer.seekTo(newprogress_music);
                mediaPlayer.start();
            }
            public void onStartTrackingTouch(SeekBar arg0) {
            }
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            }
        });



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


        btn_playPause.setOnTouchListener(mDelayHideTouchListener);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(MediaPlayerController.this, MainActivity.class);
        startActivity(myIntent);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            currentTime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime))));
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };


    private Boolean LaunchNewMedia(String path){
        boolean playing = false;
        boolean isInit = false;
        if (mediaPlayer != null){
            playing = mediaPlayer.isPlaying();
        }
        if (oneTimeOnly == 0) {
            Uri uripath = Uri.parse(path);
            mediaPlayer = MediaPlayer.create(this, uripath);
            isInit = true;
        }else{
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
            isInit = true;

        }
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        maxTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) (int)finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) (int)finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime))));
        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
        currentTime.setText("0:0");
        mediaPlayer.setVolume(actualVolume/10, actualVolume/10);
        oneTimeOnly = 0;
        if (playing || isInit){
            mediaPlayer.start();
        }
        return true;
    }

}
