package com.sarlmoclen.smockdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sarlmoclen.smock.SMockCore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SMockCore().mock();
    }
}
