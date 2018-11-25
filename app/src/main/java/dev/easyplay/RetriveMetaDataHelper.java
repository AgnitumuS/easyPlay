package dev.easyplay;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import dev.easyplay.data.Song;
import dev.easyplay.data.Video;
import dev.easyplay.database.DatabaseHelper;



// //from this u get all audio
// private void getallaudio()
// {
// String[] STAR = { "*" };
// Cursor audioCursor = ((Activity) cntx).managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, STAR, null, null, null);
// if (audioCursor != null)
// {
// if (audioCursor.moveToFirst())
// {
// do
// {
// String path = audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
// controller.audioWrapper.add(new MediaWrapper(new File(path).getName(), path, "Audio",false,color_string));
// //                    Log.i("Audio Path",path);
// }while (audioCursor.moveToNext());
// }
// //            audioCursor.close();
// }
// }
// //from this u get all video
// private void getallvideo()
// {
// String[] STAR = { "*" };
// controller.videoWrapper.clear();
// Cursor videoCursor = ((Activity) cntx).managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, STAR, null, null, null);
// if (videoCursor != null)
// {
// if (videoCursor.moveToFirst())
// {
// do
// {
// String path = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.DATA));
// controller.videoWrapper.add(new MediaWrapper(new File(path).getName(), path, "Video",false,color_string));
// //                    Log.i("Video Path",path);
// }while (videoCursor.moveToNext());
// }
// //            videoCursor.close();
// }
// }


/**
 * private SharedPreferences mSharedpreferences;
 * mSharedpreferences = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
   mSharedpreferences.edit().putBoolean("key", true).commit();
 * boolean myBool = mSharedpreferences.getBoolean("key", true); // on doit donner un default value dans le cas ou la pref nexiste pas
        */

public class RetriveMetaDataHelper {

    private String TAG = "RetriveMetaDataHelper";
    private MediaMetadataRetriever mMetaDataRetriver;
    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences mSharedpreferences;

    public RetriveMetaDataHelper(Context context) {
        mContext = context;
        try {
            mDatabaseHelper = new DatabaseHelper(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSharedpreferences = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    public void GetFile() {

            ContentResolver cr = mContext.getContentResolver();

            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cur = cr.query(uri, null, selection, null, sortOrder);
            int count = 0;
            if(cur != null) {
                count = cur.getCount();
                if(count > 0) {
                    while(cur.moveToNext()) {
                        String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                        if (data.isEmpty() == false && data != null) {
                            try {
                                Song song = getSong(data);
                                if (!mDatabaseHelper.isSongExist(song.mSongName))
                                    mDatabaseHelper.updateSong(song);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            cur.close();
            String[] STAR = { "*" };

            Cursor videoCursor = ((Activity) mContext).managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, STAR, null, null, null);
            if (videoCursor != null) {
                if (videoCursor.moveToFirst()) {
                    do {
                        String path = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        String mimetype = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                        if (mimetype.compareTo("video/mp4") == 0 || mimetype.compareTo("video/3gp2")  == 0
                                || mimetype.compareTo("video/3gpp")  == 0 || mimetype.compareTo("video/avi") == 0
                                || mimetype.compareTo("video/x-matroska")  == 0 || mimetype.compareTo("video/webm")  == 0
                                || mimetype.compareTo("video/mp2ts")  == 0) {
                            try {
                                Video video = getVideo(path);
                                if (!mDatabaseHelper.isVideoExist(video.mVideoName))
                                    mDatabaseHelper.updateVideo(video);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } while (videoCursor.moveToNext());

                }
                videoCursor.close();
            }
            mSharedpreferences.edit().putBoolean("reload", true).commit();

    }

    Song getSong(String path) {
        Song song = new Song();
        try {

            mMetaDataRetriver = new MediaMetadataRetriever();

            mMetaDataRetriver.setDataSource(path);
            song.mSongArtist = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            if (song.mSongArtist == null) {
                song.mSongArtist = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                if (song.mSongArtist == null) {
                    song.mSongArtist = "Unknown";
                }
            }
            // a voir le format de temps recuperer pour ladapter au besoin des autres activité
            //song.mSongImg = mMetaDataRetriver.getEmbeddedPicture();
            song.mSongName = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (song.mSongName == null) {
                song.mSongName = path.substring(path.lastIndexOf("/")+1);
                if (song.mSongName == null) {
                    song.mSongName = "Unknown";
                }
            }

            song.mSongAlbum = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if (song.mSongAlbum == null) {
                song.mSongAlbum = "Unknown";
            }

            song.mSongPath = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
            if (song.mSongPath == null) {
                song.mSongPath = path;
            }
            mMetaDataRetriver.release();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return (song);
    }

    Video getVideo(String path) {
        Video video = new Video();
        try {
            mMetaDataRetriver = new MediaMetadataRetriever();
            mMetaDataRetriver.setDataSource(path);
            video.mVideoArtist = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (video.mVideoArtist == null) {
                video.mVideoArtist = "Unknown";
            }
            // a voir le format de temps recuperer pour ladapter au besoin des autres activité
            video.mVideoName = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (video.mVideoName == null) {
                video.mVideoName = path.substring(path.lastIndexOf("/")+1);
                if (video.mVideoName == null) {
                    video.mVideoName = "Unknown";
                }
            }

            video.mVideoPath = mMetaDataRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
            if (video.mVideoPath == null) {
                video.mVideoPath = path;
            }
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (thumb != null) {
                thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
                video.mVideoImg = stream.toByteArray();
            }

            mMetaDataRetriver.release();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return (video);
    }
}
