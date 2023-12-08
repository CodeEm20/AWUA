package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
    }
}