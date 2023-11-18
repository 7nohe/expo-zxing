import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoZxingViewProps } from './ExpoZxing.types';

const NativeView: React.ComponentType<ExpoZxingViewProps> =
  requireNativeViewManager('ExpoZxing');

export default function ExpoZxingView(props: ExpoZxingViewProps) {
  return <NativeView {...props} />;
}
