package dev.easyplay.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "videos")
public class Video {
    @DatabaseField(columnName = "id", generatedId = true)
    public int mVideoId;

    @DatabaseField(columnName = "name")
    public String mVideoName;

    @DatabaseField(columnName = "artist")
    public String mVideoArtist;

    @DatabaseField(columnName = "duration")
    public double mVideoDuration;

    @DatabaseField(columnName = "path")
    public String mVideoPath;

    @DatabaseField(columnName = "imgtbl", dataType = DataType.BYTE_ARRAY)
    public byte[] mVideoImg;

    public Video() {
    }
}
