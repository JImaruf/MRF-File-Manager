package com.example.mrffilemanager;

import java.io.File;

public interface OnFileSelectedListener {

    void onFileClicked(File file);
    void onFileLongClick(File file);
}
