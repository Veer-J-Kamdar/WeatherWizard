package com.example.weatherwizard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class radar extends Fragment
{
    WebView wv;
    public radar()
    { /* Required empty public constructor*/ }
    /*@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_radar, container, false);

        wv=v.findViewById(R.id.wvRadar);
        wv.loadUrl("https://earth.nullschool.net");
        WebSettings ws=wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyWebViewClient());
        return  v;
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            return false;
        }
    }
}