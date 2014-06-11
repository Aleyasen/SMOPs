/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aale
 */
public class InfoType {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MAILING_ADDRESS = "mailing_address";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String USERNAME = "username";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP_CODE = "zip_code";
    public static final String ORGANIZATION = "organization";
    public static final String AGE = "age";
    public static final String BIRTH_DATE = "birth_date";
    public static final String GENDER = "gender";
    public static final String RACE = "race";
    public static final String IP_ADDRESS = "ip_address";
    public static final String BROWSER_TYPE = "browser_type";

    public static final String DEVICE_TYPE = "device_type";
    public static final String BROWSER_PLUGINS_DETAILS = "browser_plugins_details";
    public static final String TIME_ZONE = "time_zone";
    public static final String BROWSER_HISTORY = "browser_history";
    public static final String LANGUAGE_PREFERENCE = "language_preference";
    public static final String BROWSER_SNIFFING = "browser_sniffing";
    public static final String UNKNOWN = "unknown";

    public static final List<String> forms_types = new ArrayList<>(Arrays.asList(
            NAME, EMAIL, MAILING_ADDRESS, PHONE_NUMBER, USERNAME,
            STREET, CITY, STATE, ZIP_CODE, ORGANIZATION,
            AGE, BIRTH_DATE, GENDER, RACE, IP_ADDRESS
    ));
}
