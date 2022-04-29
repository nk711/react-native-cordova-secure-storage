import SecureStorage from 'react-native-cordova-secure-storage';

describe('cordova-plugin-secure-storage', function () {


    it('should be defined', () => {
        expect(SecureStorage).toBeDefined();
    });

    it('should be able to initialize', () => {

    });

    it('should be able to set a key/value', () => {

    });

    it('should be able to get a key/value', () => {

    });

    it('should be able to get a key/value that was set from a previous instance of SecureStorage', () => {

    });

    it('should call the error handler when getting a key that does not exist', () => {

    });

    it('should call the error handler when getting a key that existed but got deleted', () => {

    });

    
    it('should call the error handler when setting a value that is not a string', () => {

    });

    it('should be able to remove a key/value', () => {

    });


    it('should be able to set and retrieve multiple values in sequence', () => {

    });
    it('should be able to get the storage keys as an array', () => {

    });
    it('should be able to clear the storage', () => {

    });
    it('should return [] when keys is called and the storage is empty', () => {

    });
    it('should be able to clear without error when the storage is empty', () => {

    });
    it('should be able to handle simultaneous sets and gets', () => {

    });
    it('should not be able to get a key/value that was set from a another instance of SecureStorage', () => {

    });
    it('different instances should be able to get different values for the same key', () => {

    });
    
}