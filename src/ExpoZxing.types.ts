import { ViewProps } from "react-native";

export type OnScannedEvent = {
  result: string;
};

export type CameraPreviewProps = {
  onScanned?: (event: { nativeEvent: OnScannedEvent }) => void;
} & ViewProps;
