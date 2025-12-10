package com.appgm.launchinterceptor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AppBlockerUtils {
    private static final String TAG = "AppBlockerUtils";
    private static final String PREFS_NAME = "app_blocker_prefs";
    private static final String BLOCKED_APPS_KEY = "blocked_apps";
    private static final String LOG_KEY = "block_log";
    
    // 默认拦截的应用列表
    private static final Set<String> DEFAULT_BLOCKED_APPS = new HashSet<String>() {{
        add("com.tencent.mm"); // 微信
        add("com.tencent.mobileqq"); // QQ
        add("com.taobao.taobao"); // 淘宝
        add("com.jingdong.app.mall"); // 京东
        add("com.tmall.wireless"); // 天猫
        add("com.ss.android.ugc.aweme"); // 抖音
        add("com.smile.gifmaker"); // 快手
        add("com.tencent.wework"); // 企业微信
        add("com.alibaba.android.rimet"); // 钉钉
    }};
    
    public static boolean shouldBlockApp(Context context, String packageName) {
        Set<String> blockedApps = getBlockedApps(context);
        return blockedApps.contains(packageName);
    }
    
    public static Set<String> getBlockedApps(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> blockedApps = prefs.getStringSet(BLOCKED_APPS_KEY, new HashSet<String>());
        
        // 如果没有设置拦截列表，使用默认列表
        if (blockedApps.isEmpty()) {
            blockedApps = DEFAULT_BLOCKED_APPS;
            saveBlockedApps(context, blockedApps);
        }
        
        return blockedApps;
    }
    
    public static void saveBlockedApps(Context context, Set<String> blockedApps) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(BLOCKED_APPS_KEY, blockedApps);
        editor.apply();
    }
    
    public static void addBlockedApp(Context context, String packageName) {
        Set<String> blockedApps = getBlockedApps(context);
        blockedApps.add(packageName);
        saveBlockedApps(context, blockedApps);
    }
    
    public static void removeBlockedApp(Context context, String packageName) {
        Set<String> blockedApps = getBlockedApps(context);
        blockedApps.remove(packageName);
        saveBlockedApps(context, blockedApps);
    }
    
    public static void logBlockEvent(Context context, String packageName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String existingLog = prefs.getString(LOG_KEY, "");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        String logEntry = timestamp + " - 拦截应用: " + packageName + "\n";
        
        String newLog = logEntry + existingLog;
        
        // 保留最近100条记录
        String[] lines = newLog.split("\n");
        StringBuilder trimmedLog = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length, 100); i++) {
            if (i > 0) trimmedLog.append("\n");
            trimmedLog.append(lines[i]);
        }
        
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LOG_KEY, trimmedLog.toString());
        editor.apply();
        
        Log.i(TAG, "应用被拦截: " + packageName + " at " + timestamp);
    }
    
    public static String getBlockLog(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(LOG_KEY, "暂无拦截记录");
    }
    
    public static void clearBlockLog(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LOG_KEY, "");
        editor.apply();
    }
}