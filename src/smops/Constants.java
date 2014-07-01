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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import smops.dao.FormPurpose;
import smops.dao.InfoType;

/**
 *
 * @author Aale
 */
public class Constants {

    public static final Map<String, List<String>> purpose_keywords = new HashMap<String, List<String>>();
    public static final Set<String> purpose_set = new HashSet<String>();

    public static final Map<String, List<String>> infoType_keywords = new HashMap<String, List<String>>();
    public static final Set<String> infoType_set = new HashSet<String>();

    static {
        purpose_set.addAll(Arrays.asList(
                FormPurpose.SUBSCRIPTION,
                FormPurpose.CONTACT,
                FormPurpose.REFERRAL,
                FormPurpose.RESERVATION,
                FormPurpose.FEEDBACK,
                FormPurpose.LOGIN,
                FormPurpose.STORE_LOCATOR));

        infoType_set.addAll(Arrays.asList(
                InfoType.EMAIL,
                InfoType.BIRTH_DATE,
                InfoType.AGE,
                InfoType.CITY,
                InfoType.STATE,
                InfoType.GENDER,
                InfoType.LANGUAGE_PREFERENCE,
                InfoType.MAILING_ADDRESS,
                InfoType.NAME,
                InfoType.ORGANIZATION,
                InfoType.PHONE_NUMBER,
                InfoType.RACE,
                InfoType.STREET,
                InfoType.USERNAME,
                InfoType.ZIP_CODE));
        initMap(purpose_keywords, purpose_set, "conf/purpose.txt");
        initMap(infoType_keywords, infoType_set, "conf/infoType");
    }

    public static void initMap(Map<String, List<String>> map, Set<String> set, String filepath) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("\t");
                String key = split[0];
                if (set.contains(key)) {
                    List<String> values = new ArrayList<String>();
                    for (int i = 1; i < split.length; i++) {
                        values.add(split[i]);
                    }
                    map.put(key, values);
                } else {
                    System.err.println("Invalid Key = " + key);
                }
                line = br.readLine();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        System.out.println(isContains("zip Code*", infoType_keywords.get(InfoType.MAILING_ADDRESS)));
    }
}
