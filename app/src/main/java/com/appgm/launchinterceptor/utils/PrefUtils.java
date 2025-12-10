package com.appgm.launchinterceptor.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    private static final String PREFS_NAME = "app_launcher_prefs";
    
    public static final String DISCLAIMER_ACCEPTED_KEY = "disclaimer_accepted";
    public static final String SERVICE_ENABLED_KEY = "service_enabled";
    public static final String FIRST_LAUNCH_KEY = "first_launch";
    
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static boolean isDisclaimerAccepted(Context context) {
        return getSharedPreferences(context).getBoolean(DISCLAIMER_ACCEPTED_KEY, false);
    }
    
    public static void setDisclaimerAccepted(Context context, boolean accepted) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(DISCLAIMER_ACCEPTED_KEY, accepted);
        editor.apply();
    }
    
    public static boolean isServiceEnabled(Context context) {
        return getSharedPreferences(context).getBoolean(SERVICE_ENABLED_KEY, false);
    }
    
    public static void setServiceEnabled(Context context, boolean enabled) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SERVICE_ENABLED_KEY, enabled);
        editor.apply();
    }
    
    public static boolean isFirstLaunch(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_LAUNCH_KEY, true);
    }
    
    public static void setFirstLaunch(Context context, boolean firstLaunch) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FIRST_LAUNCH_KEY, firstLaunch);
        editor.apply();
    }
}