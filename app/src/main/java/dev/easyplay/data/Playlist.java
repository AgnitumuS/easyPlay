package dev.easyplay.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "playlists")
public class Playlist {

    @DatabaseField(columnName = "id", generatedId = true)
    public int mSPlaylistId;

    @DatabaseField(columnName = "name")
    public String mPlaylistName;

    @DatabaseField(columnName = "songs")
    public String mSongs;

    public Playlist() {
    }
}
