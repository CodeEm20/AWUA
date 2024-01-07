package com.example.awua;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainActivityMP3 extends AppCompatActivity {

    private static final String TAG = "MainActivityMP3";
    ActivityResultLauncher<Intent> activityResultLauncher;
    String[] permission ={READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    private ListView listView;
    ProgressBar progressBar;
    private Uri soundUri;
    private String URL;
    private String songName;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Music");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ArrayList<String> mp3file=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mp3);
        listView = (ListView) findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        //Feedback depending on results
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (Environment.isExternalStorageManager()) {
                        Log.v(TAG, "Permission Granted");
                    } else {
                        Log.v(TAG, "Permission Denied");
                    }
                }
            }
        });

        //Check if the app have permission for files
        if (checkPermission()) {
            File directory = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("Music")));
            //Create ArrayList of files that only accepts files that end with .mp3
            File[] mp3filesList = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".mp3");
                }
            });
            assert mp3filesList != null;
            for (File f : mp3filesList) {
                mp3file.add(f.getName());
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mp3file);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    File toUpload = mp3filesList[i];
                    songName = (String) listView.getItemAtPosition(i);

                    //Gets the uri for the mp3 file abd uploads it to firebase as well as sends you back to MainActivity
                    uploadToFirebase(toUpload);

                }
            });
        } else {
            //If no permission for files then request it
            requestPermission();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkPermission()) {
            File directory = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("Music")));
            //Create ArrayList of files that only accepts files that end with .mp3
            File[] mp3filesList = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".mp3");
                }
            });
            assert mp3filesList != null;
            for (File f : mp3filesList) {
                mp3file.add(f.getName());
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mp3file);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    File toUpload = mp3filesList[i];
                    songName = (String) listView.getItemAtPosition(i);

                    //Gets the uri for the mp3 file abd uploads it to firebase as well as sends you back to MainActivity
                    uploadToFirebase(toUpload);

                }
            });
        } else {
            //Go back to MainActivity
            Intent intent=new Intent(MainActivityMP3.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void uploadToFirebase(File toUpload) {
        soundUri = Uri.fromFile(toUpload);
        final StorageReference soundReference = storageReference.child("music/" + soundUri.getLastPathSegment());
        soundReference.putFile(soundUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                soundReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DataClass dataClass = new DataClass(uri.toString(), songName);
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(dataClass);
                        progressBar.setVisibility(View.INVISIBLE);

                        //Get URL to send to RaspberryPi
                        URL = dataClass.getSoundURL();
                        Log.v(TAG,URL);

                        //Go back to MainActivity
                        Intent intent=new Intent(MainActivityMP3.this,MainActivity.class);

                        songName = toUpload.getName().toString();
                        Log.v(TAG,songName);

                        //Save to internal storage
                        File file = new File(getApplicationContext().getFilesDir(), "alarm.txt");
                        try {
                            FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
                            writer.write("@*Song:" + songName);
                            writer.close();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.v(TAG, "Download Fail");
            }
        });
    }
    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
    void requestPermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext(), getPackageName())));
            activityResultLauncher.launch(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            activityResultLauncher.launch(intent);
        }
    }
    boolean checkPermission() {
        return Environment.isExternalStorageManager();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 30) {
            if (grantResults.length > 0) {
                boolean readPer = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writePer = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (readPer && writePer) {
                    Log.v(TAG,"Permission Granted");
                } else {
                    Log.v(TAG,"Permission Denied");
                }
            } else {
                Log.v(TAG,"Permission Denied");
            }
        }
    }
}