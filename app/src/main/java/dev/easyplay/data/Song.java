package dev.easyplay.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "songs"/*, daoClass = CustomDao.class*/)
public class Song {

    @DatabaseField(columnName = "id", generatedId = true)
    public int mSongId;

    @DatabaseField(columnName = "name")
    public String mSongName;

    @DatabaseField(columnName = "artist")
    public String mSongArtist;

    @DatabaseField(columnName = "duration")
    public double mSongDuration;

    @DatabaseField(columnName = "path")
    public String mSongPath;

    @DatabaseField(columnName = "album")
    public String mSongAlbum;

    @DatabaseField(columnName = "imgtbl", dataType = DataType.BYTE_ARRAY)
    public byte[] mSongImg;



    // Genre

    public Song() {
    }
}
