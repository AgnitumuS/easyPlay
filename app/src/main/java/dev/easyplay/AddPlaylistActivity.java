package dev.easyplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLException;

import dev.easyplay.adapter.AddPlaylistAdapter;
import dev.easyplay.data.Playlist;
import dev.easyplay.database.DatabaseHelper;

public class AddPlaylistActivity extends AppCompatActivity {

    ListView listViewPlaylistsAdd;
    Button buttonAddSave;
    AddPlaylistAdapter playlistAdapter;
    EditText editTextName;
    boolean[] listSelected;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_playlist_activity);
        try {
            databaseHelper = new DatabaseHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listViewPlaylistsAdd = (ListView) findViewById(R.id.listViewPlaylistsAdd);
        buttonAddSave = (Button) findViewById(R.id.buttonAddSave);
        editTextName = (EditText) findViewById(R.id.editTextName);
        playlistAdapter = new AddPlaylistAdapter(this);
        listViewPlaylistsAdd.setAdapter(playlistAdapter);
        listSelected = new boolean[playlistAdapter.getCount()];
        listViewPlaylistsAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                if (listSelected[position]) {
                    listSelected[position] = false;
                }
                else {
                    listSelected[position] = true;
                }
            }
        });
        buttonAddSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Playlist playlist = new Playlist();
                playlist.mPlaylistName = editTextName.getText().toString();
                if (playlist.mPlaylistName == null || playlist.mPlaylistName.isEmpty()) {
                    playlist.mPlaylistName = "Unknown";
                }
                String songs = "";
                for (int i = 0; i < playlistAdapter.getCount(); i++) {
                    if (listSelected[i] == true) {
                        songs += (playlistAdapter.getItem(i).mSongPath + ";");
                    }
                }
                playlist.mSongs = songs;
                try {
                    databaseHelper.addPlaylist(playlist);
               } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
