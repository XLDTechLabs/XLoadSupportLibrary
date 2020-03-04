# XLoadSupportLibrary
> List of convenience library for android developer under XLD Development Program. to know more about [XLD development program](https://xload.io/)

## Download
Download all support library
```
Todo dependency here
```

Libraries
```
Todo chunks depedencies
```

## XLD Connection Library
> XLDConnect library is a very light library to get a `key` from xload app that will allow you to retrieve user.

##### Document
```
XLDConnect.getKey(Context context, boolean isDevelopment, XLDConnect.OnXLoadConnectionListener listener)
```
 Description:
 - Method that you can used to get the key that will allow you to get user information from xload app, contact [https://xload.io/](XLD ) to join XLD development program

 Parameters:
 * Context - the app context
 * isDevelopment - _true_ if you want to access development env. _false_ if you want to access production env.
 * XLDConnect.OnXLoadConnectionListener - success and error callback listener

 XLDConnect.OnXLoadConnectionListener - return two callback _onXLoadConnectionSuccess_ and _onXLoadConnectionError_

 * onXLoadConnectionSuccess - return the key that can used to retrieve user information
 * onXLoadConnectionError - return _enum_ if something went wrong while retrieving the key

 onXLoadConnectionError Enum:
 1. NO_READ_PERMISSION -  when user don't have read permission
 2. XLOAD_APP_NOT_LOGIN - user is not currently login
 3. XLOAD_NOT_INSTALL - if xload app is not installed
 4. UNKNOWN_ERROR - look for android studio logcat


##### Download:
```
dependency download implementation
```

##### Integration Guide:

Add to Android Manifest*
```AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" />

<uses-permission
        android:name="android.permission.READ_EXTERNAL_STORAGE"
        tools:node="replace" />
```

Note: If your app targetSdkVersion 29 (Android Q), Add `android:requestLegacyExternalStorage="true"` to your AndroidManifest file.

```AndroidManifest.xml
 <application
     android:requestLegacyExternalStorage="true"
     ...>
 ...
 </application>
```
Also don't forget to asked READ_EXTERNAL_STORAGE permission for Android M devices
```Android.java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
     if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(
               this,
               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
               REQUEST_READ_PERMISSION);
     }
}
```
##### Usage
Retrieve user information from xload app
```
  XLDConnect.getKey(this, true, new XLDConnect.OnXLoadConnectionListener() {
            @Override
            public void onXLoadConnectionError(XLDConnect.Error error) {
                // Error message
            }

            @Override
            public void onXLoadConnectionSuccess(String key) {
                // Get key to retrieve user from xload app
            }
        });
```
