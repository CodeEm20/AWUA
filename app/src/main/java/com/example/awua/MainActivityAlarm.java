package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivityAlarm extends AppCompatActivity {

    private EditText txv_alarmName;
    private EditText txv_hour;
    private EditText txv_minute;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarm);
        txv_alarmName = (EditText) findViewById(R.id.txv_myAlarm);
        txv_hour = (EditText) findViewById(R.id.txv_hourValue);
        txv_minute = (EditText) findViewById(R.id.txv_minuteValue);
        txv_mon = (TextView) findViewById(R.id.txv_monTxtA);
        txv_tue = (TextView) findViewById(R.id.txv_tueTxtA);
        txv_wed = (TextView) findViewById(R.id.txv_wedTxtA);
        txv_thu = (TextView) findViewById(R.id.txv_thuTxtA);
        txv_fri = (TextView) findViewById(R.id.txv_friTxtA);
        txv_sat = (TextView) findViewById(R.id.txv_satTxtA);
        txv_sun = (TextView) findViewById(R.id.txv_sunTxtA);
        btn_save = (Button) findViewById(R.id.btnSave);

        txv_alarmName.setHint("Alarm name");
        Boolean[] alarmDays = new Boolean[7];
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
                Intent intent = new Intent(MainActivityAlarm.this, MainActivity.class);
                //Send over information to MainActivity
                intent.putExtra("aName",alarmName);
                //Save to internal storage
                File file = ((MyApplication) getApplication()).getSaveDataFile();
                try {
                    FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
                    writer.write(alarmName);
                    writer.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });

    }
}