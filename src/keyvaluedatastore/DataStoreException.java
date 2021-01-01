/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyvaluedatastore;

/**
 * This class contains the custom checked and unchecked exception.
 *
 * @author Shreyas
 */
public class DataStoreException extends Exception {

    DataStoreException(String s) {
        super(s);
    }

    DataStoreException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

class DuplicateKeyException extends RuntimeException {

    DuplicateKeyException(String s) {
        super(s);
    }
}

class KeyLengthTooLargeException extends RuntimeException {

    KeyLengthTooLargeException(String s) {
        super(s);
    }
}

class NullValueException extends RuntimeException {

    NullValueException(String s) {
        super(s);
    }
}

class MalFormedFileException extends DataStoreException {

    MalFormedFileException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

class FileAlreadyInUseException extends DataStoreException {

    FileAlreadyInUseException(String errorMessage) {
        super(errorMessage);
    }
}
