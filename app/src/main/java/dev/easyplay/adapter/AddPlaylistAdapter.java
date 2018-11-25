package dev.easyplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.easyplay.data.Song;
import dev.easyplay.database.DatabaseHelper;

public class AddPlaylistAdapter extends BaseAdapter {
    protected List<Song> playlists;
    private Context context;
    private DatabaseHelper dbHelper;


    public AddPlaylistAdapter(Context context) {
        try {
                dbHelper = new DatabaseHelper(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.context = context;
            try {
                this.playlists = dbHelper.getAllSongs();
                /*Collections.sort(this.playlists, new Comparator<Song>()
                {
                    @Override
                    public int compare(Song text1, Song text2)
                    {
                        if (text1 != null && text2 != null && text1.mSongName != null && text2.mSongName != null) {
                            return text1.mSongName.compareToIgnoreCase(text2.mSongName);
                        }
                        return +1;
                    }
                });*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SongItemView songItemView;
        if (convertView == null) {
            songItemView = new SongItemView(context);
            songItemView.setSong(getItem(position));
        } else {
            songItemView = (SongItemView) convertView;
        }

        songItemView.setSong(getItem(position));

        return songItemView;
    }

        @Override
        public int getCount() {
            return playlists.size();
        }

        @Override
        public Song getItem(int position) {
            return playlists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }