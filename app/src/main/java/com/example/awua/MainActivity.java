package com.example.awua;

import static java.text.DateFormat.DEFAULT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private TextView txv_alarm;
    private TextView txv_timeValue;
    private TextView txv_mon;
    private TextView txv_tue;
    private TextView txv_wed;
    private TextView txv_thu;
    private TextView txv_fri;
    private TextView txv_sat;
    private TextView txv_sun;
    private TextView txv_alarmSound;
    private Button btn_newAlarm;
    private Button btn_musicFile;

    private String songName;

    private String filepath;
    public File file;

    private String alarmName;
    private boolean monday;
    private boolean tisdag;
    private boolean wedsday;
    private boolean torsdag;
    private boolean fredag;
    private boolean saterday;
    private boolean sunday;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txv_alarm = (TextView) findViewById(R.id.txv_alarmTxt);
        txv_timeValue = (TextView) findViewById(R.id.txv_timeValue);
        txv_mon = (TextView) findViewById(R.id.txv_monTxt);
        txv_tue = (TextView) findViewById(R.id.txv_tueTxt);
        txv_wed = (TextView) findViewById(R.id.txv_wedTxt);
        txv_thu = (TextView) findViewById(R.id.txv_thuTxt);
        txv_fri = (TextView) findViewById(R.id.txv_friTxt);
        txv_sat = (TextView) findViewById(R.id.txv_satTxt);
        txv_sun = (TextView) findViewById(R.id.txv_sunTxt);
        txv_alarmSound = (TextView) findViewById(R.id.txv_alarmSound);
        txv_alarmSound.setMovementMethod(new ScrollingMovementMethod());
        btn_newAlarm = (Button) findViewById(R.id.btnNew);
        btn_musicFile = (Button) findViewById(R.id.btnFile);


        //Create file to save data on first open and if the file exist do loadContent()
        createFileIfNeeded();

        alarmName = txv_alarm.getText().toString();
        songName = txv_alarmSound.getText().toString();

        btn_newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //run("Python/PlaySound.py");
                Intent intent= new Intent(MainActivity.this, MainActivityAlarm.class);
                startActivity(intent);
            }
        });

        btn_musicFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,MainActivityMP3.class);
                startActivity(intent);
            }
        });

    }

    private void createFileIfNeeded() {
        filepath = getApplicationContext().getFilesDir().getAbsolutePath();
        file = new File(filepath + "/alarm.txt");
        if(!file.exists()){
            try {
                if (file.createNewFile()) {
                    ((MyApplication) getApplication()).setSaveDataFile(file);
                    Log.v(TAG,"File created");
                }else {
                    Log.v(TAG,"Failed to create file");
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Log.v(TAG,"File already exist");
            loadContent();
        }
    }

    public void loadContent(){
        byte[] content = new byte[(int) file.length()];

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(content);

            alarmName = new String(content);
            txv_alarm.setText(alarmName);

            songName = new String(content);
            txv_alarmSound.setText(songName);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(alarmName);
            writer.write(songName);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onDestroy();
    }

    public void run (String command) {
        String hostname = "raspberrypi";
        String username = "pi";
        String password = "raspberry";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection conn = new Connection(hostname); //Init connection
            
            conn.connect(); //Start connection to the hostname

            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            //Reads text
            while (true){
                String line = br.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }

            //Show exit status, if available (otherwise "null")
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e){
            e.printStackTrace(System.err);
            //System.exit(2);
            Log.v("Pi","No connection");
        }
    }



}