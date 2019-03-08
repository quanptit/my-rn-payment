package com.app.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.view.Window;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.baseLibs.utils.L;


public class ActivityBill extends Activity implements BillingProcessor.IBillingHandler {
    private String ITEM_SKU;
    private BillingProcessor bp;

    public static void openActivity(Activity activity, int requestCode, String productId, String IAP_LICENSE_KEY) {
        L.d("ActivityBill: productId = " + productId + " , IAP_LICENSE_KEY: " + IAP_LICENSE_KEY);
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
        if (!isAvailable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("\nDevice Not Support\n");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.show();
            return;
        }
        Intent intent = new Intent(activity, ActivityBill.class);
        intent.putExtra("DATA", productId);
        intent.putExtra("IAP_LICENSE_KEY", IAP_LICENSE_KEY);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        String IAP_LICENSE_KEY = getIntent().getStringExtra("IAP_LICENSE_KEY");
        bp = new BillingProcessor(this, IAP_LICENSE_KEY, this);
        setFinishOnTouchOutside(true);
        ITEM_SKU = getIntent().getStringExtra("DATA");
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Mua item, Nếu item chưa được tiêu thụ, nó sẽ vẫn còn trong listOwnedProducts.
     * Nếu item vẫn còn mà gọi mua lần nữa ==> sẽ nhảy ngay vào onProductPurchased
     */
    private void purchase(String producId) {
        L.d("ActivityBil purchase: " + producId);
        bp.purchase(this, producId);
    }

    /**
     * Tiêu thu item đã mua. return true nếu thanh công (có item để tiêu thụ)
     */
    private boolean consumePurchase(String producId) {
        boolean isSuccess = bp.consumePurchase(producId);
        L.d("ActivityBil consumePurchase: " + producId + ", isSuccess: " + isSuccess);
        return isSuccess;
    }

    //Called when BillingProcessor was initialized and it's ready to purchase
    @Override
    public void onBillingInitialized() {
        L.d("ActivityBil onBillingInitialized");
        purchase(ITEM_SKU);
    }

    //Called when requested PRODUCT ID was successfully purchased
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        L.d("ActivityBil onProductPurchased: " + productId);
        Intent intentData = new Intent();
        intentData.putExtra("DATA", productId);
        setResult(RESULT_OK, intentData);
        finish();
    }

    /**
     * Called when some error occurred.
     * Note - this includes handling the case where the user canceled the buy dialog:
     * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
     */
    @Override
    public void onBillingError(int errorCode, Throwable error) {
        L.d("ActivityBil onBillingError: " + errorCode);
        Intent intentData = new Intent();
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED)
            if (error != null)
                intentData.putExtra("DATA", error.getMessage());

        setResult(RESULT_CANCELED, intentData);
        finish();
    }

    /**
     * Called when purchase history was restored and the list of all owned PRODUCT ID's
     * was loaded from Google Play
     */
    @Override
    public void onPurchaseHistoryRestored() {
        L.d("ActivityBil onPurchaseHistoryRestored: ");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }
}
