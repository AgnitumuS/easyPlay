package dev.easyplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dev.easyplay.R;
import dev.easyplay.data.Playlist;
import dev.easyplay.data.Song;
import dev.easyplay.data.Video;

public class PlaylistItemView extends LinearLayout {

    public Playlist playlist;
    public TextView textViewName;
    public TextView textViewNbSong;
    Context context;
    View view;
    boolean isSelected;
    PlaylistItemView self;



    // Genre
    public PlaylistItemView(Context context) {
        super(context);

        this.view =  LayoutInflater.from(getContext()).inflate(
                R.layout.playlist_item, null);
        textViewName = (TextView) view.findViewById(R.id.textViewPlaylistItem);
        textViewNbSong = (TextView) view.findViewById(R.id.textViewNbSongItem);
        this.context = context;
        isSelected = false;
        self = this;
        this.addView(view);

    }

    private String getNbSong() {
        int count = 0;

        for (int i = 0; i < playlist.mSongs.length(); i++) {
            if (playlist.mSongs.charAt(i) == ';') {
                count++;
            }
        }
        return (Integer.toString(count));
    }


    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        textViewName.setText(playlist.mPlaylistName);
        textViewNbSong.setText(getNbSong() + "songs");
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