package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivityAlarm extends AppCompatActivity {

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
        setContentView(R.layout.activity_main_alarm);
    }
}