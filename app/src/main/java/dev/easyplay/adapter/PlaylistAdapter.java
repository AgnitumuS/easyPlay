package dev.easyplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.easyplay.data.Playlist;
import dev.easyplay.data.Song;
import dev.easyplay.database.DatabaseHelper;

public class PlaylistAdapter extends BaseAdapter {

        protected List<Playlist> playlists;
        private Context context;
        private DatabaseHelper dbHelper;


        public PlaylistAdapter(Context context) {
            try {
                dbHelper = new DatabaseHelper(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.context = context;
            try {
                this.playlists = dbHelper.getAllPlaylist();
 Collections.sort(this.playlists, new Comparator<Playlist>()
                {
                    @Override
                    public int compare(Playlist text1, Playlist text2)
                    {
                        if (text1 != null && text2 != null && text1.mPlaylistName != null && text2.mPlaylistName != null) {
                            return text1.mPlaylistName.compareToIgnoreCase(text2.mPlaylistName);
                        }
                        return +1;
                    }
                });

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PlaylistItemView playlistItemView;
            if (convertView == null) {
                playlistItemView = new PlaylistItemView(context);
            } else {
                playlistItemView = (PlaylistItemView) convertView;
            }

            playlistItemView.setPlaylist(getItem(position));

            return playlistItemView;
        }

@Override
        public int getCount() {
            // TODO Auto-generated method stub
            return playlists.size();
        }


        @Override
        public Playlist getItem(int position) {
            return playlists.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

    }
