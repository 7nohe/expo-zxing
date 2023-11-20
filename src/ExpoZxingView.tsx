import { requireNativeViewManager } from "expo-modules-core";
import React from "react";

import { CameraPreviewProps } from "./ExpoZxing.types";

const NativeView: React.ComponentType<CameraPreviewProps> =
  requireNativeViewManager("ExpoZxing");

export function CameraPreview({ ...props }: CameraPreviewProps) {
  return <NativeView {...props} />;
}
