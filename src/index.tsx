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

