package dev.easyplay;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import dev.easyplay.database.DatabaseHelper;
import dev.easyplay.fragments.AudioFragment;
import dev.easyplay.fragments.CameraFragment;
import dev.easyplay.fragments.ExplorerFragment;
import dev.easyplay.fragments.MapsFragment;
import dev.easyplay.fragments.SettingsFragment;
import dev.easyplay.fragments.VideoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RetriveMetaDataHelper retriveMetaDataHelper;
    private SharedPreferences mSharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedpreferences = this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
            }
        }

        // A remettre
        if (mSharedpreferences.getBoolean("reload", false) == false) {

            retriveMetaDataHelper = new RetriveMetaDataHelper(this);
            retriveMetaDataHelper.GetFile();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the Audio Fragment as default fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AudioFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Get access to the Fragment Manager
        FragmentManager fragmentManager = getFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_audio) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AudioFragment()).commit();
        } else if (id == R.id.menu_video) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new VideoFragment()).commit();
        } else if (id == R.id.menu_explorer) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ExplorerFragment()).commit();
        }/* else if (id == R.id.menu_camera) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CameraFragment()).commit();
        } else if (id == R.id.menu_maps) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MapsFragment()).commit();
        }*/ else if (id == R.id.menu_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
