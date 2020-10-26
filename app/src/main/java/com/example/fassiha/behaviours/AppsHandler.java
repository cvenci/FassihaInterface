package com.example.fassiha.behaviours;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class AppsHandler {

     private Context context;
     private Activity activity;
     private static Map<Integer, String>  appIdHash= new HashMap<Integer, String>();

     public AppsHandler(Context context, Activity activity){
         this.context = context;
         this.activity = activity;

         appIdHash.put(-1, "");
         appIdHash.put(1, "com.android.calendar");
         appIdHash.put(2,"com.lge.clock");
         appIdHash.put(3,"com.android.calculator2");
         appIdHash.put(4,"com.android.facebook"); // not working
         appIdHash.put(5,"com.instagram.android");
         appIdHash.put(6,"com.lge.music");

     }

     public void openApp(Integer id){
         if(id != 0){
             if(id == -1){
                 System.exit(0);
             }
         Intent intent = this.context.getPackageManager().getLaunchIntentForPackage(appIdHash.get(id));
         this.activity.startActivity(intent);
         }
     }

     public void googleSearch(String query){
         Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
         intent.putExtra(SearchManager.QUERY, query);
         this.activity.startActivity(intent);
     }
}
