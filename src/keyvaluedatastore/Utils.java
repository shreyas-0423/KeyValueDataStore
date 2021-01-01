/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyvaluedatastore;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * this acts as a utility class and also keeps the constants being shared
 * between different classes.
 *
 *
 * @author Shreyas
 */
public class Utils {

    final static String DELIMITER = ":";

    public static String encodeString(String value) {
        String base64EncodedValue = Base64.getEncoder().encodeToString(value.getBytes());
        return base64EncodedValue;
    }

    public static String dcodeString(String value) {
        // Decode data using Base64
        byte[] decodedBytes = Base64.getDecoder().decode(value);
        String decodedStr = new String(decodedBytes, Charset.defaultCharset());
        return decodedStr;
    }
}
