package com.example.awua;

import android.app.Application;

import java.io.File;

public class MyApplication extends Application {
    private File saveDataFile;

    public File getSaveDataFile(){
        return saveDataFile;
    }

    public void setSaveDataFile(File file){
        this.saveDataFile = file;
    }
}
