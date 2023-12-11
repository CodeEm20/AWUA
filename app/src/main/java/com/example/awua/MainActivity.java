package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class MainActivity extends AppCompatActivity {

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
        btn_newAlarm = (Button) findViewById(R.id.btnNew);
        btn_musicFile = (Button) findViewById(R.id.btnFile);

        btn_musicFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run("Python/PlaySound.py");
// below you write code to change switch status and action to take
            }
        });
    }

    public void run (String command) {
        String hostname = "raspberrypi";
        String username = "pi";
        String password = "raspberry";
        try
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection conn = new Connection(hostname); //init connection
            conn.connect(); //start connection to the hostname
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//reads text
            while (true){
                String line = br.readLine(); // read line
                if (line == null)
                    break;
                System.out.println(line);
            }
            /* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e)
        { e.printStackTrace(System.err);
            System.exit(2); }
    }

}