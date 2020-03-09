package io.xload.appconnect_ktx

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.nfc.Tag
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.xload.appconnect_ktx.constant.AppConstant
import io.xload.appconnect_ktx.model.Error
import io.xload.appconnect_ktx.model.Generated
import java.io.*
import java.lang.StringBuilder

class XLDConnectKtx {

    companion object {

        private val TAG = XLDConnectKtx::class.java.simpleName

        fun getKey(
                activity: Activity,
                isDevelopment: Boolean,
                listener: ConnectionListener) {

            if (!hasReadPermission(activity)) {
                listener.onXLoadConnectionError(Error.NO_READ_PERMISSION)
            } else if (!isXLoadAppInstall(activity, isDevelopment)) {
                listener.onXLoadConnectionError(Error.XLOAD_NOT_INSTALL)
            } else {
                val folder = File(Environment.getExternalStorageDirectory(), AppConstant.FOLDER_NAME)

                if (folder.exists()) {
                    val file = File(folder, AppConstant.FILE_NAME)

                    try {
                        val result = FileInputStream(file).bufferedReader().use { it.readLine() }

                        val gson = Gson()
                        val key = gson.fromJson(result.toString(), Generated::class.java)
                        listener.onXLoadConnectionSuccess(encrypt(key.generated))
                    } catch (ignore: FileNotFoundException) {
                        listener.onXLoadConnectionError(Error.XLOAD_APP_NOT_LOGIN)
                    } catch (error: IOException) {
                        Log.d(TAG, "Error ${error.message}")
                        listener.onXLoadConnectionError(Error.UNKNOWN_ERROR)
                    }
                }
            }
        }

        fun hasReadPermission(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }

            return true
        }

        fun isXLoadAppInstall(
                context: Context,
                isDevelopment: Boolean): Boolean {
            return if (isDevelopment) {
                isXLoadAppDevelopementInstalled(context)
            } else {
                isXLoadAppProductionInstalled(context)
            }
        }

        fun isXLoadAppDevelopementInstalled(context: Context): Boolean {
            return isPackageInstalled(
                    AppConstant.XLD_DEVELOPMENT_PACKAGE,
                    context.packageManager
            )
        }

        fun isXLoadAppProductionInstalled(context: Context): Boolean {
            return isPackageInstalled(
                    AppConstant.XLD_PRODUCTION_PACKAGE,
                    context.packageManager
            )
        }

        fun isPackageInstalled(
                packageName: String,
                packageManager: PackageManager): Boolean {
            try {
                packageManager.getPackageInfo(packageName, 0)
                return true
            } catch (ignore: PackageManager.NameNotFoundException) {
                Log.d("DEBUG", " ${ignore.message}")
                return false
            }
        }

        private fun encrypt(deviceId: String): String {
            val encrypt1 = deviceId.indexOf('_', ignoreCase = true)
            val encrypt2 = deviceId.indexOf('_', encrypt1 + 2, ignoreCase = true)

            return deviceId.substring(encrypt1 + 1, encrypt2)
        }
    }

    public interface ConnectionListener {
        fun onXLoadConnectionError(error: Error)
        fun onXLoadConnectionSuccess(key: String)
    }
}