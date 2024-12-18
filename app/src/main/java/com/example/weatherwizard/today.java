package com.example.weatherwizard;

import static android.content.Intent.getIntent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class today extends Fragment
{
    TextView tvCity,tvTemp,tvCondition;
    ImageView ivCurrentTemp;
    Button btnSearch,btnOptions,btnAirQuality;
    RecyclerView rvHourForecast;
    WebView webView;

    AdView adv;
    LocationManager locationManager;
    BottomSheetDialog sheet;
    View view;
    String cityName="";
    int PERMISSION_CODE = 1;

    ArrayList<rv_items> rv_itemsArrayList;
    rv_adapter rvAdapter;

    public today() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_today, container, false);

        tvCity=v.findViewById(R.id.tvCity);
        tvTemp=v.findViewById(R.id.tvTemp);
        tvCondition=v.findViewById(R.id.tvCondition);
        ivCurrentTemp=v.findViewById(R.id.ivCurrentTemp);
        btnSearch=v.findViewById(R.id.btnSearch);
        btnOptions=v.findViewById(R.id.btnOptions);
        btnAirQuality=v.findViewById(R.id.btnAirQuality);
        rvHourForecast=v.findViewById(R.id.rvHour);

        adv = v.findViewById(R.id.adView);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adv.loadAd(adRequest);
        sheet= new BottomSheetDialog(getContext());
        createDialog();
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheet.setContentView(view);
                sheet.show();
            }
        });
        sheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        rv_itemsArrayList=new ArrayList<>();
        rvAdapter=new rv_adapter(getContext(),rv_itemsArrayList);
        rvHourForecast.setAdapter(rvAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               Intent ob=new Intent(getContext(),search.class);
               startActivity(ob);
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //cityName=getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);

        webView=v.findViewById(R.id.wvToday);
        webView.loadUrl("https://earth.nullschool.net");
        WebSettings ws=webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWebViewClient());

        return v;
    }
    private class myWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            return false;
        }
    }

    private void createDialog()
    {
        view=getLayoutInflater().inflate(R.layout.sheet_options,null,false);
        Button btnSettings=view.findViewById(R.id.btnSettings);
        Button btnShare=view.findViewById(R.id.btnShare);
        Button btnRate=view.findViewById(R.id.btnRate);
        Button btnContact=view.findViewById(R.id.btnContact);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob=new Intent(getContext(),settings.class);
                startActivity(ob);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "5 Star", Toast.LENGTH_SHORT).show();
            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Developer is in front of you", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWeatherInfo(String cityName)
    {
        String url="https://api.weatherapi.com/v1/forecast.json?key=ba54f36c834d4904bc4155803230501&q=Vadodara&days=1&aqi=no&alerts=no";
        tvCity.setText(cityName);
        RequestQueue rq=Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                rv_itemsArrayList.clear();
                try
                {
                    String temprature=response.getJSONObject("current").getString("temp_c");
                    tvTemp.setText(temprature.concat("°C"));
                    int isDayNight=response.getJSONObject("current").getInt("is_day");
                    String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");

                    Picasso.get().load("https:".concat(conditionIcon)).into(ivCurrentTemp);
                    tvCondition.setText(condition);

                    JSONObject ob=response.getJSONObject("forecast");
                    JSONObject forecast=ob.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourly=forecast.getJSONArray("hour");

                    for(int i=0;i< hourly.length();i++)
                    {
                        JSONObject hour=hourly.getJSONObject(i);
                        String time=hour.getString("time");
                        String temp=hour.getString("temp_c");
                        String img=hour.getJSONObject("condition").getString("icon");
                        String wind=hour.getString("wind_kph");
                        rv_itemsArrayList.add(new rv_items(time,temp,img,wind));
                    }
                    rvAdapter.notifyDataSetChanged();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            Toast.makeText(getContext(), "Please enter valid city name", Toast.LENGTH_SHORT).show();
        }
    });
        rq.add(jsonObjectRequest);
    }
    public String getCityName(double longitude, double latitude)
    {
        String cityName="Not found";
        Geocoder gcd=new Geocoder(getActivity().getBaseContext(), Locale.getDefault());

        try
        {
            List<Address> addressList=gcd.getFromLocation(longitude,latitude,10);
            for(Address adr:addressList)
            {
                if(adr!=null)
                {
                    String city=adr.getLocality();
                    if(city!=null && !city.equals(""))
                    {
                        city=cityName;
                    }
                    else
                    {
                        Log.i("CityName", "City not found");
                        Toast.makeText(getContext(),"City noy found",Toast.LENGTH_SHORT).show();;
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return cityName;
    }
}