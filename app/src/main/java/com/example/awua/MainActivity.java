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
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Arrays;

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
    private TextView[] dayView;
    private TextView txv_alarmSound;
    private Button btn_newAlarm;
    private Button btn_musicFile;
    private String songName;
    private String filepath;

    public File file;
    private String alarmName;
    private boolean[] alarmDays;
    private String time;
    boolean goingToOtherActivity;
    boolean toAlarm;

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
        goingToOtherActivity = false;
        toAlarm = false;

        alarmDays =new boolean[7];
        Arrays.fill(alarmDays, false);
        dayView =new TextView[]{txv_mon, txv_tue, txv_wed, txv_thu, txv_fri, txv_sat, txv_sun};


        //Create file to save data on first open and if the file exist do loadContent()
        createFileIfNeeded();

        btn_newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //run("Python/PlaySound.py");
                Intent intent= new Intent(MainActivity.this, ActivityAlarm.class);
                goingToOtherActivity = true;
                toAlarm = true;
                startActivity(intent);

            }
        });

        btn_musicFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,ActivityMP3.class);
                goingToOtherActivity = true;
                toAlarm = false;
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

    public void loadContent() {
        byte[] content = new byte[(int) file.length()];

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(content);
            String fullMsg = new String(content);
            String[] arrOfMsg = fullMsg.split("@");
            for (String a : arrOfMsg) {
                if (a.contains("*Name:")) {
                    alarmName = a.replace("*Name:", "");
                    txv_alarm.setText(alarmName);
                } else if (a.contains("*Song:")) {
                    songName = a.replace("*Song:", "");
                    txv_alarmSound.setText(songName);
                } else if (a.contains("*Days:")) {
                    a = a.replace("*Days:%", "");
                    String[] arrOfDays = a.split("%");
                    for (String s : arrOfDays) {
                        Log.v(TAG, "At Days" + s);
                        if (s.contains("*0:")) {
                            String msg = s.replace("*0:", "");
                            alarmDays[0] = Boolean.parseBoolean(msg);
                            if (alarmDays[0]) {
                                dayView[0].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[0].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*1:")) {
                            String msg = s.replace("*1:", "");
                            alarmDays[1] = Boolean.parseBoolean(msg);
                            if (alarmDays[1]) {
                                dayView[1].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[1].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*2:")) {
                            String msg = s.replace("*2:", "");
                            alarmDays[2] = Boolean.parseBoolean(msg);
                            if (alarmDays[2]) {
                                dayView[2].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[2].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*3:")) {
                            String msg = s.replace("*3:", "");
                            alarmDays[3] = Boolean.parseBoolean(msg);
                            if (alarmDays[3]) {
                                dayView[3].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[3].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*4:")) {
                            String msg = s.replace("*4:", "");
                            alarmDays[4] = Boolean.parseBoolean(msg);
                            if (alarmDays[4]) {
                                dayView[4].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[4].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*5:")) {
                            String msg = s.replace("*5:", "");
                            alarmDays[5] = Boolean.parseBoolean(msg);
                            if (alarmDays[5]) {
                                dayView[5].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[5].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        } else if (s.contains("*6:")) {
                            String msg = s.replace("*6:", "");
                            alarmDays[6] = Boolean.parseBoolean(msg);
                            if (alarmDays[6]) {
                                dayView[6].setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                            } else {
                                dayView[6].setBackground(getResources().getDrawable(R.drawable.circle));
                            }
                        }
                    }
                } else if (a.contains("*Hour:")) {
                    String[] arrOfTime = a.split("%");
                    String minute = arrOfTime[1].replace("*Min:", "");
                    String hour = arrOfTime[0].replace("*Hour:", "");
                    Log.v(TAG, minute + hour);
                    int m = Integer.parseInt(minute);
                    int h = Integer.parseInt(hour);
                    if (h<10) {
                        char lastChar = hour.charAt(hour.length()-1);
                        hour = "0" + lastChar;
                    }
                    if(m<10){
                        char lastChar = minute.charAt(minute.length()-1);
                        minute = "0" + lastChar;
                    }
                    time = hour + ":" + minute;
                    txv_timeValue.setText(time);
                } else if(a.equals("")) {
                    Log.v(TAG, "What are you? " + a);
                }
            }
            Log.v(TAG, "Done Loading");
        } catch(Exception e){
            e.printStackTrace();
            Log.v(TAG, "Failed to loadContent");
        }

    }

    //Saves
    @Override
    protected void onStop() {
        if (!goingToOtherActivity) {
            Log.v(TAG,"Closing App");
            try {
                Writer ClearFile = new FileWriter(file, false);
                ClearFile.write("");
                ClearFile.close();
                Writer writer = new FileWriter(file, false);
                writer.write("@*Name:" + alarmName);
                writer.write("@*Song:" + songName);
                writer.write("@*Days:");
                writer.write("%*0:" + alarmDays[0]);
                writer.write("%*1:" + alarmDays[1]);
                writer.write("%*2:" + alarmDays[2]);
                writer.write("%*3:" + alarmDays[3]);
                writer.write("%*4:" + alarmDays[4]);
                writer.write("%*5:" + alarmDays[5]);
                writer.write("%*6:" + alarmDays[6]);
                String[] theTime = time.split(":");
                writer.write("@*Hour:" + theTime[0]);
                writer.write("%*Min:" + theTime[1]);
                writer.flush();
                writer.close();
                Log.v(TAG, "Done saving");
            } catch (Exception e) {
                Log.v(TAG, "Failed to save");
            }
        } else {
            if(!toAlarm){
                Log.v(TAG,"Going to set Music");
                try {
                    Writer ClearFile = new FileWriter(file, false);
                    ClearFile.write("");
                    ClearFile.close();
                    Writer writer = new FileWriter(file, false);
                    writer.write("@*Name:" + alarmName);
                    writer.write("@*Days:");
                    writer.write("%*0:" + alarmDays[0]);
                    writer.write("%*1:" + alarmDays[1]);
                    writer.write("%*2:" + alarmDays[2]);
                    writer.write("%*3:" + alarmDays[3]);
                    writer.write("%*4:" + alarmDays[4]);
                    writer.write("%*5:" + alarmDays[5]);
                    writer.write("%*6:" + alarmDays[6]);
                    String[] theTime = time.split(":");
                    writer.write("@*Hour:" + theTime[0]);
                    writer.write("%*Min:" + theTime[1]);
                    writer.flush();
                    writer.close();
                    Log.v(TAG, "Done saving");
                } catch (Exception e) {
                    Log.v(TAG, "Failed to save");
                }
            } else {
                Log.v(TAG,"Going to save Alarm");
                try {
                    Writer ClearFile = new FileWriter(file, false);
                    ClearFile.write("");
                    ClearFile.close();
                    Writer writer = new FileWriter(file, false);
                    writer.write("@*Song:" + songName);
                    writer.close();
                    Log.v(TAG, "Done saving");
                } catch (Exception e) {
                    Log.v(TAG, "Failed to save");
                }
            }
        }
        super.onStop();
    }
}