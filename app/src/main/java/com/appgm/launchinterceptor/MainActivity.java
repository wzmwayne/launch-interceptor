package com.appgm.launchinterceptor;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private TextView serviceStatusText;
    private Button enableServiceBtn;
    private Button manageAppsBtn;
    private Button settingsBtn;
    
    private static final int REQUEST_OVERLAY_PERMISSION = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 首次启动显示免责声明
        if (!PrefUtils.isDisclaimerAccepted(this)) {
            showDisclaimer();
        }
        
        initViews();
        setupClickListeners();
        updateServiceStatus();
    }
    
    private void initViews() {
        serviceStatusText = findViewById(R.id.service_status_text);
        enableServiceBtn = findViewById(R.id.btn_enable_service);
        manageAppsBtn = findViewById(R.id.btn_manage_apps);
        settingsBtn = findViewById(R.id.btn_settings);
    }
    
    private void setupClickListeners() {
        enableServiceBtn.setOnClickListener(v -> openAccessibilitySettings());
        manageAppsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppManagementActivity.class);
            startActivity(intent);
        });
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
    
    private void updateServiceStatus() {
        boolean isServiceEnabled = AccessibilityUtils.isAccessibilityServiceEnabled(this);
        
        if (isServiceEnabled) {
            serviceStatusText.setText(R.string.service_enabled);
            serviceStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            enableServiceBtn.setText(R.string.service_enabled);
            enableServiceBtn.setEnabled(false);
        } else {
            serviceStatusText.setText(R.string.service_disabled);
            serviceStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            enableServiceBtn.setText(R.string.btn_enable_service);
            enableServiceBtn.setEnabled(true);
        }
    }
    
    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "请找到并启用'应用启动拦截器'服务", Toast.LENGTH_LONG).show();
    }
    
    private void showDisclaimer() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.disclaimer_title)
                .setMessage(R.string.disclaimer_content)
                .setPositiveButton(R.string.btn_accept, (dialog, which) -> {
                    PrefUtils.setDisclaimerAccepted(this, true);
                    checkAndRequestPermissions();
                })
                .setNegativeButton(R.string.btn_decline, (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }
    
    private void checkAndRequestPermissions() {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission();
        }
    }
    
    private void requestOverlayPermission() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_overlay_title)
                .setMessage(R.string.permission_overlay_message)
                .setPositiveButton(R.string.btn_grant, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }
}