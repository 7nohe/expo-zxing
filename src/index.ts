import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoZxing.web.ts
// and on native platforms to ExpoZxing.ts
import ExpoZxingModule from './ExpoZxingModule';
import ExpoZxingView from './ExpoZxingView';
import { ChangeEventPayload, ExpoZxingViewProps } from './ExpoZxing.types';

// Get the native constant value.
export const PI = ExpoZxingModule.PI;

export function hello(): string {
  return ExpoZxingModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoZxingModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoZxingModule ?? NativeModulesProxy.ExpoZxing);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoZxingView, ExpoZxingViewProps, ChangeEventPayload };
