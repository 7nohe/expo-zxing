import * as React from 'react';

import { ExpoZxingViewProps } from './ExpoZxing.types';

export default function ExpoZxingView(props: ExpoZxingViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
