import { CameraPreview, requestCameraPermissionsAsync } from "expo-zxing";
import { useEffect, useState } from "react";
import { View, StyleSheet, Text } from "react-native";

export function CameraScreen() {
  const [permissionGranted, setPermissionGranted] = useState(false);
  useEffect(() => {
    (async () => {
      const { status } = await requestCameraPermissionsAsync();
      setPermissionGranted(status === "granted");
    })();
  }, []);

  return (
    <View style={styles.container}>
      {permissionGranted ? (
        <CameraPreview
          style={{ flex: 1 }}
          onScanned={(event) => {
            console.log(event.nativeEvent.result);
          }}
        />
      ) : (
        <Text style={{ color: "white" }}>
          Camera permissions are required to use the QR Code scanner
        </Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#000000",
  },
});
