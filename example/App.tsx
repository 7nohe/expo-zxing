import * as FileSystem from "expo-file-system";
import * as ImagePicker from "expo-image-picker";
import * as ExpoZxing from "expo-zxing";
import { useState } from "react";
import { Button, StyleSheet, Text, View } from "react-native";

export default function App() {
  const [results, setResults] = useState<string[]>([]);
  const handlePress = async () => {
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: await ImagePicker.MediaTypeOptions.All,
      quality: 0.5,
    });
    const uri = result.assets?.at(0)?.uri;
    if (!uri) return;
    const imageBase64 = await FileSystem.readAsStringAsync(uri, {
      encoding: FileSystem.EncodingType.Base64,
    });
    const results = ExpoZxing.decode(imageBase64);
    if (!results) return;
    setResults(results);
  };

  return (
    <View style={styles.container}>
      <Button title="Read" onPress={handlePress} />
      {results.map((result, index) => (
        <Text key={index}>{result}</Text>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    padding: 20,
  },
});
