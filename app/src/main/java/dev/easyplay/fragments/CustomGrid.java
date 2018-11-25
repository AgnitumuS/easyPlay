package dev.easyplay.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.easyplay.R;
import dev.easyplay.data.Video;

/**
 * Created by Arkane on 03/04/2017.
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private List<Video> files;


    public CustomGrid(Context c, List<Video> files) {
        mContext = c;
        this.files = files;
//        Collections.sort(this.files, new Comparator<Video>()
//        {
//            @Override
//            public int compare(Video text1, Video text2)
//            {
//                if (text1 != null && text2 != null && text1.mVideoName != null && text2.mVideoName != null) {
//                    return text1.mVideoName.compareToIgnoreCase(text2.mVideoName);
//                }
//                return +1;
//            }
//        });
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(files.get(position).mVideoName);
            textView.setBackgroundColor(Color.parseColor("#34495e"));

            if (files.get(position).mVideoImg != null) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(files.get(position).mVideoImg, 0, files.get(position).mVideoImg.length));
            } else {
                String uri = "@drawable/empty";

                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

                Drawable res = mContext.getResources().getDrawable(imageResource);
                imageView.setImageDrawable(res);
            }
        } else {
            grid = convertView;
        }

        return grid;
    }
}
