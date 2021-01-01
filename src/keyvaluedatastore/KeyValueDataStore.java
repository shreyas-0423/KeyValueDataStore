/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyvaluedatastore;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class can be used as a local storage for a single process on one
 * machine.
 *
 * @author Shreyas
 */
public class KeyValueDataStore {

    //private final static String DEFAULT_FILE_PATH = "./data/kvDataStore.txt";
    private final static String DEFAULT_FILE_PATH = "./data/kvDataStore.txt";
    private final static long DEFAULT_TIME_TO_LIVE = Long.MAX_VALUE;
    private final static long SEC_TO_MILLISECOND = 1000;

    private static HashSet<String> filesInUse = new HashSet<>();

    private String dbFilePath;
    private ConcurrentHashMap<String, JSONObject> keyValueMap;

    public KeyValueDataStore() throws FileAlreadyInUseException {
        dbFilePath = DEFAULT_FILE_PATH;
        try {
            initializeDataStore(dbFilePath);
        } catch (MalFormedFileException ex) {
            ex.printStackTrace();
        }
    }

    public KeyValueDataStore(String dbFilePath) throws MalFormedFileException {
        this.dbFilePath = dbFilePath;
        try {
            initializeDataStore(dbFilePath);
        } catch (FileAlreadyInUseException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeDataStore(String dbFilePath) throws MalFormedFileException, FileAlreadyInUseException {

        if (filesInUse.contains(dbFilePath)) {
            throw new FileAlreadyInUseException(dbFilePath + "already bring used by a process, please try after some time or specify another file");
        }

        filesInUse.add(dbFilePath);
        FileHandler.createFile(dbFilePath);

        keyValueMap = (ConcurrentHashMap<String, JSONObject>) FileHandler.parseFile(dbFilePath);
    }

    private void checkInput(String key, JSONObject obj) {

        if (key.length() > 32) {
            throw new KeyLengthTooLargeException("The key is always a String - capped at 32-chars");
        }

        if (obj == null) {
            throw new NullValueException("The value corresponding to a key cannot be null");
        }

        try {
            if (keyValueMap.containsKey(key) && ((Long) keyValueMap.get(key).get("time") > System.currentTimeMillis())) {
                throw new DuplicateKeyException("This key already exists, Duplicate key not allowed!");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is used to create a key-value pair which is also stored in
     * the file It also checks for valid input, in case any constraints are
     * breached, exceptions are thrown for the same.
     *
     * @param key
     * @param valueObj
     * @param ttl
     * @return
     */
    public boolean create(String key, JSONObject valueObj, int ttl) {

        checkInput(key, valueObj);

        long timeToLive = System.currentTimeMillis() + ttl * SEC_TO_MILLISECOND;

        if (ttl < 0) {
            timeToLive = DEFAULT_TIME_TO_LIVE;
        }

        JSONObject wrapper = new JSONObject();

        try {
            wrapper.put("data", valueObj);
            wrapper.put("time", timeToLive);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        keyValueMap.put(key, wrapper);
        String keyValueString = key + Utils.DELIMITER + Utils.encodeString(wrapper.toString());
        FileHandler.writeToFile(dbFilePath, keyValueString, true);

        return true;
    }

    /**
     * This is an overloaded form of the create method which will be used in
     * case the user does not specify the TTL field
     *
     * @param key
     * @param obj
     * @return
     */
    public boolean create(String key, JSONObject obj) {
        return create(key, obj, -1);
    }

    /**
     * This method is used to read the data corresponding to a key, in case the
     * key does not exists the method returns null.
     *
     * @param key
     * @return
     */
    public JSONObject read(String key) {

        if (keyValueMap.containsKey(key)) {
            try {
                if ((Long) keyValueMap.get(key).get("time") >= System.currentTimeMillis()) {
                    return (JSONObject) keyValueMap.get(key).get("data");
                } else {
                    FileHandler.deleteLineInFile(dbFilePath, key);
                    keyValueMap.remove(key);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method is used to delete the key-value pair, in case the key does
     * not exist it simply return false.
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        if (keyValueMap.containsKey(key)) {
            boolean flag = true;
            try {
                if ((Long) keyValueMap.get(key).get("time") <= System.currentTimeMillis()) {
                    flag = false;
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            FileHandler.deleteLineInFile(dbFilePath, key);
            keyValueMap.remove(key);
            return flag;
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        filesInUse.remove(this.dbFilePath);
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }
}
