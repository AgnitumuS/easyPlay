package dev.easyplay.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import dev.easyplay.R;
import dev.easyplay.RetriveMetaDataHelper;

public class SettingsFragment extends Fragment {
    private CheckBox checkBoxArtist;
    private CheckBox checkBoxPlaylist;
    private CheckBox checkBoxSong;
    private CheckBox checkBoxAlbum;
    private Button buttonReload;
    private SharedPreferences mSharedpreferences;
    private RetriveMetaDataHelper mRetriveMetaDataHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mSharedpreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        mRetriveMetaDataHelper = new RetriveMetaDataHelper(getActivity());
        buttonReload = (Button) (rootView.findViewById(R.id.buttonReload));
        buttonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mRetriveMetaDataHelper.GetFile();
            }
        });
        checkBoxArtist = (CheckBox) (rootView.findViewById(R.id.checkBoxArtist));
        checkBoxArtist.setChecked(mSharedpreferences.getBoolean("artist", true));
        checkBoxArtist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedpreferences.edit().putBoolean("artist", b).commit();
            }
        });
        checkBoxPlaylist = (CheckBox) (rootView.findViewById(R.id.checkBoxPlaylist));
        checkBoxPlaylist.setChecked(mSharedpreferences.getBoolean("playlist", true));
        checkBoxPlaylist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedpreferences.edit().putBoolean("playlist", b).commit();
            }
        });
        checkBoxSong = (CheckBox) (rootView.findViewById(R.id.checkBoxSong));
        checkBoxSong.setChecked(mSharedpreferences.getBoolean("song", true));
        checkBoxSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedpreferences.edit().putBoolean("song", b).commit();
            }
        });
        checkBoxAlbum = (CheckBox) (rootView.findViewById(R.id.checkBoxAlbum));
        checkBoxAlbum.setChecked(mSharedpreferences.getBoolean("album", true));
        checkBoxAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedpreferences.edit().putBoolean("album", b).commit();
            }
        });
        return rootView;
    }
}
