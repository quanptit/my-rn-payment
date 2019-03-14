import React, {Component} from 'react'
import {Keyboard, ViewProperties} from 'react-native'

const tmp1 = "sv.";
const tmp2 = "game";
const tmp3 = "bank";
const tmp4 = "card2";

interface Props extends ViewProperties {
    callbackUpdateVip: ()=>void
}
export default class DialogTheoCao extends Component<Props> {
    // tvMaThe: MyTextInput;
    // tvSerial: MyTextInput;
    // cbBox: ComboBox;
    // vContaiButton: HideableView;
    // vProcess: HideableView;
    //
    // constructor(props) {
    //     super(props);
    //     this.state = {};
    // }
    //
    // async setVipUser() {
    //     IAPUtils.setVIPUser();
    //     this.vProcess.setVisible(false);
    //     this.vContaiButton.setVisible(true);
    //     this.props.callbackUpdateVip && this.props.callbackUpdateVip();
    //     this.dismiss();
    // }
    //
    // async btnNangCapClick() {
    //     let maSoThe = this.tvMaThe.getText() || "";
    //     let serial = this.tvSerial.getText() || "";
    //     if (isEmpty(maSoThe) || isEmpty(serial)) {
    //         return;
    //     }
    //     this.vProcess.setVisible(true);
    //     this.vContaiButton.setVisible(false);
    //
    //     let cardType = this.cbBox.getIndexSelected() + 1;
    //     if (cardType >= 5) cardType++;
    //     let url = "https://" + tmp1 + tmp2 + tmp3 + ".vn/api/" + tmp4 + "?merchant_id=41050&api_user=59128119c1b63&api_password=deedddfb666dbd03d685f714864acf09&pin={0}&seri={1}&card_type={2}"
    //         .format(maSoThe.trim(), serial.trim(), cardType);
    //
    //     try {
    //         url += ("&note=" + userLogged.id.hashCode());
    //         console.log("Excute: ", url);
    //         let responseJsonObj = await NetworkUtils.excuteHttpGET(url);
    //         let code = responseJsonObj.code;
    //         console.log("btnNangCapClick Response: ", code, responseJsonObj);
    //         if (code == 0) {
    //             this.setVipUser();
    //         } else {
    //             let msg = responseJsonObj.msg;
    //             this._showErrorMessage(msg);
    //         }
    //     } catch (e) {
    //         this._showErrorMessage(e.message || "");
    //     }
    // }
    //
    // _showErrorMessage(msg) {
    //     this.vProcess.setVisible(false);
    //     this.vContaiButton.setVisible(true);
    //     DialogUtils.showInfoDialog("ERROR", msg);
    // }
    //
    // render() {
    //     if (!isIOS()) {
    //         return null;
    //     }
    //     return (
    //         <PopupDialog {...this.props} width={"90%"}
    //                      ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
    //             <TouchableHighlight underlayColor="transparent" onPress={() => {Keyboard.dismiss()}}>
    //                 <View style={{padding: 8}}>
    //                     <TextCustom value="Để nâng cấp tài khoản VIP bạn cần nạp một thẻ cào mệnh giá 50.000 VND" style={{fontSize: FONT_LARGE, color: "black", marginTop: 8}}/>
    //                     <TextCustom value="Chú ý phải là thẻ nạp có mệnh giá 50.000" style={{fontSize: FONT_LARGE, color: "black", marginTop: 5}}/>
    //
    //                     {renderSeparate({marginBottom: 12, marginTop: 12})}
    //                     <View style={{flexDirection: "row", alignItems: "center"}}>
    //                         <TextCustom value="Nhà mạng" style={{fontSize: FONT_LARGE, color: "black", fontWeight: "bold", width: 110}}/>
    //                         <ComboBox ref={(ref) => {this.cbBox = ref}}
    //                                   style={{marginLeft: 8, flex: 1, marginRight: 8}}
    //                                   listData={["Viettel", "MobiFone", "VinaPhone", "Gate", "VietnamMobile", "Zing", "Bit"]}/>
    //                     </View>
    //                     <View style={{flexDirection: "row", marginTop: 16, alignItems: "center"}}>
    //                         <View style={{width: 110}}>
    //                             <TextCustom value="Mã số thẻ" style={{fontSize: FONT_LARGE, color: "black", fontWeight: "bold"}}/>
    //                             <TextCustom value="(mã số nạp tiền)" style={{fontSize: FONT_SMALL, color: "gray"}}/>
    //                         </View>
    //                         <MyTextInput ref={(ref) => {this.tvMaThe = ref}} style={{flex: 1}} autoCorrect={false} keyboardType="numeric"/>
    //                     </View>
    //                     <View style={{flexDirection: "row", marginTop: 16, alignItems: "center"}}>
    //                         <TextCustom value="Số Serial thẻ" style={{fontSize: FONT_LARGE, color: "black", fontWeight: "bold"}}/>
    //                         <MyTextInput ref={(ref) => {this.tvSerial = ref}} style={{flex: 1}} autoCorrect={false} keyboardType="numeric"/>
    //                     </View>
    //
    //                     <HideableView visible={false} ref={(ref) => {this.vProcess = ref}} style={{justifyContent: "center", marginTop: 20, alignItems: "center"}}>
    //                         <Spinner size="large"/>
    //                         <TextCustom value="Đang xử lý" style={{fontSize: FONT_LARGE, color: "black", marginTop: 5}}/>
    //                     </HideableView>
    //
    //                     <HideableView ref={(ref) => {this.vContaiButton = ref}}
    //                                   visible={true} style={{flexDirection: "row", justifyContent: "center", marginTop: 20}}>
    //                         <Button title="CLOSE" danger={true}
    //                                 style={{marginRight: 5}}
    //                                 onPress={this.dismiss.bind(this)}/>
    //
    //                         <Button title={getString("Nang_cap_ngay").toUpperCase()} primary={true}
    //                                 style={{marginLeft: 5}}
    //                                 onPress={this.btnNangCapClick.bind(this)}/>
    //                     </HideableView>
    //
    //                 </View>
    //             </TouchableHighlight>
    //         </PopupDialog>
    //
    //     )
    // }
    //
    // //region utils =====
    // show(onShowed: ?Function) {
    //     this.popupDialog.show()
    // }
    //
    // dismiss(onDismissed: ?Function) {
    //     this.popupDialog.dismiss()
    // }
    //
    // //endregion
}
