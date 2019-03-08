import React, {Component} from 'react'
import IAPUtils from './IAPUtils'
import DialogTheoCao from './DialogTheoCao'
import {Button, ButtonModel, DialogUtils, PopupDialog, StyleUtils, TextCustom, Toast} from "my-rn-base-component";
import {isEmpty, isIOS, sendError} from "my-rn-base-utils";
import {UserUtils} from "react-native-login";
import {getStringsCommon} from "my-rn-common-resource";

interface Props {
    callbackUpdateVip?: () => void
    iosBundleID?: string
    IAP_LICENSE_KEY: string
}

const s = StyleUtils.getAllStyle();

export class DialogPaymentChoose extends Component<Props> {
    private isEnableTheCao: boolean;
    private popupDialog: PopupDialog;

    //region Event ===========
    async btnIAPClick() {
        try {
            let productId = isIOS() ? (this.props + ".vipuser") : "vip_user";
            await IAPUtils.purchase(productId, this.props.IAP_LICENSE_KEY);
            await this._setVipUser()
        } catch (e) {
            sendError(e);
            if (!isEmpty(e)) {
                Toast.showLongBottom("ERROR: " + e.message)
            }
        }
    }

    async btnRestoreClick() {
        try {
            let success = await IAPUtils.restoreProduct();
            if (!success) {
                Toast.showLongBottom("Nothing to Restore");
                return
            }
            await this._setVipUser()
        } catch (e) {
            Toast.showLongBottom(e.message)
        }
    }

    async _setVipUser() {
        await UserUtils.setVipUser();
        Toast.showLongBottom(getStringsCommon().success);
        this.callbackUpdateVip()
    }

    callbackUpdateVip() {
        this.props.callbackUpdateVip && this.props.callbackUpdateVip();
        DialogUtils.hideDialog();
        DialogUtils.showInfoDialog(null, getStringsCommon().success_need_restart)
    }

    //endregion

    render() {
        let buttons = [];
        if (this.isEnableTheCao) {
            buttons.push(<Button key={buttons.length} title="Nâng cấp qua thẻ cào điện thoại"
                                 model={ButtonModel.primary} style={{margin: 12}}
                                 onPress={() => {
                                     if (isIOS())
                                         DialogUtils.showDialog(<DialogTheoCao
                                             style={{justifyContent: "flex-start", paddingTop: 20}}
                                             callbackUpdateVip={this.callbackUpdateVip.bind(this)}/>)
                                 }}/>)
        }
        buttons.push(<Button key={buttons.length} title={getStringsCommon().Nang_cap_ngay} model={ButtonModel.warning}
                             style={{marginLeft: 12, marginRight: 12, marginTop: 12, marginBottom: isIOS() ? 0 : 12}}
                             onPress={this.btnIAPClick.bind(this)}/>);
        if (isIOS())
            buttons.push(<Button key={buttons.length}
                                 title={"Did you buy earlier? Restore now"} model={ButtonModel.light}
                                 style={{margin: 12}}
                                 onPress={this.btnRestoreClick.bind(this)}/>);

        return (
            <PopupDialog {...this.props} width={"90%"} ref={(popupDialog) => { this.popupDialog = popupDialog }}>
                <TextCustom value={"VIP Member Benefits: "} style={[s.f_lar_b, s.black, {margin: 10}]}/>
                <TextCustom value={"1. Use everywhere, cross-platform"} style={[s.f_lar, {fontWeight: "500", color: "black", margin: 12}]}/>
                <TextCustom value={"2. No ads"} style={[s.f_lar, {fontWeight: "500", color: "black", margin: 12}]}/>
                {buttons}
            </PopupDialog>
        )

    }

    //region utils =====
    show(onShowed?) {
        this.popupDialog.show()
    }

    dismiss(onDismissed?) {
        this.popupDialog.dismiss()
    }

    public static showDialog(props?: Props) {
        DialogUtils.showDialog(<DialogPaymentChoose {...props}/>)
    }

    //endregion
}
