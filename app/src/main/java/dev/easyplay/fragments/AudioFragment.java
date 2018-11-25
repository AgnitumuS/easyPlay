package dev.easyplay.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ServiceCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.easyplay.AddPlaylistActivity;
import dev.easyplay.MainActivity;
import dev.easyplay.MediaPlayerController;
import dev.easyplay.R;
import dev.easyplay.adapter.AlbumAdapter;
import dev.easyplay.adapter.ArtistAdapter;
import dev.easyplay.adapter.PlaylistAdapter;
import dev.easyplay.data.Song;
import dev.easyplay.data.Video;
import dev.easyplay.database.DatabaseHelper;

import static dev.easyplay.R.id.buttonAlbum;

public class AudioFragment extends Fragment {

    Button buttonPlaylist;
    Button buttonArtist;
    Button buttonSong;
    Button buttonAlbum;
    Button buttonAddPlaylist;
    Button buttonDeletePlaylist;
    PlaylistAdapter playlistAdapter;
    boolean[] listSelected;
    List<Song> files = new ArrayList<>();
    AlbumAdapter albumAdapter;
    CustomList adapter;
    ArtistAdapter artistAdapter;

    LinearLayout scrollViewPlaylist;
    LinearLayout scrollViewArtist;
    LinearLayout scrollViewSong;
    LinearLayout scrollViewAlbum;

    ListView listViewPlaylists;
    ListView grid;
    ListView  listViewArtist;
    ListView listViewAlbum;

    DatabaseHelper databaseHelper;
    private SharedPreferences mSharedpreferences;

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            try {
            databaseHelper = new DatabaseHelper(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }


           // fragmentManager = getActivity().getFragmentManager();
        View rootView = inflater.inflate(R.layout.fragment_audio, container, false);

        buttonPlaylist = (Button) (rootView.findViewById(R.id.buttonPlaylist));
        buttonArtist = (Button) (rootView.findViewById(R.id.buttonArtist));
        buttonSong = (Button) (rootView.findViewById(R.id.buttonSong));
        buttonAlbum = (Button) (rootView.findViewById(R.id.buttonAlbum));


        scrollViewPlaylist = (LinearLayout) (rootView.findViewById(R.id.scrollViewPlaylist));
        scrollViewArtist = (LinearLayout) (rootView.findViewById(R.id.scrollViewArtist));
        scrollViewAlbum = (LinearLayout) (rootView.findViewById(R.id.scrollViewAlbum));
        scrollViewSong = (LinearLayout) (rootView.findViewById(R.id.scrollViewSong));

        buttonArtist.setBackgroundColor(Color.DKGRAY);
        buttonArtist.setTextColor(getResources().getColor(R.color.colorAccent));
        scrollViewAlbum.setVisibility(View.GONE);
        scrollViewSong.setVisibility(View.GONE);
        scrollViewPlaylist.setVisibility(View.GONE);
        grid = (ListView) (rootView.findViewById(R.id.grid_viewSong));
        listViewArtist = (ListView) (rootView.findViewById(R.id.grid_viewArtist));
        listViewAlbum = (ListView) (rootView.findViewById(R.id.grid_viewAlbum));

        // Inflate CustomGrid with grid_item layout and content

        try {
            adapter = new CustomList(getActivity(), databaseHelper.getAllSongs());
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent myIntent = new Intent(getActivity(), MediaPlayerController.class);
                    myIntent.putExtra("videoPath", adapter.getItem(position).mSongPath);
                    myIntent.putExtra("videoName", adapter.getItem(position).mSongName);
                    myIntent.putExtra("objectId", adapter.getItem(position).mSongId);
                    myIntent.putExtra("objectType", 0);
                    startActivity(myIntent);
                }
            });
            albumAdapter = new AlbumAdapter(getActivity(), databaseHelper.getAllSongs());
            artistAdapter = new ArtistAdapter(getActivity(), databaseHelper.getAllSongs());
            listViewArtist.setAdapter(artistAdapter);
            listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent myIntent = new Intent(getActivity(), MediaPlayerController.class);
                    myIntent.putExtra("videoPath", artistAdapter.getItem(position).mSongPath);
                    myIntent.putExtra("videoName", artistAdapter.getItem(position).mSongName);
                    myIntent.putExtra("objectId", adapter.getItem(position).mSongId);
                    myIntent.putExtra("objectType", 0);
                    startActivity(myIntent);
                }
            });
            listViewAlbum.setAdapter(albumAdapter);
            listViewAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent myIntent = new Intent(getActivity(), MediaPlayerController.class);
                    myIntent.putExtra("videoPath", albumAdapter.getItem(position).mSongPath);
                    myIntent.putExtra("videoName", albumAdapter.getItem(position).mSongName);
                    myIntent.putExtra("objectId", adapter.getItem(position).mSongId);
                    myIntent.putExtra("objectType", 0);
                    startActivity(myIntent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



        /* A revoir pour le Highlight du bouton des top menu */
        mSharedpreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        buttonAddPlaylist = (Button) (rootView.findViewById(R.id.buttonAddPlaylist));
        buttonDeletePlaylist = (Button) (rootView.findViewById(R.id.buttonDeletePlaylist));
        listViewPlaylists = (ListView) (rootView.findViewById(R.id.listViewPlaylists));
        playlistAdapter = new PlaylistAdapter(getActivity());
        listViewPlaylists.setAdapter(playlistAdapter);
        listSelected = new boolean[playlistAdapter.getCount()];

        buttonArtist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonArtist.setBackgroundColor(Color.DKGRAY);
                buttonArtist.setTextColor(getResources().getColor(R.color.colorAccent));

                buttonAlbum.setBackgroundResource(R.color.colorPrimaryDark);
                buttonAlbum.setTextColor(Color.WHITE);
                buttonSong.setBackgroundResource(R.color.colorPrimaryDark);
                buttonSong.setTextColor(Color.WHITE);
                buttonPlaylist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonPlaylist.setTextColor(Color.WHITE);


                scrollViewPlaylist.setVisibility(View.GONE);
                scrollViewSong.setVisibility(View.GONE);
                scrollViewAlbum.setVisibility(View.GONE);
                scrollViewArtist.setVisibility(View.VISIBLE);
            }
        });

        buttonPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonPlaylist.setBackgroundColor(Color.DKGRAY);
                buttonPlaylist.setTextColor(getResources().getColor(R.color.colorAccent));

                buttonArtist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonArtist.setTextColor(Color.WHITE);
                buttonAlbum.setBackgroundResource(R.color.colorPrimaryDark);
                buttonAlbum.setTextColor(Color.WHITE);
                buttonSong.setBackgroundResource(R.color.colorPrimaryDark);
                buttonSong.setTextColor(Color.WHITE);



                scrollViewArtist.setVisibility(View.GONE);
                scrollViewAlbum.setVisibility(View.GONE);
                scrollViewSong.setVisibility(View.GONE);
                scrollViewPlaylist.setVisibility(View.VISIBLE);
            }
        });

        buttonAlbum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonAlbum.setBackgroundColor(Color.DKGRAY);
                buttonAlbum.setTextColor(getResources().getColor(R.color.colorAccent));

                buttonArtist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonArtist.setTextColor(Color.WHITE);
                buttonPlaylist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonPlaylist.setTextColor(Color.WHITE);
                buttonSong.setBackgroundResource(R.color.colorPrimaryDark);
                buttonSong.setTextColor(Color.WHITE);

                scrollViewSong.setVisibility(View.GONE);
                scrollViewPlaylist.setVisibility(View.GONE);
                scrollViewArtist.setVisibility(View.GONE);
                scrollViewAlbum.setVisibility(View.VISIBLE);
            }
        });

        buttonSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSong.setBackgroundColor(Color.DKGRAY);
                buttonSong.setTextColor(getResources().getColor(R.color.colorAccent));

                buttonArtist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonArtist.setTextColor(Color.WHITE);
                buttonAlbum.setBackgroundResource(R.color.colorPrimaryDark);
                buttonAlbum.setTextColor(Color.WHITE);
                buttonPlaylist.setBackgroundResource(R.color.colorPrimaryDark);
                buttonPlaylist.setTextColor(Color.WHITE);

                scrollViewArtist.setVisibility(View.GONE);
                scrollViewPlaylist.setVisibility(View.GONE);
                scrollViewAlbum.setVisibility(View.GONE);
                scrollViewSong.setVisibility(View.VISIBLE);
            }
        });

        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                listSelected[position] = true;
            }
        });

        listViewPlaylists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), MediaPlayerController.class);
                myIntent.putExtra("playlistName", playlistAdapter.getItem(position).mPlaylistName);
                myIntent.putExtra("songs", playlistAdapter.getItem(position).mSongs);
                startActivity(myIntent);
                return false;
            }
        });

        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlaylistActivity.class);
                startActivity(intent);
            }
        });

        buttonDeletePlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < playlistAdapter.getCount(); i++) {
                    try {
                        if (listSelected[i]) {
                            databaseHelper.deletePlaylist(playlistAdapter.getItem(i));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                playlistAdapter = new PlaylistAdapter(getActivity());
                listViewPlaylists.setAdapter(playlistAdapter);
                listSelected = new boolean[playlistAdapter.getCount()];
            }
        });

        if (mSharedpreferences.getBoolean("artist", true) == false) {
            buttonArtist.setVisibility(View.GONE);
            scrollViewArtist.setVisibility(View.GONE);
        }
        if (mSharedpreferences.getBoolean("album", true) == false) {
            buttonAlbum.setVisibility(View.GONE);
            scrollViewAlbum.setVisibility(View.GONE);
        }
        if (mSharedpreferences.getBoolean("song", true) == false) {
            buttonSong.setVisibility(View.GONE);
            scrollViewSong.setVisibility(View.GONE);
        }
        if (mSharedpreferences.getBoolean("playlist", true) == false) {
            buttonPlaylist.setVisibility(View.GONE);
            scrollViewPlaylist.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        playlistAdapter = new PlaylistAdapter(getActivity());
        listViewPlaylists.setAdapter(playlistAdapter);
        listSelected = new boolean[playlistAdapter.getCount()];
    }
}
