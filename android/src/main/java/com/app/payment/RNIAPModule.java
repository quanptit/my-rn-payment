package com.app.payment;

import android.app.Activity;
import android.content.Intent;

import com.baseLibs.utils.L;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNIAPModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private Promise promise;

    public RNIAPModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "IAPUtils";
    }

    @ReactMethod
    public void purchaseProduct(String productId, String IAP_LICENSE_KEY, Promise promise) {
        this.promise = promise;
        Activity activity = getCurrentActivity();
        if (activity != null) {
            ActivityBill.openActivity(activity, 186, productId, IAP_LICENSE_KEY);
        } else
            promise.reject("0", "activity null");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 186) {
                if (promise == null) {
                    L.d("ERROR: Promise null ==========");
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    promise.resolve(true);
                } else {
                    if (data != null && data.hasExtra("DATA"))
                        promise.reject("0", data.getStringExtra("DATA"));
                    else
                        promise.reject("0", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}
