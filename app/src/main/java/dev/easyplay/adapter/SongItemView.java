package dev.easyplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.CharacterObjectType;
import com.j256.ormlite.table.DatabaseTable;

import dev.easyplay.R;
import dev.easyplay.data.Song;
import dev.easyplay.data.Video;

public class SongItemView extends LinearLayout {

    public Song song;
    public Video video;
    public TextView textViewName;
    Context context;
    View view;
    boolean isSelected;
    SongItemView self;



    // Genre
    public SongItemView(Context context) {
        super(context);

        this.view =  LayoutInflater.from(getContext()).inflate(
                R.layout.song_item, null);
        textViewName = (TextView) view.findViewById(R.id.textViewSongItem);
        this.context = context;
        isSelected = false;
        self = this;
        this.addView(view);

    }

    public void setSong(Song song) {
        this.song = song;
        textViewName.setText(song.mSongName);
    }

    public void setVideo(Video video) {
        this.video = video;
        textViewName.setText(video.mVideoName);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSelected) {
            self.setBackgroundColor(Color.parseColor("#34495e"));
        }
        else {
            self.setBackgroundColor(Color.parseColor("#2c3e50"));

        }
        isSelected = !isSelected;

        return true;
    }


}