package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class MainActivityAlarm extends AppCompatActivity {

    private EditText txv_alarmName;
    private TimePicker timePicker;
    private TextView txv_mon;
    private TextView txv_tue;
    private TextView txv_wed;
    private TextView txv_thu;
    private TextView txv_fri;
    private TextView txv_sat;
    private TextView txv_sun;
    private Button btn_save;
    private int hour;
    private int min;
    private final String TAG = "MainActivityAlarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarm);
        txv_alarmName = (EditText) findViewById(R.id.txv_myAlarm);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        txv_mon = (TextView) findViewById(R.id.txv_monTxtA);
        txv_tue = (TextView) findViewById(R.id.txv_tueTxtA);
        txv_wed = (TextView) findViewById(R.id.txv_wedTxtA);
        txv_thu = (TextView) findViewById(R.id.txv_thuTxtA);
        txv_fri = (TextView) findViewById(R.id.txv_friTxtA);
        txv_sat = (TextView) findViewById(R.id.txv_satTxtA);
        txv_sun = (TextView) findViewById(R.id.txv_sunTxtA);
        btn_save = (Button) findViewById(R.id.btnSave);

        timePicker.setIs24HourView(true);
        txv_alarmName.setHint("Alarm name");
        boolean[] alarmDays = new boolean[7];
        Arrays.fill(alarmDays, false);

        txv_mon.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_mon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[0]){
                    txv_mon.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[0] = true;
                } else {
                    txv_mon.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[0] = false;
                }
            }
        });

        txv_tue.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_tue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[1]){
                    txv_tue.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[1] = true;
                } else {
                    txv_tue.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[1] = false;
                }
            }
        });

        txv_wed.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_wed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[2]){
                    txv_wed.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[2] = true;
                } else {
                    txv_mon.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[2] = false;
                }
            }
        });

        txv_thu.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_thu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[3]){
                    txv_thu.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[3] = true;
                } else {
                    txv_thu.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[3] = false;
                }
            }
        });

        txv_fri.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_fri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[4]){
                    txv_fri.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[4] = true;
                } else {
                    txv_fri.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[4] = false;
                }
            }
        });

        txv_sat.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_sat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[5]){
                    txv_sat.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[5] = true;
                } else {
                    txv_sat.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[5] = false;
                }
            }
        });

        txv_sun.setBackground(getResources().getDrawable(R.drawable.circle));
        txv_sun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!alarmDays[6]){
                    txv_sun.setBackground(getResources().getDrawable(R.drawable.circle_clicked));
                    alarmDays[6] = true;
                } else {
                    txv_sun.setBackground(getResources().getDrawable(R.drawable.circle));
                    alarmDays[6] = false;
                }
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmName = txv_alarmName.getText().toString();
                hour = timePicker.getHour();
                min = timePicker.getMinute();
                Intent intent = new Intent(MainActivityAlarm.this, MainActivity.class);
                //Send over information to MainActivity
                intent.putExtra("aName",alarmName);
                //Save to internal storage
                File file =new File(getApplicationContext().getFilesDir(), "alarm.txt");
                try {
                    FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
                    writer.write("@*Name:" + alarmName);
                    writer.write("@*Days:");
                    writer.write("%*0:" + alarmDays[0]);
                    writer.write("%*1:" + alarmDays[1]);
                    writer.write("%*2:" + alarmDays[2]);
                    writer.write("%*3:" + alarmDays[3]);
                    writer.write("%*4:" + alarmDays[4]);
                    writer.write("%*5:" + alarmDays[5]);
                    writer.write("%*6:" + alarmDays[6]);
                    writer.write("@*Hour:" + hour);
                    writer.write("%*Min:" + min);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);

                Log.v(TAG, "Run command on Pi");
                //Schedule job to run on specific time using cron jobs
                String days = "";
                if (alarmDays[6]) days = days + "0,";
                if (alarmDays[0]) days = days + "1,";
                if (alarmDays[1]) days = days + "2,";
                if (alarmDays[2]) days = days + "3,";
                if (alarmDays[3]) days = days + "4,";
                if (alarmDays[4]) days = days + "5,";
                if (alarmDays[5]) days = days + "6,";

                days = days.substring(0, days.length() - 1);

                run("(echo \"" + min + " " + hour + " * * " + days + " python Python/PlaySound.py\") | crontab -");
                //run("python Python/PlaySound.py");
                Log.v(TAG, "Done running command");
            }
        });

    }

    public void run (String command) {

        //IP is hardcoded to my home network and will require change on different networks
        String hostname = "10.0.0.63";
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
        catch (Exception e){
            e.printStackTrace(System.err);
            Log.v("Pi","No connection");
        }
    }
}