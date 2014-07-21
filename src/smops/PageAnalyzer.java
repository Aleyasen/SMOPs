/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.io.File;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import smops.dao.BusinessManager;
import smops.dao.Seal;
import smops.hibernate.Business;
import smops.hibernate.Field;
import smops.hibernate.Form;
import smops.hibernate.JsBusiness;
import smops.hibernate.JsLib;
import smops.hibernate.SealBusiness;
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
            analyseForms(doc, current_form_bodies, business, input, url, session);
            return current_form_bodies;
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Set<String> saveJSLibs(String url, String filepath, Business business, Set<String> current_js_libs) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
            analyseJS(doc, current_js_libs, business, input, url, session);
            return current_js_libs;
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return current_js_libs;
    }

    private static void analyseForms(Document doc, Set<String> current_form_bodies, Business business, File input, String url, Session session) throws HibernateException {
        Elements form_elements = doc.select("form");
        System.out.println("#forms in page: " + form_elements.size());
//            System.out.println(biz_website_a.toString());
        if (form_elements != null) {
            for (Element f : form_elements) {
                int numberOfValidInputs = getNumberOfValidInputs(f);
                if (numberOfValidInputs == 0) {
                    System.out.println("number of valid inputs is zero");
                    continue;
                }
//                    session.beginTransaction();
                Form form_obj = new Form();
                String action = f.attr("action");
                String form_html = f.html();
                String form_text = f.text();
                boolean isAccepted = isAcceptedForm(form_obj, action, form_html, form_text, current_form_bodies);
                if (!isAccepted) {
                    System.out.println("form is not accepted!");
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
                System.out.println("form saved for " + url);
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
//                    if (!(type.equals("text") || type.equals("password") || type.equals("radio") || type.equals("checkbox"))) {
//                        continue;
//                    }
                    if (!isAcceptedInputType(type)) {
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
                    System.out.println("field " + name + " saved for " + form_obj.getId());
                    session.getTransaction().commit();

                }
                session.save(form_obj);
//                    session.getTransaction().commit();
                System.out.println("Input#:" + input_count);
            }
        }
    }

    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String crawl_list_file = "1000-websites/crawled-list-1000.txt";
        final List<String> cw_list = Utils.readFileLineByLine(crawl_list_file);
        Map<Integer, List<String>> crawlInfoMap = new HashMap<>();
        for (String cw : cw_list) {
            String[] splits = cw.split("\t", 2);
            Business biz = BusinessManager.getByWebsite(splits[0], session);
            int biz_id = biz.getId();
            System.out.println("biz_id=" + biz_id);
            String pageAndPath = splits[1];
            System.out.println("pageAndPath=" + pageAndPath);
            List<String> list = crawlInfoMap.get(biz_id);
            if (list == null) {
                crawlInfoMap.put(biz_id, new ArrayList<String>());
            }
            crawlInfoMap.get(biz_id).add(pageAndPath);
        }
        int biz_counter = 0;
        for (Integer biz_id : crawlInfoMap.keySet()) {
            System.out.println("start analyzing " + biz_counter + " biz (id=" + biz_id + ")");
            biz_counter++;
            final Business biz = (Business) session.get(Business.class, biz_id);
            Set<String> current_form_bodies = new HashSet<>();
            Set<String> current_js_libs = new HashSet<>();
            Set<String> current_seals = new HashSet<>();
            int html_forms_count = 0;
            int js_libs_count = 0;
            session.beginTransaction();
            biz.setNumPages(crawlInfoMap.get(biz_id).size());
            session.save(biz);
            session.getTransaction().commit();
            for (String others : crawlInfoMap.get(biz_id)) {
                String[] splits = others.split("\t");
                String url = splits[0];
                String file_path = splits[1];
                file_path = file_path.replace("/home/commercenet/smops/SMOPs/crawl-1000-only-valid/", "C:\\Data\\github\\SMOPs\\1000-websites\\crawl-1000\\");
                current_form_bodies = saveFormsAndFields(url, file_path, biz, current_form_bodies);
//                saveImpPagesTypes(url, file_path, biz, session);
//                current_seals = saveSeals(url, file_path, biz, current_seals, session);
//                html_forms_count += getHtmlFormsCount(url, file_path, biz, session);
//                js_libs_count += getJsLibsCount(url, file_path, biz, session);

//                current_js_libs = saveJSLibs(url, file_path, biz, current_js_libs);
            }
//            updateWebsiteType(biz, html_forms_count, js_libs_count, session);
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
        return "UNKNOWN";
    }

    private static String getBestInfoType(Element input) {
        String type = input.attr("type");
        if (isAcceptedInputType(type)) {
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
        return "UNKNOWN";

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

    private static void analyseJS(Document doc, Set<String> current_js_libs, Business business, File input, String url, Session session) {

        Elements js_tags = doc.select("script");
//        System.out.println("url=" + url);
        int count = 0;
        for (Element js_tag : js_tags) {
            String src = js_tag.attr("src");
            count++;
            if (src != null && src.length() > 0) {
                final String jslib = Utils.matchJsLib(src);
                if (current_js_libs.contains(jslib)) {

                } else {
                    JsLib jslibObj = getJsLibObj(jslib, session);
                    if (jslibObj != null) {
                        current_js_libs.add(jslib);
                        JsBusiness jsbiz = new JsBusiness();
                        jsbiz.setBusiness(business);
                        jsbiz.setJsLib(jslibObj);
                        jsbiz.setUrl(src);
                        session.beginTransaction();
                        session.save(jsbiz);
                        session.getTransaction().commit();
                    }
                }

                System.out.println(count + " " + jslib + " - " + src);
            }
        }
        System.out.println();
    }

    private static JsLib getJsLibObj(String jslib, Session session) {
        Criteria criteria = session.createCriteria(JsLib.class);
        JsLib jslibObj = (JsLib) criteria.add(Restrictions.eq("name", jslib)).uniqueResult();
        if (jslibObj == null) {
            JsLib newJslibObj = new JsLib();
            newJslibObj.setName(jslib);
            session.beginTransaction();
            session.save(newJslibObj);
            session.getTransaction().commit();
            return newJslibObj;
        } else {
            return jslibObj;
        }
    }

    private static void saveImpPagesTypes(String url, String filepath, Business business, Session session) {
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
//            System.out.println(doc);
            analyseLinksForImpPages(doc, business, input, url, session);
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void analyseLinksForImpPages(Document doc, Business business, File input, String url, Session session) {
        Elements link_elements = doc.select("a");
        System.out.println("#links in page: " + link_elements.size());
        for (Element link : link_elements) {
            String href = link.attr("href");
            String text = link.text();
            String alt = link.attr("alt");
            String pageType = null;
            if (pageType == null && text != null) {
                pageType = Constants.getImpPage(text);
            }
            if (pageType == null && href != null) {
                pageType = Constants.getImpPage(href);
            }
            if (pageType == null && alt != null) {
                pageType = Constants.getImpPage(alt);
            }
            if (pageType != null) {
                if (pageType.equals("PRIVACY_POLICY")) {
                    if (text != null) {
                        business.setPrivacyPolicyAnchorText(text);
                    }
                    if (url != null) {
                        business.setPrivacyPolicyUrl(href);
                    }
                    business.setHasPrivacyPolicy(Boolean.TRUE);
                    System.out.println("privacy policy for biz " + business.getId() + " updated");
                } else if (pageType.equals("CONTACT_US")) {
                    if (text != null) {
                        business.setContactUsAnchorText(text);
                    }
                    if (url != null) {
                        business.setContactUsUrl(href);
                    }
                    business.setHasContactUs(Boolean.TRUE);
                    System.out.println("contact us for biz " + business.getId() + " updated");
                } else if (pageType.equals("LOGIN")) {
                    if (text != null) {
                        business.setLoginAnchorText(text);
                    }
                    if (url != null) {
                        business.setLoginUrl(href);
                    }
                    business.setSupportLogin(Boolean.TRUE);
                    System.out.println("login for biz " + business.getId() + " updated");
                }
                session.beginTransaction();
                session.save(business);
                session.getTransaction().commit();
            }
            Elements input_elements = doc.select("input");
            for (Element inp : input_elements) {
                String type = inp.attr("type");
                if (type != null) {
                    if (type.toLowerCase().equals("submit")) {
                        String name = inp.attr("name");
                        String value = inp.attr("value");
                        String title = inp.attr("title");
                        String buttonType = null;

                        if (buttonType == null && value != null) {
                            buttonType = Constants.getImpPage(value);
                        }
                        if (buttonType == null && title != null) {
                            buttonType = Constants.getImpPage(title);
                        }
                        if (buttonType == null && name != null) {
                            buttonType = Constants.getImpPage(name);
                        }
                        if (buttonType != null && buttonType.equals("LOGIN")) {
                            String anchor = "SUBMIT INPUT: ";
                            if (value != null) {
                                anchor += "value= " + value + " , ";
                            }
                            if (title != null) {
                                anchor += "title= " + title + " , ";
                            }
                            if (name != null) {
                                anchor += "name= " + name + " , ";
                            }
                            business.setLoginAnchorText(anchor);
                            business.setSupportLogin(Boolean.TRUE);
                            session.beginTransaction();
                            session.save(business);
                            session.getTransaction().commit();

                        } else if (buttonType != null) {
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>The Button Type is not login. it is " + buttonType);
                        }
                    }
                }
            }

        }
//      

    }

    private static Set<String> saveSeals(String url, String filepath, Business business, Set<String> current_seals, Session session) {
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
            analyseSeals(doc, current_seals, business, input, url, session);
            return current_seals;
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return current_seals;
    }

    private static void analyseSeals(Document doc, Set<String> current_seals, Business business, File input, String url, Session session) {
        Elements link_elements = doc.select("a");
        System.out.println("#links in page: " + link_elements.size());
        for (Element link : link_elements) {
            String href = link.attr("href");
            String text = link.text();
            String alt = link.attr("alt");
            if (href != null) {
                String seal = Seal.getSealByURL(href);
                if (seal != null) {
                    addSealForBusiness(seal, href, text, current_seals, business, session);
                }
            }
            if (text != null) {
                String seal = Seal.getSealByAnchorText(text);
                if (seal != null) {
                    addSealForBusiness(seal, href, text, current_seals, business, session);
                }
            }
            if (alt != null) {
                String seal = Seal.getSealByAnchorText(alt);
                if (seal != null) {
                    addSealForBusiness(seal, href, text, current_seals, business, session);
                }
            }
        }
    }

    private static void addSealForBusiness(String seal, String url, String anchorText, Set<String> current_seals, Business business, Session session) {
        if (current_seals.contains(seal)) {
            System.out.println("duplicate seal for the business, ignore adding seal");
            return;
        }
        System.out.println("Seal saved. name=" + seal + " business=" + business.getId());
        session.beginTransaction();
        SealBusiness sealbiz = new SealBusiness();
        sealbiz.setBusiness(business);
        sealbiz.setSeal(seal);
        if (anchorText != null) {
            sealbiz.setSealAnchorText(anchorText);
        }
        if (url != null) {
            sealbiz.setSealUrl(url);
        }
        session.save(sealbiz);
        current_seals.add(seal);
        session.getTransaction().commit();
    }

    private static int getHtmlFormsCount(String url, String filepath, Business business, Session session) {
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
            Elements form_elements = doc.select("form");
            if (form_elements != null) {
                return form_elements.size();
            }
            return 0;
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private static int getJsLibsCount(String url, String filepath, Business business, Session session) {
        try {
            File input = new File(filepath);
            Document doc = Jsoup.parse(input, "UTF-8");
            Elements js_tags = doc.select("script");
            if (js_tags != null) {
                int count = 0;
                for (Element js_tag : js_tags) {
                    String src = js_tag.attr("src");
                    if (src != null) {
                        count++;
                    }
                }
                return count;
            } else {
                return 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private static void updateWebsiteType(Business biz, int html_forms_count, int js_libs_count, Session session) {
        session.beginTransaction();
        if (html_forms_count == 0 && js_libs_count == 0) {
            biz.setWebsiteType("POSTER");
        } else {
            biz.setWebsiteType("OTHER");
        }
        session.save(biz);
        session.getTransaction().commit();
    }

    static List<String> notAcceptedInputType = Arrays.asList("submit", "hidden", "reset", "button", "image");

    private static boolean isAcceptedInputType(String type) {
        if (type == null || type.length() == 0) {
            return false;
        }
        return !notAcceptedInputType.contains(type.toLowerCase());
    }
}
