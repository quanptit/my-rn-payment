import { NativeModules } from 'react-native';
import { isIOS, RNCommonUtils } from "my-rn-base-utils";
let IAPUtilsNative = NativeModules.IAPUtils;
/*
Android:
 - Tao id: vip_user

IOS
1. Enable In-app purchase trong setting của app. trong mục capabilities
2. Vào trang submit app. mục Features: tạo một productId. thường chọn kiểu là Non-Consumable
    + Reference Name: Vip User
    + Product ID: {iosBundleID}.vipuser (Ví dụ study.writing.learning.japanese.vipuser)
    + Become Vip User
    + This IAP for upgrade normal user to VIP user
3, Add cái product vừa tạo vào chỗ IAP của app info
* */
export default {
    purchase(productId, IAP_LICENSE_KEY) {
        if (isIOS()) {
            return new Promise(function (resolve, reject) {
                IAPUtilsNative.purchaseProduct(productId, function (value) {
                    resolve(value);
                }, function (error) {
                    reject(error);
                });
            });
        }
        return IAPUtilsNative.purchaseProduct(productId, IAP_LICENSE_KEY);
    },
    restoreProduct() {
        if (isIOS())
            return IAPUtilsNative.restoreProduct();
        return undefined;
    },
    setVipUser() {
        return RNCommonUtils.setVIPUser();
    }
};
