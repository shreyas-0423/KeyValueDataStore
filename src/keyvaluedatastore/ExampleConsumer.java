/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyvaluedatastore;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is an example class to show the working of the dataStore.
 * @author Shreyas
 */
public class ExampleConsumer {

    public static void main(String[] args) throws JSONException, FileAlreadyInUseException {
        KeyValueDataStore keyValueDataStore = new KeyValueDataStore();
        JSONObject temp = new JSONObject();
        temp.put("name", "Shreyas mahajan");
        temp.put("location", "indore");
        
        System.out.println(keyValueDataStore.create("details_imp", temp));
        System.out.println(keyValueDataStore.read("details"));       
    }
}
