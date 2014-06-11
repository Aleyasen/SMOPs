/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops.dao;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import smops.Constants;
import smops.HibernateUtil;
import smops.hibernate.Business;
import smops.hibernate.Field;
import smops.hibernate.Form;

/**
 *
 * @author Aale
 */
public class BusinessManager {

    public static List<Business> getBusinesses(int offset, int limit) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String queryStr = "from Business";
        Query query = session.createQuery(queryStr);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<Business> biz = (List<Business>) query.list();
        return biz;
    }

    public static int getValidFormsCount(Business biz) {
        int count = 0;
        for (Object f : biz.getForms()) {
            Form form = (Form) f;
            if (form.getPurpose().equals("unknown")) {
                continue;
            }
            count++;
        }
        return count;
    }

    public static void prepareReport(int offset, int limit) {
        JSONArray all_json = new JSONArray();
        final List<Business> bizs = getBusinesses(offset, limit);
        for (Business biz : bizs) {
            if (getValidFormsCount(biz) == 0) {
                continue;
            }
//            System.out.println("");
//            System.out.println("[" + biz.getName() + "] " + biz.getUrl());
            JSONObject biz_json = new JSONObject();
            biz_json.put("name", biz.getName());
//            biz_json.put("url", biz.getUrl());
            JSONArray forms_json = new JSONArray();
            for (Object f : biz.getForms()) {
                Form form = (Form) f;
                if (form.getPurpose().equals("unknown")) {
                    continue;
                }
                JSONObject f_json = new JSONObject();
                f_json.put("page_url", " " + form.getPageUrl() + " ");
                f_json.put("purpose", form.getPurpose());
//                System.out.println("\t" + form.getPageUrl());
//                System.out.print("\t[" + form.getPurpose() + "]: ");
                String fields_str = "";
                for (Object inp : form.getFields()) {
                    Field field = (Field) inp;
                    if (field.getInfoType().equals("unknown")) {
                        continue;
                    }
                    fields_str += field.getInfoType() + " ";
                }
                f_json.put("fields", fields_str);
                forms_json.add(f_json);
//                System.out.println(fields_str);
            }
            if (!forms_json.isEmpty()) {
                biz_json.put("forms", forms_json);
            }
            if (!biz_json.isEmpty()) {
                all_json.add(biz_json);
            }
        }
        StringWriter out = new StringWriter();
        try {
            all_json.writeJSONString(out);
        } catch (IOException ex) {
            Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String jsonText = out.toString();
        System.out.print(jsonText.replaceAll("/", ""));
    }

    public static void prepareTableReport(int offset, int limit) {
        JSONArray all_json = new JSONArray();
        final List<Business> bizs = getBusinesses(offset, limit);
        for (Business biz : bizs) {
            if (getValidFormsCount(biz) == 0) {
                continue;
            }
//            System.out.println("");
//            System.out.println("[" + biz.getName() + "] " + biz.getUrl());
            JSONObject biz_json = new JSONObject();
            biz_json.put("name", biz.getName());
//            biz_json.put("url", biz.getUrl());
            JSONArray forms_json = new JSONArray();
            for (Object f : biz.getForms()) {
                Form form = (Form) f;
                if (!form.getPurpose().equals("unknown")) {
                    continue;
                }
                JSONObject f_json = new JSONObject();
                f_json.put("page_url", " " + form.getPageUrl() + " ");
                f_json.put("purpose", form.getPurpose());
                System.out.print(biz.getName() + "\t");
                System.out.print(form.getPageUrl() + "\t");
                System.out.print(form.getPurpose() + "\t");
//                System.out.println("\t" + form.getPageUrl());
//                System.out.print("\t[" + form.getPurpose() + "]: ");
                String fields_str = "";
                List<String> fields_infoType = new ArrayList<>();
                for (Object inp : form.getFields()) {
                    Field field = (Field) inp;
                    fields_infoType.add(field.getInfoType());
                }
                for (String inftype : InfoType.forms_types) {
                    if (fields_infoType.contains(inftype)) {
                        System.out.print("1\t");
                    } else {
                        System.out.print(" \t");
                    }

                }
                for (Object inp : form.getFields()) {
                    Field field = (Field) inp;
                    if (field.getInfoType().equals("unknown")) {
                        continue;
                    }
                    fields_str += field.getInfoType() + " ";
                }
                f_json.put("fields", fields_str);
                forms_json.add(f_json);
//                System.out.println(fields_str);
                System.out.println();
            }
            if (!forms_json.isEmpty()) {
                biz_json.put("forms", forms_json);
            }
            if (!biz_json.isEmpty()) {
                all_json.add(biz_json);
            }
        }
        StringWriter out = new StringWriter();
        try {
            all_json.writeJSONString(out);
        } catch (IOException ex) {
            Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String jsonText = out.toString();
//        System.out.print(jsonText.replaceAll("/", ""));
    }

    public static List<String> getWebsites(int offset, int limit) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String queryStr = "select distinct website from Business";
        Query query = session.createQuery(queryStr);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<String> websites = (List<String>) query.list();
        return websites;
    }

    public static void main(String[] args) {
        prepareTableReport(100, 100);
//        final List<Business> biz = getBusinesses(0, 100);
//        System.out.println(biz);
    }
}
