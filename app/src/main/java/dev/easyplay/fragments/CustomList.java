package dev.easyplay.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.easyplay.R;
import dev.easyplay.adapter.SongItem;
import dev.easyplay.data.Song;


public class CustomList extends BaseAdapter {
    private Context mContext;
    private List<Song> files;


    public CustomList(Context c, List<Song> files) {
        mContext = c;
        this.files = files;
        Collections.sort(this.files, new Comparator<Song>()
        {
            @Override
            public int compare(Song text1, Song text2)
            {
                if (text1 != null && text2 != null && text1.mSongName != null && text2.mSongName != null) {
                    return text1.mSongName.compareToIgnoreCase(text2.mSongName);
                }
                return +1;
            }
        });
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return files.size();
    }

    @Override
    public Song getItem(int position) {
        // TODO Auto-generated method stub
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SongItem songItem;
        if (convertView == null) {
            songItem = new SongItem(mContext);
        } else {
            songItem = (SongItem) convertView;
        }

        songItem.setSong(getItem(position));

        return songItem;
    }
}
