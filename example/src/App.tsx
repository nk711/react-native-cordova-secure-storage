import * as React from 'react';

import { View, Button } from 'react-native';
import SecureStorage from 'react-native-cordova-secure-storage';

export default function App() {
  const onPress = async() => {
    try {
      const result = await SecureStorage.keys();
      console.log("test", result);

    } catch (e) {
      console.log(e)
    }
  }

  return (
    <View>
      <Button 
            title = "CLick to invoke your native module!"
            color = "#841584"
            onPress = {onPress}
          />
    </View>
  );
}

