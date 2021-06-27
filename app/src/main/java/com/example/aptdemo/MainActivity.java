package com.example.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.apt_annotation.SensorsDataBindView;
import com.example.apt_sdk1.SensorsDataAPI;

public class MainActivity extends AppCompatActivity {

    @SensorsDataBindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorsDataAPI.bindView(this);
        textView.setText("azy");
    }
}
