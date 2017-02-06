package grodrich.grc.familyapp.view.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.UUID;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.view.fragments.FamilyCreationFragment;
import grodrich.grc.familyapp.view.fragments.HomeFragment;
import grodrich.grc.familyapp.view.fragments.MyFamilyFragment;
import grodrich.grc.familyapp.view.fragments.OptionsFragment;

/**
 * Created by gabri on 30/08/16.
 */
public class NavigationActivity extends OptionsActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        getViewsByXML();
        listeners();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        putFragment(new HomeFragment(), navigationView.getMenu().findItem(R.id.home_section));
    }

    @Override
    protected void getViewsByXML() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        loadImages();
        toolbar = (Toolbar)findViewById(R.id.appbar);
    }

    private void loadImages(){
        ctrl.getUserImage().addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((ImageView)navigationView.getHeaderView(0).findViewById(R.id.profileImage)).setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if (ctrl.getActualUser().hasFamily() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            ctrl.getFamilyImage().addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                    ((RelativeLayout)navigationView.getHeaderView(0).findViewById(R.id.header_layout)).setBackground(d);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    @Override
    protected void listeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                itemSelected(item);
                return false;
            }
        });
    }

    private void itemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home_section:
                fragment = new HomeFragment();
                break;
            case R.id.family_section:
                if (ctrl.getActualUser().hasFamily()){
                    fragment = new MyFamilyFragment();
                }else{
                    fragment = new FamilyCreationFragment();
                }

                break;
            case R.id.tasks_section:
                break;
            case R.id.settings_option:
                break;
            case R.id.about_option:
                break;
        }
        putFragment(fragment, item);
        drawerLayout.closeDrawers();
    }

    private void putFragment(Fragment fragment, MenuItem item){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            getSupportActionBar().setTitle(item.getTitle());
            item.setChecked(true);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
