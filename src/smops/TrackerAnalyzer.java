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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import smops.dao.BusinessManager;
import smops.hibernate.Business;
import smops.hibernate.TrackerBusiness;

/**
 *
 * @author Aale
 */
public class TrackerAnalyzer {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String inputfile_pattern = "1000-websites/trackers/";
        for (int i = 1; i <= 20; i++) {
            String inputfile = inputfile_pattern + i + ".out";
            final Map<String, Set<String>> trackers = analyseTrackerFile(inputfile);
            for (String host : trackers.keySet()) {
                session.beginTransaction();
                Business biz = BusinessManager.getByWebsite(host, session);
                if (biz == null) {
                    System.out.println("business is null!!!! " + "host = " + host);
                } else {
                    for (String trck : trackers.get(host)) {
                        TrackerBusiness trbiz = new TrackerBusiness();
                        trbiz.setBusiness(biz);
                        trbiz.setTrackerUrl(trck);
                        trbiz.setFullUrl("logfile= " + i + ".out");
                        session.save(trbiz);
                    }
                }
                session.getTransaction().commit();
            }
        }
    }

    public static Map<String, Set<String>> analyseTrackerFile(String filepath) {
        System.out.println("analyzing trackers in file = " + filepath);
        Map<String, Set<String>> trackersForEachWebsite = new HashMap<String, Set<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            int line_number = 0;
            while (line != null) {
                String[] split = line.split("\\s+");
                if (split.length == 3) {
                    String host = getDomainName(split[1]);
                    String tracker = getDomainName(split[2]);
                    if (host == null || tracker == null || host.equals(tracker)) {
                        line = br.readLine();
                        line_number++;
                        continue;
                    }
                    if (!trackersForEachWebsite.containsKey(host)) {
                        trackersForEachWebsite.put(host, new HashSet<String>());
                    }
                    trackersForEachWebsite.get(host).add(tracker);
                } else {
                    System.out.println("Split size is not 3. it is " + split.length + " line number=" + line_number);
                }
                line = br.readLine();
                line_number++;
            }
            System.out.println("Number of keys for file=" + filepath + " = " + trackersForEachWebsite.size());
            return trackersForEachWebsite;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (domain == null) {
                System.out.println("domain is null. url = " + url);
                return null;
            }
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            Logger.getLogger(TrackerAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
