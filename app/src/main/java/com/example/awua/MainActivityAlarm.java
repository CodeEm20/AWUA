package com.example.awua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivityAlarm extends AppCompatActivity {

    private EditText txv_alarmName;
    private EditText txv_hour;
    private TextView txv_minute;
    private TextView txv_mon;
    private TextView txv_tue;
    private TextView txv_wed;
    private TextView txv_thu;
    private TextView txv_fri;
    private TextView txv_sat;
    private TextView txv_sun;
    private Button btn_save;
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
        txv_alarmName = (EditText) findViewById(R.id.txv_myAlarm);
        txv_hour = (EditText) findViewById(R.id.txv_hourValue);
        txv_minute = (TextView) findViewById(R.id.txv_minuteValue);
        txv_mon = (TextView) findViewById(R.id.txv_monTxtA);
        txv_tue = (TextView) findViewById(R.id.txv_tueTxtA);
        txv_wed = (TextView) findViewById(R.id.txv_wedTxtA);
        txv_thu = (TextView) findViewById(R.id.txv_thuTxtA);
        txv_fri = (TextView) findViewById(R.id.txv_friTxtA);
        txv_sat = (TextView) findViewById(R.id.txv_satTxtA);
        txv_sun = (TextView) findViewById(R.id.txv_sunTxtA);
        btn_save = (Button) findViewById(R.id.btnSave);

        txv_alarmName.setHint("Alarm name");



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = txv_alarmName.getText().toString();
                Intent intent = new Intent(MainActivityAlarm.this, MainActivity.class);
                intent.putExtra("aName",alarmName);
                startActivity(intent);
            }
        });

    }
}