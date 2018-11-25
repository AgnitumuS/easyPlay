package dev.easyplay.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.easyplay.R;
import dev.easyplay.data.Song;

public class ArtistItem extends LinearLayout {

    public Song song;
    public TextView textViewName;
    public TextView secondColumn;
    public TextView thirdColumn;
    public ImageView audioImage;
    Context context;
    View view;
    ArtistItem self;



    // Genre
    public ArtistItem(Context context) {
        super(context);

        this.view =  LayoutInflater.from(getContext()).inflate(
                R.layout.list_item, null);
        textViewName = (TextView) view.findViewById(R.id.firstColumn);
        secondColumn = (TextView) view.findViewById(R.id.secondColumn);
        thirdColumn = (TextView) view.findViewById(R.id.thirdColumn);
        audioImage = (ImageView) view.findViewById(R.id.audioImage);
        this.context = context;
        self = this;
        this.addView(view);

    }

    public void setSong(Song song) {
        this.song = song;
        textViewName.setText(song.mSongArtist);
        secondColumn.setText(song.mSongName);
        thirdColumn.setText(song.mSongAlbum);
        if (song.mSongImg != null) {
            audioImage.setImageBitmap(BitmapFactory.decodeByteArray(song.mSongImg, 0, song.mSongImg.length));
        } else {
            String uri = "@drawable/empty";

            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

            Drawable res = context.getResources().getDrawable(imageResource);
            audioImage.setImageDrawable(res);
        }
    }
}