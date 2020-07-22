package com.liner.switchicon;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SwitchIcon switchIcon1;
    SwitchIcon switchIcon2;
    SwitchIcon switchIcon3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchIcon1 = findViewById(R.id.switch_icon1);
        switchIcon2 = findViewById(R.id.switch_icon2);
        switchIcon3 = findViewById(R.id.switch_icon3);

        switchIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchIcon1.switchState();
            }
        });
        switchIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchIcon2.switchState();
            }
        });
        switchIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchIcon3.switchState();
            }
        });
    }
}