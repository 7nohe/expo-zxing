import ExpoZxingModule from "./ExpoZxingModule";

export function decode(imageString: string): string[] | null {
  return ExpoZxingModule.decode(imageString);
}
