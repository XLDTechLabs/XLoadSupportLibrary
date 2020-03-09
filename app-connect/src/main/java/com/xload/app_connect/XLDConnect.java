package com.xload.app_connect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.xload.app_connect.constant.Constants;
import com.xload.app_connect.models.Generated;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class XLDConnect {

    private static final String TAG = XLDConnect.class.getSimpleName();

    public enum Error {
        NO_READ_PERMISSION,
        XLOAD_APP_NOT_LOGIN,
        XLOAD_NOT_INSTALL,
        UNKNOWN_ERROR
    }

    public static void getKey(
            Activity activity,
            boolean isDevelopment,
            OnXLoadConnectionListener listener
            ) {

        if (!hasReadPermission(activity)) {
            listener.onXLoadConnectionError(Error.NO_READ_PERMISSION);
        } else if (!isXLoadAppInstall(activity, isDevelopment)) {
            listener.onXLoadConnectionError(Error.XLOAD_NOT_INSTALL);
        } else {
            File folder = new File(Environment.getExternalStorageDirectory(), Constants.FOLDER_NAME);
            StringBuilder key = new StringBuilder();

            if (folder.exists()) {
                File file = new File(folder, Constants.FILE_NAME);

                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        key.append(line);
                    }

                    Gson gson = new Gson();
                    Generated generated = gson.fromJson(key.toString(), Generated.class);

                    listener.onXLoadConnectionSuccess(generated.getGenerated());
                } catch (FileNotFoundException ignore) {
                    listener.onXLoadConnectionError(Error.XLOAD_APP_NOT_LOGIN);
                } catch (IOException error) {
                    Log.d(TAG, "ERROR: " + error.getMessage());
                    listener.onXLoadConnectionError(Error.UNKNOWN_ERROR);
                }
            }
        }
    }

    private static boolean hasReadPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private static boolean isXLoadAppInstall(Context context, boolean isDevelopment) {
        if (isDevelopment) {
            return isXLoadDevInstall(context);
        } else {
            return isXLoadAppProdInstall(context);
        }
    }

    private static boolean isXLoadAppProdInstall(Context context) {
        return isPackageInstalled(Constants.XLD_PRODUCTION_PACKAGE, context.getPackageManager());
    }

    private static boolean isXLoadDevInstall(Context context) {
        return isPackageInstalled(Constants.XLD_DEVELOPMENT_PACKAGE, context.getPackageManager());
    }

    private static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException ignore) {
            return false;
        }
    }

    public interface OnXLoadConnectionListener {
        void onXLoadConnectionError(Error error);

        void onXLoadConnectionSuccess(String key);
    }

}
