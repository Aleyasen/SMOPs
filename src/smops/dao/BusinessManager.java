/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops.dao;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import smops.Constants;
import smops.HibernateUtil;
import smops.Utils;
import smops.hibernate.Business;
import smops.hibernate.Field;
import smops.hibernate.Form;
import smops.hibernate.JsLib;

/**
 *
 * @author Aale
 */
public class BusinessManager {

    static List<String> websitesForTest = Arrays.asList("denimexpress.com", "fivecentnickel.com", "sprucecreekonline.com", "rockinghamtoyota.com", "cefaly.us", "worthynews.com", "agusti2.eu", "breckenridgefineart.org", "daamusic.org", "floridamarinetanks.com", "http.com", "lomaricareliefsociety.org", "nhltorrents.co.uk", "radonzone.com", "spiritfireimages.com", "tubeomega.com", "wildyoats.com", "wguc.com", "goalmascots.com", "emmawatsonofficial.com", "lastbottlewines.com", "ghthealth.com", "onhandsoftware.com", "prier.com", "digitalpeddler.com", "lymenet.nl", "yaho.cm", "russellbrand.com", "hiff.org", "republicanoperative.com", "themailboxcompanion.com", "sweeney-emporium.com", "refrigerationsupply.com", "crystalmccallsoldteam.com", "literaryservicesinc.com", "woodstockgroundhog.org", "ronique.net", "peteranswers.com", "acsedu.com", "kaiserslauternamerican.com", "metalwareprollectibles.com", "505turbo.com", "bunchesmore.com", "marsels.com", "geniusbrainpower.com", "eachedule.com", "christianhubert.com", "localjobcentral.com", "walking-pneumonia.org", "topcarwarranty.com", "medi-syn.com", "hvacfun.com", "electroniccigarettesale.net", "almustaqbal.com", "tennesseejones.com", "stepinsally.com", "stih-schnock.de", "kolstermbakery.com", "itbegins2012.com", "rangemasterfence.com", "amotostuff.com", "vanguaerdgroup.com", "southharrisonwater.com", "luxembourg-paris-hotel.com", "thehighlinehotel.com", "vancouvertours.net", "everythingfinancialradio.com", "hrpharma.com", "okterritorialmuseum.org", "turistinka.ru", "creampiemovie.org", "electricity.cm", "centraldrugspdx.com", "cricklers.com", "ghbcc.com", "prairieviewanimalhospital.com", "stonecreektrading.com", "usajeruty.pl", "fleamarketnewsonline.com", "hoppeidustries.com", "sensaul.com", "claytonofknoxville.com", "astonishcleaners.com", "discovergold.org", "copesaddlery.com", "jimfalkmotorsofmaui.com", "fatpleasure.com", "houseofjameson.com", "filmforallnyc.com", "gtrlc.org", "yourlivedigest.com", "b5b581a15e.com", "ab4bj.com", "discountitemstore.com", "aurorapower.net", "visitlakemichiganlighthouses.com", "capitol360.com", "guildwars2hub.com", "downloadfreegames.dk", "burn-iso.com");

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
        final List<Business> bizs = getBusinesses(offset, limit);
        prepareTableReport(bizs);
    }

    public static void prepareTableReportByWebsites(List<String> websites) {
        List<Business> bizs = new ArrayList();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        for (String website : websites) {
            final Business biz = BusinessManager.getByWebsite(website, session);
            bizs.add(biz);
        }
        System.out.println("business#: " + bizs.size());
        prepareTableReport(bizs);
    }

    public static void prepareTableReport(List<Business> bizs) {
        JSONArray all_json = new JSONArray();
        for (Business biz : bizs) {
//            if (getValidFormsCount(biz) == 0) {
//                continue;
//            }
//            System.out.println("");
//            System.out.println("[" + biz.getName() + "] " + biz.getUrl());
            JSONObject biz_json = new JSONObject();
            biz_json.put("name", biz.getName());
//            biz_json.put("url", biz.getUrl());
            JSONArray forms_json = new JSONArray();
            for (Object f : biz.getForms()) {
                Form form = (Form) f;
//                if (!form.getPurpose().equals("unknown")) {
//                    continue;
//                }
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
                for (String inftype : Constants.infoType_keywords.keySet()) {
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

    public static void saveBusinessesFromFile(String filename) {
        final List<String> list = Utils.readFileLineByLine(filename);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (String website : list) {
            Business biz = new Business();
            biz.setWebsite(website);
            biz.setDomain(website);
            biz.setSource("QUANTCAST");
            session.save(biz);
        }
        session.getTransaction().commit();
    }

    public static Business getByWebsite(String website, Session session) {
        Criteria criteria = session.createCriteria(Business.class);
        Business biz = (Business) criteria.add(Restrictions.eq("website", website)).uniqueResult();
        return biz;
    }

    public static void updateBusinessRanks(String filename) {
        final List<String> list = Utils.readFileLineByLine(filename);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        int rank = 1;
        for (String website : list) {
            System.out.println("website=" + website);
            Business biz = getByWebsite(website, session);
            if (biz != null) {
                System.out.println("biz=" + biz.getId() + " " + biz.getWebsite());
                session.beginTransaction();
                biz.setRank(rank);
                session.saveOrUpdate(biz);
                System.out.println("Rank for business " + biz.getId() + " updated to " + rank);
                session.getTransaction().commit();
                rank++;
            } else {
                System.out.println("business for website " + website + " is null.");
            }
        }
    }

    public static void main(String[] args) {
        updateBusinessRanks("1000-websites/websites_1000.txt");
//        prepareTableReportByWebsites(websitesForTest);
//        saveBusinessesFromFile("1000-websites/websites_1000.txt");
//        prepareTableReport(100, 100);
//        final List<Business> biz = getBusinesses(0, 100);
//        System.out.println(biz);
    }
}
