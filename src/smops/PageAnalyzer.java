/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import smops.dao.FormPurpose;
import smops.dao.InfoType;
import smops.hibernate.Business;
import smops.hibernate.Field;
import smops.hibernate.Form;
import smops.yelp.Yelp;

/**
 *
 * @author Aale
 */
public class PageAnalyzer {

    public static Set<String> saveFormsAndFields(String url, String filepath, Business business, Set<String> current_form_bodies) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
//            System.out.println(doc);
            Elements form_elements = doc.select("form");
//            System.out.println(biz_website_a.toString());
            if (form_elements != null) {
                for (Element f : form_elements) {
                    int numberOfValidInputs = getNumberOfValidInputs(f);
                    if (numberOfValidInputs == 0) {
                        continue;
                    }
//                    session.beginTransaction();
                    Form form_obj = new Form();
                    String action = f.attr("action");
                    String form_html = f.html();
                    String form_text = f.text();
                    boolean isAccepted = isAcceptedForm(form_obj, action, form_html, form_text, current_form_bodies);
                    if (!isAccepted) {
                        continue;
                    }
                    String title = getBestTitle(f);
                    String purpose = getBestPurpose(f);
                    form_obj.setAction(action);
                    form_obj.setBusiness(business);
                    form_obj.setFileName(input.getName());
                    form_obj.setPageUrl(url);
                    form_obj.setTitle(Utils.truncate(title, 2048));
                    form_obj.setHtml(Utils.truncate(form_html, 65535));
                    form_obj.setPurpose(purpose);
                    session.beginTransaction();
                    session.save(form_obj);

                    session.getTransaction().commit();
                    session.flush();
                    System.out.println("form_obj=" + form_obj.getId());
                    session.refresh(form_obj);
                    System.out.println("form=" + form_obj);
//                    session.getTransaction().commit();

                    final Elements inputs = f.select("input");
                    int input_count = 0;
//                    session.beginTransaction();
                    for (Element i : inputs) {
                        String type = i.attr("type");
                        if (!type.equals("text")) {
                            continue;
                        }
                        input_count++;
                        String name = i.attr("name");
                        String value = i.attr("value");
                        String html = i.html();
                        String label = getBestLabel(i);
                        String infoType = getBestInfoType(i);
                        Field field = new Field();
                        field.setType(type);
                        field.setName(name);
                        field.setValue(Utils.truncate(value, 512));
                        field.setLabel(Utils.truncate(label, 1024));
                        field.setHtml(html);
                        field.setInfoType(infoType);
                        field.setForm(form_obj);
                        form_obj.getFields().add(field);
//                        System.out.println("field=" + field);
                        session.beginTransaction();
                        session.saveOrUpdate(form_obj);
                        session.save(field);
                        session.getTransaction().commit();

                    }
                    session.save(form_obj);
//                    session.getTransaction().commit();
                    System.out.println("Input#:" + input_count);
                }
            }
            return current_form_bodies;
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String crawl_list_file = "crawled-list.txt";
        final List<String> cw_list = Utils.readFileLineByLine(crawl_list_file);
        Map<Integer, List<String>> crawlInfoMap = new HashMap<>();
        for (String cw : cw_list) {
            String[] splits = cw.split("\t", 2);
            int biz_id = Integer.parseInt(splits[0]);
            System.out.println("biz_id=" + biz_id);
            String others = splits[1];
            System.out.println("other=" + others);
            List<String> list = crawlInfoMap.get(biz_id);
            if (list == null) {
                crawlInfoMap.put(biz_id, new ArrayList<String>());
            }
            crawlInfoMap.get(biz_id).add(others);
        }
        for (Integer biz_id : crawlInfoMap.keySet()) {
            final Business biz = (Business) session.get(Business.class, biz_id);
            Set<String> current_form_bodies = new HashSet<>();
            for (String others : crawlInfoMap.get(biz_id)) {
                String[] splits = others.split("\t");
                String url = splits[0];
                String file_path = splits[1];
                current_form_bodies = saveFormsAndFields(url, file_path, biz, current_form_bodies);

            }
        }
    }

    private static boolean checkForBestLabel(String text) {
        if (text != null && text.length() > 0 && Constants.getInfoType(text) != null) {
            return true;
        }
        return false;
    }

    private static String getBestLabel(Element elem) {
        String text = elem.text();
        if (checkForBestLabel(text)) {
            return text;
        }
        Element previousSibling = elem.previousElementSibling();
        if (previousSibling != null) {
            text = previousSibling.text();
            if (checkForBestLabel(text)) {
                return text;
            }
        }
        Element parent = elem.parent();
        if (parent != null) {
            text = parent.text();
            if (checkForBestLabel(text)) {
                return text;
            }
        }
        return "";
    }

    private static String getBestTitle(Element f) {
        return f.text();
    }

    private static int getNumberOfValidInputs(Element form) {
        final Elements inputs = form.select("input");
        int count = 0;
        for (Element inp : inputs) {
            if (isAcceptedInputElement(inp)) {
                count++;
            }
//            String type = inp.attr("type");
//            if (type.equals("text")) {
//                count++;
//            }
        }
        return count;
    }

    private static boolean isAcceptedInputElement(Element input) {
        String type = input.attr("type");
        if (type.equals("text")) {
            String name = input.attr("name");
            if (Constants.getInfoType(name) != null) {
                return true;
            }
            String value = input.attr("value");
            if (Constants.getInfoType(value) != null) {
                return true;
            }
            String label = getBestLabel(input);
            if (Constants.getInfoType(label) != null) {
                return true;
            }
        }
        return false;

    }

    private static String getBestPurpose(Element form) {
        final Element firstChild = form.child(0);
        String text, purpose;
        if (firstChild != null) {
            text = firstChild.text();
            purpose = Constants.getPurpose(text);
            if (purpose != null) {
                return purpose;
            }
        }
        final Element previousElementSibling = form.previousElementSibling();
        if (previousElementSibling != null) {
            text = previousElementSibling.text();
            purpose = Constants.getPurpose(text);
            if (purpose != null) {
                return purpose;
            }
        }
        String formText = form.text();
        if (formText != null && formText.length() < 300) {
            purpose = Constants.getPurpose(formText);
            if (purpose != null) {
                return purpose;
            }
        }
        return FormPurpose.UNKNOWN;
    }

    private static String getBestInfoType(Element input) {
        String type = input.attr("type");
        if (type.equals("text")) {
            String name = input.attr("name");
            String infoType = Constants.getInfoType(name);
            if (infoType != null) {
                return infoType;
            }
            String value = input.attr("value");
            infoType = Constants.getInfoType(value);
            if (infoType != null) {
                return infoType;
            }
            String label = getBestLabel(input);
            infoType = Constants.getInfoType(label);
            if (infoType != null) {
                return infoType;
            }
        }
        return InfoType.UNKNOWN;

    }

    private static boolean isAcceptedForm(Form form_obj, String action, String form_html, String form_text, Set<String> current_form_bodies) {
        if (form_text.length() > 1000) {
            return false;
        }
        if (current_form_bodies.contains(form_text)) {
            return false;
        } else {
            current_form_bodies.add(form_text);
            return true;
        }
    }
}
