package com.example.weatherwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class settings extends AppCompatActivity
{

    Button btnGeneral, btnNotification, btnUnits, btnCustomization;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnGeneral=findViewById(R.id.btnGeneral);
        btnNotification=findViewById(R.id.btnNotification);
        btnUnits=findViewById(R.id.btnUnits);
        btnCustomization=findViewById(R.id.btnCustomization);
    }
}