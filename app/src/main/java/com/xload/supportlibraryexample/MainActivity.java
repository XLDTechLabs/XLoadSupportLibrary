package com.xload.supportlibraryexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.xload.app_connect.XLDConnect;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_READ_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askStoragePermission();

        XLDConnect.getKey(MainActivity.this, true, new XLDConnect.OnXLoadConnectionListener() {
            @Override
            public void onXLoadConnectionError(XLDConnect.Error error) {
                Log.d(TAG, "ERROR " + error.name());
            }

            @Override
            public void onXLoadConnectionSuccess(String key) {
                Log.d(TAG, "key = " + key);
            }
        });
    }

    private void askStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_PERMISSION);
            }
        }
    }
}
