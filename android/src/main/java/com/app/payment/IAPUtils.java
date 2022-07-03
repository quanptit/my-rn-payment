package com.app.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.baseLibs.utils.L;
import com.facebook.common.internal.ImmutableList;

import java.util.List;

public class IAPUtils implements PurchasesUpdatedListener {
    private Activity activity;
    private IABCallback iabCallback;
    private BillingClient billingClient;

    // Đơn user mua, phải được xác nhận lại rằng đã kích hoạt tính năng.
    private void handlePurchase(Purchase purchase) {
        L.d("handlePurchase");
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }

    // Được gọi khi có bất cứ thay đổi nào về item đang mua.
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        L.d("onPurchasesUpdated: " + billingResult.getDebugMessage());
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            iabCallback.success();
            return;
        }

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
            iabCallback.success();
        }else
            iabCallback.error(billingResult.getDebugMessage());
    }

    public IAPUtils(Activity context, IABCallback iabCallback) {
        this.activity = context;
        this.iabCallback = iabCallback;
        billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build();
    }

    // Sau khi kết nối thành công, lấy được Product muốn mua ==> Launch the billing flow
    private void getProductDetailSuccess(ProductDetails productDetails) {
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                                // for a list of offers that are available to the user
//                               .setOfferToken(selectedOfferToken)
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();
        billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    // Function được gọi khi BillingClient đã tạo và kết nối thành công. Lúc này thực hiện các bước tiếp theo
    private void onBillingSetupSuccess(final String producId) {
        // The BillingClient is ready. You can query purchases here.
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId(producId)
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build()))
                        .build();
// Lấy danh sách mặt hàng đã khai báo trên Playstore. <ứng với producId truyền vào>
        billingClient.queryProductDetailsAsync(queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult,
                                                         @NonNull List<ProductDetails> productDetailsList) {
                        L.d("onProductDetailsResponse  List<ProductDetails> Size: " + productDetailsList.size());
                        if (productDetailsList.size() == 0) {
                            iabCallback.error(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                    ? "Unknow Error" : billingResult.getDebugMessage());
                        } else
                            getProductDetailSuccess(productDetailsList.get(0));
                    }
                }
        );
    }


    public void purchase(final String producId) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    iabCallback.error(billingResult.getDebugMessage());
                } else
                    onBillingSetupSuccess(producId);
            }

            @Override
            public void onBillingServiceDisconnected() {
                L.d("onBillingServiceDisconnected");
                iabCallback.error("Unknow Error");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }


    //region static ultils method
    public interface IABCallback {
        void error(String errorMessage);

        void success();
    }

    private static Intent getBindServiceIntent() {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    public static boolean isIabServiceAvailable(Activity activity) {
        final PackageManager packageManager = activity.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
        return list != null && list.size() > 0;
    }
    //endregion
}
