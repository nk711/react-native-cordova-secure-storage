package com.example.reactnativecordovasecurestorage;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import android.os.Build;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Method;
import java.util.Hashtable;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.crypto.Cipher;

public class SecureStorage extends ReactContextBaseJavaModule {
    private static final String TAG = "SecureStorage";

    private static final String SERVICE = "store";

    private static final boolean SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    private static final String MSG_NOT_SUPPORTED = "API 20 (Android 5.0 Lollipop) is required. This device is running API " + Build.VERSION.SDK_INT;

    private Hashtable<String, SharedPreferencesHandler> SERVICE_STORAGE = new Hashtable<String, SharedPreferencesHandler>();
   
    ReactApplicationContext context; 

    SecureStorage(ReactApplicationContext context) {
        super(context);
        this.context = context;
    }
    
    @Override
    public String getName() {
        return "SecureStorage";
    }

    @ReactMethod
    public boolean get(String key, Promise promise) throws JSONException {
        if (!SUPPORTED) {
            Log.w(TAG,MSG_NOT_SUPPORTED);
            promise.reject(MSG_NOT_SUPPORTED);
            return false;
        }
        String alias = service2alias(SERVICE);
        SharedPreferencesHandler storage = new SharedPreferencesHandler(alias, this.context);  
        String value = storage.fetch(key);
        if (value != null) {
            JSONObject json = new JSONObject(value);
            final byte[] encKey = Base64.decode(json.getString("key"), Base64.DEFAULT);
            JSONObject data = json.getJSONObject("value");
            final byte[] ct = Base64.decode(data.getString("ct"), Base64.DEFAULT);
            final byte[] iv = Base64.decode(data.getString("iv"), Base64.DEFAULT);
            final byte[] adata = Base64.decode(data.getString("adata"), Base64.DEFAULT);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        byte[] decryptedKey = RSA.decrypt(encKey, service2alias(SERVICE));
                        String decrypted = new String(AES.decrypt(ct, decryptedKey, iv, adata));
                        promise.resolve(decrypted);
                    } catch (Exception e) {
                        Log.e(TAG, "Decrypt failed :", e);
                        promise.reject(e.getMessage());
                    }
                }
            }).start();
        } else {
            promise.reject("Key [" + key + "] not found.");
        }
        return true;
    }

     @ReactMethod
    public boolean remove(String key, Promise promise) throws JSONException {
        if (!SUPPORTED) {
            Log.w(TAG,MSG_NOT_SUPPORTED);
            promise.reject(MSG_NOT_SUPPORTED);
            return false;
        }
        String alias = service2alias(SERVICE); 
        SharedPreferencesHandler storage = new SharedPreferencesHandler(alias, this.context);  
        storage.remove(key);
        return true;
    }

    @ReactMethod
    public boolean keys(Promise promise) throws JSONException {
        if (!SUPPORTED) {
            Log.w(TAG,MSG_NOT_SUPPORTED);
            promise.reject(MSG_NOT_SUPPORTED);
            return false;
        }
        String alias = service2alias(SERVICE); 
        SharedPreferencesHandler storage = new SharedPreferencesHandler(alias, this.context);  
        JSONArray arr = new JSONArray(storage.keys());
        promise.resolve(arr.toString());
        return true;
    }


    @ReactMethod
    public boolean clear(Promise promise) throws JSONException {
        if (!SUPPORTED) {
            Log.w(TAG,MSG_NOT_SUPPORTED);
            promise.reject(MSG_NOT_SUPPORTED);
            return false;
        }
        String alias = service2alias(SERVICE); 
        SharedPreferencesHandler storage = new SharedPreferencesHandler(alias, this.context);  
        storage.clear();
        return true;
    }

    @ReactMethod
    public boolean set(String key, String value, Promise promise) throws JSONException {
        if (!SUPPORTED) {
            Log.w(TAG,MSG_NOT_SUPPORTED);
            promise.reject(MSG_NOT_SUPPORTED);
            return false;
        }
        String adata = SERVICE;
        String alias = service2alias(SERVICE); 
        SharedPreferencesHandler storage = new SharedPreferencesHandler(alias, this.context); 
        new Thread(new Runnable() {
                public void run() {
                    try {
                        JSONObject result = AES.encrypt(value.getBytes(), adata.getBytes());     // A
                        byte[] aes_key = Base64.decode(result.getString("key"), Base64.DEFAULT);
                        byte[] aes_key_enc = RSA.encrypt(aes_key, alias);
                        result.put("key", Base64.encodeToString(aes_key_enc, Base64.DEFAULT));
                        storage.store(key, result.toString());
                        promise.resolve(key);
                    } catch (Exception e) {
                        Log.e(TAG, "Decrypt failed :", e);
                        promise.reject(e.getMessage());
                    }
                }
            }).start();
        return true;
    }

    private String service2alias(String service) {
        String res = this.context.getPackageName() + "." + service;
        return  res;
    }

    private SharedPreferencesHandler getStorage(String service) {
        return SERVICE_STORAGE.get(service);
    }



} 