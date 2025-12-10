package com.appgm.launchinterceptor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.appgm.launchinterceptor.R;

public class BlockDialog extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 设置对话框样式
        setupDialogStyle();
        
        // 获取被拦截的应用包名
        String packageName = getIntent().getStringExtra("package_name");
        
        // 获取应用名称
        String appName = getAppName(packageName);
        
        // 创建并显示拦截对话框
        showBlockDialog(appName, packageName);
    }
    
    private void setupDialogStyle() {
        // 设置为对话框样式
        setTheme(android.R.style.Theme_Dialog);
        
        // 设置窗口参数
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
    
    private String getAppName(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }
    
    private void showBlockDialog(String appName, String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("应用已拦截");
        builder.setMessage("应用 \"" + appName + "\" 已被拦截。\n\n根据设置，此应用不允许在此设备上启动。");
        
        // 确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        
        // 设置不可取消
        builder.setCancelable(false);
        
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // 设置消息文本样式
        TextView messageView = dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextSize(16);
        }
    }
    
    @Override
    public void onBackPressed() {
        // 禁用返回键，确保用户必须点击确定按钮
        // super.onBackPressed();
    }
}