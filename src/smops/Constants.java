/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import smops.dao.FormPurpose;
import smops.dao.InfoType;

/**
 *
 * @author Aale
 */
public class Constants {

    public static final Map<String, List<String>> purpose_keywords = new HashMap<String, List<String>>();

    static {
        purpose_keywords.put(FormPurpose.SUBSCRIPTION, Arrays.asList("newsletter", "subscribe", "sign up", "sign-up", "join", "email list"));
        purpose_keywords.put(FormPurpose.CONTACT, Arrays.asList("contact"));
        purpose_keywords.put(FormPurpose.REFERRAL, Arrays.asList("invite", "friend"));
        purpose_keywords.put(FormPurpose.RESERVATION, Arrays.asList("reserve", "reservation", "appointment", "appoin"));
        purpose_keywords.put(FormPurpose.FEEDBACK, Arrays.asList("comment", "feedback", "complain"));
        purpose_keywords.put(FormPurpose.LOGIN, Arrays.asList("login", "sign in", "sign-in"));
        purpose_keywords.put(FormPurpose.STORE_LOCATOR, Arrays.asList("find a location", "near you", "store location", "find a store", "find store"));
    }

    public static final Map<String, List<String>> infoType_keywords = new HashMap<String, List<String>>();

    static {
        infoType_keywords.put(InfoType.EMAIL, Arrays.asList("email", "e-mail"));
        infoType_keywords.put(InfoType.BIRTH_DATE, Arrays.asList("birth"));
        infoType_keywords.put(InfoType.AGE, Arrays.asList(" age"));
        infoType_keywords.put(InfoType.CITY, Arrays.asList("city", "~zip"));

        infoType_keywords.put(InfoType.STATE, Arrays.asList("state"));
        infoType_keywords.put(InfoType.GENDER, Arrays.asList("gender"));
        infoType_keywords.put(InfoType.LANGUAGE_PREFERENCE, Arrays.asList("language"));
        infoType_keywords.put(InfoType.MAILING_ADDRESS, Arrays.asList("mailing", "address", "~email", "~e-mail", "~zip", "~state", "~city", "~postalcode", "~postal code"));
        infoType_keywords.put(InfoType.NAME, Arrays.asList("firstname", "first name", "first_name", "lastname", "last name", "last_name", "your name", "your_name", "yourname", "friend name", "friendname", "friend_name", "fullname", "full name", "contact name","contactname","contact_name"));
        infoType_keywords.put(InfoType.ORGANIZATION, Arrays.asList("organization", "company"));
        infoType_keywords.put(InfoType.PHONE_NUMBER, Arrays.asList("phone", "tele", "cell", "fax", "~email", "~e-mail", "~name"));
        infoType_keywords.put(InfoType.RACE, Arrays.asList("race", "ethnicity"));
        infoType_keywords.put(InfoType.STREET, Arrays.asList("street"));
        infoType_keywords.put(InfoType.USERNAME, Arrays.asList("user name", "username"));
        infoType_keywords.put(InfoType.ZIP_CODE, Arrays.asList("zipcode", "zip code", "postal code", "zip-code", "zip"));
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
