package dev.easyplay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.List;
import java.sql.SQLException;

import dev.easyplay.data.Playlist;
import dev.easyplay.data.Song;
import dev.easyplay.data.Video;

/**
 * Created by Erwin on 14/04/2017.
 *  compile 'com.j256.ormlite:ormlite-core:4.48'
    compile 'com.j256.ormlite:ormlite-android:4.48'
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "ormlite.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Song, Integer> mSongDao = null;
    private Dao<Video, Integer> mVideoDao = null;
    private Dao<Playlist, Integer> mPlaylistDao = null;

    public DatabaseHelper(Context context) throws Exception  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
        if (mSongDao == null) {
            mSongDao = getDao(Song.class);
        }
        if (mVideoDao == null) {
            mVideoDao = getDao(Video.class);
        }
        if (mPlaylistDao == null) {
            mPlaylistDao = getDao(Playlist.class);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Song.class);
            TableUtils.createTable(connectionSource, Video.class);
            TableUtils.createTable(connectionSource, Playlist.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Song.class, true);
            TableUtils.dropTable(connectionSource, Video.class, true);
            TableUtils.dropTable(connectionSource, Playlist.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Song */

    public void addSong(Song song) throws SQLException {
        mSongDao.create(song);
    }

    public boolean isSongExist(String name) throws SQLException {
        List<Song> songs = mSongDao.queryForAll();
        for (Song song : songs) {
            if (name != null && song != null && song.mSongName != null && song.mSongName.compareTo(name) == 0)
                return true;
        }
        return false;
    }

    public void updateSong(Song song) throws SQLException {
        mSongDao.createOrUpdate(song);
    }

    public void deleteSong(Song song) throws SQLException {
        mSongDao.delete(song);
    }

    public List<Song> getAllSongs() throws SQLException {
        List<Song> songs = mSongDao.queryForAll();
        return songs;
    }

    public Song getSongById(int mSongId) throws SQLException {
        Song song = mSongDao.queryForId(mSongId);
        return song;
    }

    public Song getSongByPath(String path) throws SQLException {
        List<Song> songs = mSongDao.queryBuilder().where()
                .eq("path", path).query();
        return songs.get(0);
    }

    /* Video */

    public void addVideo(Video video) throws SQLException {
        mVideoDao.create(video);
    }

    public boolean isVideoExist(String name) throws SQLException {
        List<Video> songs = mVideoDao.queryForAll();
        for (Video song : songs) {
            if (song.mVideoName.compareTo(name) == 0)
                return true;
        }
        return false;
    }

    public void updateVideo(Video video) throws SQLException {
        mVideoDao.createOrUpdate(video);
    }

    public void deleteVideo(Video video) throws SQLException {
        mVideoDao.delete(video);
    }

    public List<Video> getAllVideos() throws SQLException {
        List<Video> videos = mVideoDao.queryForAll();
        return videos;
    }

    public Video getVideoById(int mVideoId) throws SQLException {
        Video video = mVideoDao.queryForId(mVideoId);
        return video;
    }

    public Video getVideoByPath(String path) throws SQLException {
        List<Video> videos = mVideoDao.queryBuilder().where()
                .eq("path", path).query();
        return videos.get(0);
    }

    /* Playlist */

    public void addPlaylist(Playlist playlist) throws SQLException {
        mPlaylistDao.create(playlist);
    }

    public void updatePlaylist(Playlist playlist) throws SQLException {
        mPlaylistDao.createOrUpdate(playlist);
    }

    public void deletePlaylist(Playlist playlist) throws SQLException {
        mPlaylistDao.delete(playlist);
    }

    public List<Playlist> getAllPlaylist() throws SQLException {
        List<Playlist> playlists = mPlaylistDao.queryForAll();
        return playlists;
    }

    public Playlist getPlaylistById(int mPlaylistId) throws SQLException {
        Playlist playlist = mPlaylistDao.queryForId(mPlaylistId);
        return playlist;
    }

    public Playlist getPlaylistByPath(String path) throws SQLException {
        List<Playlist> playlists = mPlaylistDao.queryBuilder().where()
                .eq("path", path).query();
        return playlists.get(0);
    }

    /***
     * mSongDao.queryBuilder()
     .where()
     .eq("name", "toto")
     .and() // :or()
     .eq("artist", "titi")
     .query(); return List<Song>
     */

    @Override
    public void close() {
        mSongDao = null;

        super.close();
    }
}