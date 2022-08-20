package com.example.mrffilemanager;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    private static final int STORAGE_PERMISSION_CODE = 1001;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_bar);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment)
                .commit();
        navigationView.setCheckedItem(R.id.nav_home);

        if(checkPermission())
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

        }

        else
        {
            requestPermission();
        }



    }

    private void requestPermission()
    {
        if(SDK_INT>=Build.VERSION_CODES.R)
        {
            try{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri =Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);

            }
            catch (Exception e)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);

                storageActivityResultLauncher.launch(intent);
            }


        }

        else{
            ActivityCompat.requestPermissions(this,new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,STORAGE_PERMISSION_CODE);
        }
    }

    public  boolean checkPermission(){
        if(SDK_INT>=Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();

        }
        else
        {
            int write= ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            return write==PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;

        }
    }
    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(SDK_INT>=Build.VERSION_CODES.R)
            {
                if(Environment.isExternalStorageManager())
                {
                   // perform()
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }

            }

            else
            {


            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          if(requestCode==STORAGE_PERMISSION_CODE)
          {
              if(grantResults.length>0)
              {
                  boolean write = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                  boolean read = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                  
                  
                  if(write&&read)
                  {
                      ///perform
                  }
                  
                  else
                  {
                      Toast.makeText(this, "allow permission first", Toast.LENGTH_SHORT).show();
                  }
              }
          }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home: {
                HomeFragment homeFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.nav_internal: {
                InternalFragment internalFragment = new InternalFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, internalFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.nav_sd: {
                SdcardFragment sdcardFragment = new SdcardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, sdcardFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.nav_about: {
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                   break;
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();
        if(drawerLayout.isDrawerOpen((GravityCompat.START)))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();

        }

    }
}