import { Component } from 'react';
import { ViewProperties } from 'react-native';
interface Props extends ViewProperties {
    callbackUpdateVip: () => void;
}
export default class DialogTheoCao extends Component<Props> {
}
export {};
