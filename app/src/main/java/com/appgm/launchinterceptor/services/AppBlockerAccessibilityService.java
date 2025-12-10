package com.appgm.launchinterceptor.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import com.appgm.launchinterceptor.utils.AppBlockerUtils;
import com.appgm.launchinterceptor.utils.PrefUtils;
import com.appgm.launchinterceptor.ui.BlockDialog;

public class AppBlockerAccessibilityService extends AccessibilityService {
    
    private static AppBlockerAccessibilityService instance;
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? event.getPackageName().toString() : null;
            
            if (packageName != null && AppBlockerUtils.shouldBlockApp(this, packageName)) {
                // 拦截应用启动
                handleAppBlocked(packageName);
            }
        }
    }
    
    private void handleAppBlocked(String packageName) {
        // 返回主屏幕
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        
        // 显示拦截提示
        showBlockDialog(packageName);
        
        // 记录拦截日志
        AppBlockerUtils.logBlockEvent(this, packageName);
    }
    
    private void showBlockDialog(String packageName) {
        Intent intent = new Intent(this, BlockDialog.class);
        intent.putExtra("package_name", packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
    @Override
    public void onInterrupt() {
        // 服务中断时的处理
    }
    
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        instance = null;
        return super.onUnbind(intent);
    }
    
    public static AppBlockerAccessibilityService getInstance() {
        return instance;
    }
    
    public static boolean isServiceRunning() {
        return instance != null;
    }
}