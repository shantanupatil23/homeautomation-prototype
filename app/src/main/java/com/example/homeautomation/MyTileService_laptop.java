//package com.example.homeautomation;
//
//import android.graphics.drawable.Icon;
//import android.service.quicksettings.TileService;
//import android.webkit.WebView;
//
//public class MyTileService_laptop extends TileService {
//
//    private final int STATE_ON = 1;
//    private final int STATE_OFF = 0;
//    private int toggleState = STATE_ON;
//
//    int url = 100;
//
//    @Override
//    public void onClick() {
//        Icon icon;
//        if(toggleState == STATE_ON){
//            toggleState = STATE_OFF;
//            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_laptop_chromebook_white_36dp);
//            goto_url(1);
//        } else {
//            toggleState = STATE_ON;
//            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_laptop_chromebook_white_36dp);
//            goto_url(0);
//        }
//        getQsTile().setIcon(icon);
//        getQsTile().updateTile();
//    }
//
//    public void goto_url(int status){
//        //WebView myWebView = (WebView) findViewById(R.id.webview);
//        //myWebView.loadUrl( "http://192.168.0." + url + "/" + status + "2");
//    }
//}