package dev.easyplay.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.easyplay.MediaPlayerController;
import dev.easyplay.R;
import dev.easyplay.adapter.BrowserItem;
import dev.easyplay.adapter.FileArrayAdapter;
import dev.easyplay.database.DatabaseHelper;

import static android.app.Activity.RESULT_OK;

public class ExplorerFragment extends Fragment {
    ListView listViewBrowser;
    private File currentDir;
    private FileArrayAdapter adapter;
    View rootView;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            databaseHelper = new DatabaseHelper(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        rootView = inflater.inflate(R.layout.fragment_explorer, container, false);
        listViewBrowser = (ListView) rootView.findViewById(R.id.listViewBrowser);
        currentDir = new File("/");
        fill(currentDir);

        return rootView;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        List<BrowserItem> dir = new ArrayList<BrowserItem>();
        List<BrowserItem>fls = new ArrayList<BrowserItem>();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new BrowserItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                }
                else
                {
                    fls.add(new BrowserItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon", getMimeType(ff.getAbsolutePath())));
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        Log.i("Dir", currentDir.toString());
        if(!currentDir.getName().equals("/"))
            dir.add(0,new BrowserItem("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new FileArrayAdapter(getActivity(),R.layout.file_list_browser,dir);
        listViewBrowser.setAdapter(adapter);
        listViewBrowser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BrowserItem o = adapter.getItem(position);
                if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
                    if (o.getPath() != null ) {
                        currentDir = new File(o.getPath());
                        fill(currentDir);
                    }
                }
                else
                {
                    Intent intent = new Intent(getActivity(), MediaPlayerController.class);
                    intent.putExtra("GetPath",currentDir.toString());
                    intent.putExtra("GetFileName",o.getName());
                    if (o.type != null && (o.type.compareTo("video/mp4") == 0 || o.type.compareTo("video/3gp2")  == 0
                            || o.type.compareTo("video/3gpp")  == 0 || o.type.compareTo("video/avi") == 0
                            || o.type.compareTo("video/x-matroska")  == 0 || o.type.compareTo("video/webm")  == 0
                            || o.type.compareTo("video/mp2ts")  == 0)) {
                        try {
                            intent.putExtra("objectId", databaseHelper.getVideoByPath(o.getPath()).mVideoId);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("objectType", 1);
                        startActivity(intent);
                    }
                    else if (o.type != null && (o.type.compareTo("audio/mpeg3")  == 0
                        || o.type.compareTo("audio/x-mpeg-3")  == 0 || o.type.compareTo("audio/mpeg") == 0
                        || o.type.compareTo("audio/wav")  == 0 || o.type.compareTo("audio/x-wav")  == 0)) {
                        intent.putExtra("getFilePath", o.getPath());
                        try {
                            intent.putExtra("objectId", databaseHelper.getSongByPath(o.getPath()).mSongId);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("objectType", 0);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(rootView.getContext(), "Can't read this media !", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }
}
