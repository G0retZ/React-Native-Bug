package com.awesomeproject;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = "NativeEmitter")
class NativeEmitter extends ReactContextBaseJavaModule {

    private boolean active;

    NativeEmitter(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return "NativeEmitter";
    }

    @Override
    public void initialize() {
        active = true;
    }

    @Override
    public void onCatalystInstanceDestroy() {
        active = false;
    }

    @ReactMethod
    public void emit(ReadableMap payload) {
        System.out.println("NativeEmitter Got payload: " + payload.toString());
        if (active) {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.startActivity(new Intent(currentActivity, SecondActivity.class));
            }
        }
    }
}
