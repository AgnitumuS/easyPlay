package dev.easyplay.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.easyplay.MediaPlayerController;
import dev.easyplay.R;
import dev.easyplay.data.Video;
import dev.easyplay.database.DatabaseHelper;

public class VideoFragment extends Fragment {

    private Context mContext;
    View rootView;
    GridView grid;



    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate view and get context
        rootView = inflater.inflate(R.layout.fragment_video, container, false);
        mContext = getActivity();
        List<Video> files = null;

        try {
            databaseHelper = new DatabaseHelper(getActivity());
            files = databaseHelper.getAllVideos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inflate CustomGrid with grid_item layout and content
        CustomGrid adapter = new CustomGrid(mContext, files);
        grid = (GridView) rootView.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);

        // TODO: - Launch video on click
        final List<Video> finalFiles = files;
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO - Display video
                Intent myIntent = new Intent(mContext, MediaPlayerController.class);
                myIntent.putExtra("objectId", finalFiles.get(position).mVideoId);
                myIntent.putExtra("objectType", 1);
                startActivity(myIntent);
            }
        });

        return rootView;
    }
}