/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyvaluedatastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides as an utility for handling all the file related
 * operations, also it does uses bufferedReader and bufferedWriter to perform
 * operation so that the file does not needs to be fetched in memory.
 *
 * @author Shreyas
 */
public class FileHandler {

    public static void createFile(String path) {
        Path filePath = Paths.get(path);
        try {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        } catch (FileAlreadyExistsException fae) {
            //Don't want to throw exception if File already exists
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, JSONObject> parseFile(String filePath) throws MalFormedFileException {

        Map<String, JSONObject> keyValueMap = new ConcurrentHashMap<>();
        //BufferedReader is synchronized, so read operations on a BufferedReader can safely be done from multiple threads
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
            String line;
            int pos = 0;
            while ((line = br.readLine()) != null) {
                pos = line.indexOf(Utils.DELIMITER);
                keyValueMap.put(line.substring(0, pos), new JSONObject(Utils.dcodeString(line.substring(pos + 1))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException ex) {
            throw new MalFormedFileException("Te file " + filePath + " is not in the correct format", ex);
        }
        return keyValueMap;

    }

    public static boolean writeToFile(String filePath, Object value, boolean append) {
        File file = new File(filePath);

        if (!file.canWrite() || (file.length() + (16 * 1024)) > 1073741824L) //if File size + current data(16KB) exceeds 1 GB or File is not writable error
        {
            return false;
        }
        try (PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file, append)))) {
            pr.println(value);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteLineInFile(String filePath, String key) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
                PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath))))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(key) && line.indexOf(":") == key.length()) {
                    continue;
                }
                pr.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
