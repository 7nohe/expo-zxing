import { StyleSheet, Text, View } from 'react-native';

import * as ExpoZxing from 'expo-zxing';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoZxing.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
