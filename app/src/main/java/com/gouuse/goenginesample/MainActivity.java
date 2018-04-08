package com.gouuse.goenginesample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gouuse.goengine.permission.Action;
import com.gouuse.goengine.permission.GoPermission;
import com.gouuse.goengine.permission.Rationale;
import com.gouuse.goengine.permission.RequestExecutor;
import com.gouuse.goengine.permission.SettingService;
import com.gouuse.goenginesample.view.ImageListActivity;
import com.gouuse.goenginesample.view.UploadDownActivity;
import com.gouuse.goenginesample.view.login.LoginActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);




        GoPermission.with(this)
                .permission(Manifest.permission.CALL_PHONE)
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        executor.execute();
                    }
                })
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                        if (GoPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                            SettingService settingService = GoPermission.permissionSetting(MainActivity.this);
                            // 如果用户同意去设置：
                            settingService.execute();
                            // 如果用户不同意去设置：
                            settingService.cancel();
                        }

                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.btn_2:
                startActivity(new Intent(MainActivity.this, ImageListActivity.class));
                break;
            case R.id.btn_3:
                startActivity(new Intent(MainActivity.this, UploadDownActivity.class));
                break;
        }
    }
}
