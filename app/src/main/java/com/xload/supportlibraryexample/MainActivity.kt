package com.xload.supportlibraryexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.xload.app_connect.XLDConnect

import io.xload.appconnect_ktx.XLDConnectKtx
import io.xload.appconnect_ktx.model.Error

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askStoragePermission()

        XLDConnect.getKey(this@MainActivity, true, object : XLDConnect.OnXLoadConnectionListener {
            override fun onXLoadConnectionError(error: XLDConnect.Error) {
                Log.d(TAG, "ERROR " + error.name)
            }

            override fun onXLoadConnectionSuccess(key: String) {
                Log.d(TAG, "key = $key")
            }
        })

        XLDConnectKtx.getKey(
                this@MainActivity,
                true,
                object : XLDConnectKtx.ConnectionListener {
                    override fun onXLoadConnectionError(error: Error) {
                        Log.d(TAG, "ERROR " + error.name)
                    }

                    override fun onXLoadConnectionSuccess(key: String) {
                        Log.d(TAG, "key = $key")
                    }

                }
        )

    }

    private fun askStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_PERMISSION)
            }
        }
    }

    companion object {

        val TAG = MainActivity::class.simpleName
        private val REQUEST_READ_PERMISSION = 1
    }
}
