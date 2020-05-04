//package com.example.homeautomation;
//
//import android.graphics.drawable.Icon;
//import android.os.BatteryManager;
//import android.service.quicksettings.TileService;
//import android.webkit.WebView;
//
//public class MyTileService_phone extends TileService {
//
//    private final int STATE_ON = 1;
//    private final int STATE_OFF = 0;
//    private int toggleState = STATE_ON;
//
//    boolean change1;
//    int auto1Beginning = 20, auto1Ending = 90, url = 100;
//
//    @Override
//    public void onClick() {
//        Icon icon;
//        if(toggleState == STATE_ON){
//            toggleState = STATE_OFF;
//            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_cellphone_android_white_48dp);
//            goto_url(1);
//        } else {
//            toggleState = STATE_ON;
//            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_cellphone_android_white_48dp);
//            autoPhoneOn();
//        }
//        getQsTile().setIcon(icon);
//        getQsTile().updateTile();
//    }
//
//    public void goto_url(int status){
//        //WebView myWebView = (WebView) findViewById(R.id.webview);
//        //myWebView.loadUrl( "http://192.168.0." + url + "/" + status + "1");
//    }
//
//    public void autoPhoneOn(){
//        int i = 0;
//        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
//        int batLevel1 = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        if(batLevel1<=auto1Ending){
//            goto_url(0);
//            change1 = true;
//        }
//        else{
//            goto_url(1);
//            change1 = false;
//        }
//        while (i==0) {
//            if(batLevel1<=auto1Beginning){
//                if(!change1){
//                    goto_url(0);
//                    change1 = true;
//                }
//            }
//            else if(batLevel1>=auto1Ending){
//                if(change1){
//                    goto_url(1);
//                    change1 = false;
//                }
//            }
//        }
//    }
//}