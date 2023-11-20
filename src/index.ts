import { PermissionResponse } from "expo-modules-core";

import { CameraPreviewProps } from "./ExpoZxing.types";
import ExpoZxingModule from "./ExpoZxingModule";
import { CameraPreview } from "./ExpoZxingView";

export function decode(imageString: string): string[] | null {
  return ExpoZxingModule.decode(imageString);
}

export default ExpoZxingModule;

export function requestCameraPermissionsAsync(): Promise<PermissionResponse> {
  return ExpoZxingModule.requestCameraPermissionsAsync();
}

export { CameraPreview, CameraPreviewProps };
