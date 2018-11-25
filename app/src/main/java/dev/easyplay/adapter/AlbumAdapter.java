package dev.easyplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.easyplay.data.Song;

/**
 * Created by Arkane on 17/04/2017.
 */

public class AlbumAdapter extends BaseAdapter {
    private Context mContext;
    private List<Song> files;


    public AlbumAdapter(Context c, List<Song> files) {
        mContext = c;
        try {
            this.files = files;
           /* Collections.sort(this.files, new Comparator<Song>() {
                @Override
                public int compare(Song text1, Song text2) {
                    if (text1 != null && text2 != null && text1.mSongAlbum != null && text2.mSongAlbum != null) {
                        return text1.mSongAlbum.compareToIgnoreCase(text2.mSongAlbum);
                    }
                    return 100;
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        AlbumItem songItem;
        if (convertView == null) {
            songItem = new AlbumItem(mContext);
        } else {
            songItem = (AlbumItem) convertView;
        }

        songItem.setSong(getItem(position));

        return songItem;
    }
}
