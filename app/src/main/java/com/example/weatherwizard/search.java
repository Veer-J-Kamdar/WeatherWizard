package com.example.weatherwizard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class search extends AppCompatActivity
{
    TextInputLayout edtSearch;
    Button btnLoc;
    LocationManager locationManager;
    String cityName;
    int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        edtSearch=findViewById(R.id.edtSearch);
        btnLoc=findViewById(R.id.btnLoc);

        cityName=edtSearch.getEditText().getText().toString();

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent ob=new Intent(search.this,today.class);
                startActivity(ob);
                Toast.makeText(search.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}