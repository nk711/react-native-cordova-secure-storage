# react-native-cordova-secure-storage

A native implementation of the cordova-based secure storage plugin as an NPM package. Only supports Android at this point in time.

This is based on the cordova-plugin-secure-storage which can be found here: https://github.com/crypho/cordova-plugin-secure-storage. 

The code is no longer maintained, and therefore should be used with precaution. The intended use for this package is for a React Native App to access the keystore of a Cordova based App using the cordova-based secure storage plugin.

This package allows your application to securely store secrets such as usernames, passwords, tokens, certificates or other sensitive information (strings) on iOS & Android phones and Windows devices.

This package 

## Installation

```sh
npm install react-native-cordova-secure-storage
```

## Usage

```js
import SecureStorage from 'react-native-cordova-secure-storage';

// ...
try {
  const keys = SecureStorage.keys();
  console.log(keys);
} catch (e) {
  console.log(e)
}
```

## Interface

```tsx
import {NativeModules} from 'react-native';
const { SecureStorage } = NativeModules; 

interface SecureStorageInterface {
    get(key: String, value: String): Promise<String>;
    set(key: String, value: String): Promise<String>;
    keys(): Promise<String>;
    remove(key: String): Boolean;
    clear(): Boolean;
}

export default SecureStorage  as SecureStorageInterface;
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
