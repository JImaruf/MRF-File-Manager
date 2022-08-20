package com.example.mrffilemanager;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InternalFragment extends Fragment implements OnFileSelectedListener {

    List<File> fileList;
    File storage;
    ImageView img_back;
    TextView tv_pathHolder;
    RecyclerView recyclerView;
    FileAdapter fileAdapter;
   static final int PERMISSION_REQUEST_CODE=1212;
   String data;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_internal, container, false);


        img_back = v.findViewById(R.id.img_back);
        tv_pathHolder = v.findViewById(R.id.tv_pathFolder);
        recyclerView=v.findViewById(R.id.recycler_internal);

        String internalStorage = System.getenv("EXTERNAL_STORAGE");

        storage = new File(internalStorage);

        try {
            data=getArguments().getString("path");
            File file = new File(data);
            storage=file;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        tv_pathHolder.setText(storage.getAbsolutePath());
        disPlayFiles();





        //




        return v;
    }



    public ArrayList<File> findFiles(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();


        for(File singleFile: files)
        {
            if(singleFile.isDirectory()&&!singleFile.isHidden())
            {
                arrayList.add(singleFile);
            }
        }

        for(File singleFile: files)
        {
            if(singleFile.getName().toLowerCase().endsWith(".jpeg") ||
                    singleFile.getName().toLowerCase().endsWith("jpg") ||
                    singleFile.getName().toLowerCase().endsWith(".png") ||
                    singleFile.getName().toLowerCase().endsWith(".mp3") ||
                    singleFile.getName().toLowerCase().endsWith(".wav") ||
                    singleFile.getName().toLowerCase().endsWith(".mp4") ||
                    singleFile.getName().toLowerCase().endsWith(".pdf") ||
                    singleFile.getName().toLowerCase().endsWith(".doc") ||
                    singleFile.getName().toLowerCase().endsWith(".apk"))
            {
                arrayList.add(singleFile);
            }
        }
        return  arrayList;
    }

    private void disPlayFiles() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(storage));

        fileAdapter = new FileAdapter(getContext(),fileList,this);
        recyclerView.setAdapter(fileAdapter) ;



    }

    @Override
    public void onFileClicked(File file) {
        if(file.isDirectory()){
            Bundle bundle = new Bundle();
            bundle.putString("path",file.getAbsolutePath());
            InternalFragment internalFragment = new InternalFragment();
            internalFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,internalFragment).addToBackStack(null).commit();

        }
        else
        {
            try {
                FileOpener.openFile(getContext(),file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFileLongClick(File file) {

    }
}