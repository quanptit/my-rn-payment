import { Component } from 'react';
interface Props {
    callbackUpdateVip?: () => void;
    iosBundleID?: string;
    IAP_LICENSE_KEY: string;
}
export declare class DialogPaymentChoose extends Component<Props> {
    private isEnableTheCao;
    private popupDialog;
    btnIAPClick(): Promise<void>;
    btnRestoreClick(): Promise<void>;
    _setVipUser(): Promise<void>;
    callbackUpdateVip(): void;
    render(): JSX.Element;
    show(onShowed?: any): void;
    dismiss(onDismissed?: any): void;
    static showDialog(props?: Props): void;
}
export {};
