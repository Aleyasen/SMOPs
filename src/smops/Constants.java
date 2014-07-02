/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aale
 */
public class Constants {

    public static Map<String, List<String>> purpose_keywords = new HashMap<String, List<String>>();
    public static Map<String, List<String>> infoType_keywords = new HashMap<String, List<String>>();

    public static Map<String, List<String>> initMap(String filepath) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("\t");
                String key = split[0];
                List<String> values = new ArrayList<String>();
                for (int i = 1; i < split.length; i++) {
                    values.add(split[i]);
                }
                map.put(key, values);
                line = br.readLine();
            }
            return map;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getPurpose(String str) {
        return getKey(str, purpose_keywords);
    }

    public static String getInfoType(String str) {
        return getKey(str, infoType_keywords);
    }

    public static String getKey(String str, Map<String, List<String>> map) {
        String norm_str = str.toLowerCase();
        for (String key : map.keySet()) {
            if (isContains(norm_str, map.get(key))) {
                return key;
            }
        }
        return null;
    }

    public static boolean isContains(String str, List<String> list) {
        for (String item : list) {
            if (item.startsWith("~")) {
                String actual = item.substring(1);
                if (str.contains(actual)) {
                    return false;
                }
            }
        }
        for (String item : list) {
            if (!item.startsWith("~")) {
                if (str.contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        System.out.println(isContains("zip Code*", infoType_keywords.get(InfoType.MAILING_ADDRESS)));

        purpose_keywords = initMap("conf/purpose.txt");
        infoType_keywords = initMap("conf/infotype.txt");
        for (String key : infoType_keywords.keySet()) {
            System.out.println(key + "  ==> " + infoType_keywords.get(key));
        }
    }
}
