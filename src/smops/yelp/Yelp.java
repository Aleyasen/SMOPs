package smops.yelp;

/*
 Example code based on code from Nicholas Smith at http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
 For a more complete example (how to integrate with GSON, etc) see the blog post above.
 */
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import smops.HibernateUtil;
import smops.Utils;
import smops.hibernate.Business;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API
     * access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public List<Business> search(String term, String zipcode, int limit, int offset) {
        List<Business> bizresult = new ArrayList<Business>();
        try {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
            request.addQuerystringParameter("term", term);
//        request.addQuerystringParameter("ll", latitude + "," + longitude);
//            request.addQuerystringParameter("bounds", sw_latitude + "," + sw_longitude + "|" + ne_latitude + "," + ne_longitude);
            request.addQuerystringParameter("location", zipcode);
            request.addQuerystringParameter("limit", limit + "");
            request.addQuerystringParameter("offset", offset + "");
            this.service.signRequest(this.accessToken, request);
            Response response = request.send();
            String result = response.getBody();
//            System.out.println("response=" + result);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(result);
            JSONObject jobj = (JSONObject) obj;
            Object bs = jobj.get("businesses");
            if (bs != null) {
                JSONArray jbs = (JSONArray) bs;
                System.out.println(jbs.size());
                for (Object b : jbs) {
                    JSONObject bb = (JSONObject) b;
                    Business biz = new Business();
//                    System.out.println(bb.get("is_claimed"));
                    biz.setYelpId((String) bb.get("id"));
                    biz.setSmops(Boolean.FALSE);
                    biz.setSource("Yelp");
                    biz.setIsClaimed((Boolean) bb.get("is_claimed"));
                    biz.setIsClose((Boolean) bb.get("is_closed"));
                    biz.setName((String) bb.get("name"));
                    biz.setUrl((String) bb.get("url"));
                    biz.setPhone((String) bb.get("phone"));
                    Long review_count = (Long) bb.get("review_count");
                    biz.setReviewCount(review_count);
                    if (bb.get("categories") != null) {
                        biz.setCategories((String) bb.get("categories").toString());
                    }
                    biz.setRating((Double) bb.get("rating"));
                    biz.setSnippetText((String) bb.get("snippet_text"));
                    JSONObject location = (JSONObject) bb.get("location");
                    if (location != null) {
                        if (location.get("address") != null) {
                            biz.setAddress((String) location.get("address").toString());
                        }
                        if (location.get("display_address") != null) {
                            biz.setDisplayAddress((String) location.get("display_address").toString());
                        }
                        biz.setCity((String) location.get("city"));
                        biz.setCountryCode((String) location.get("country_code"));
                        biz.setStateCode((String) location.get("state_code"));
                        if (location.get("neighborhoods") != null) {
                            biz.setNeighborhoods((String) location.get("neighborhoods").toString());
                        }
                        biz.setPostalCode((String) location.get("postal_code"));
                    }
                    bizresult.add(biz);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bizresult;
    }

    // CLI
    final static String CONSUMER_KEY = "consumerKey";
    final static String CONSUMER_SECRET = "consumerSecret";
    final static String TOKEN = "token";
    final static String TOKEN_SECRET = "tokenSecret";

    final static List<Map<String, String>> keys = new ArrayList<Map<String, String>>();

    static {
        Map<String, String> map0 = new HashMap<String, String>();
        map0.put(CONSUMER_KEY, "bUqHveQ5zDVI7j41OGiQKg");
        map0.put(CONSUMER_SECRET, "zTVPyd-9I98vFvRkVmdmma1YDWU");
        map0.put(TOKEN, "-Ol-9hs0AL_OOyvZql1jOOSjE0w2Q2I0");
        map0.put(TOKEN_SECRET, "wN2s5TgFxI0r8WqAR9nvtbo2MsA");
        keys.add(0, map0);
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put(CONSUMER_KEY, "PK1nhXzknZXZuZd2bk3JqQ");
        map1.put(CONSUMER_SECRET, "DSETcYp_JuBQSyeQdcKbdn6UlAg");
        map1.put(TOKEN, "u26wLOoAtZ_83RoiqbXsWOShsANDY20z");
        map1.put(TOKEN_SECRET, "hYApFKotuoZKNJPt38_Uz8VePGo");
        keys.add(1, map1);
    }

    public static void extractBizInfo() {
        // Update tokens here from Yelp developers site, Manage API access.
        int index = 0;
        String consumerKey = keys.get(index).get(CONSUMER_KEY);
        String consumerSecret = keys.get(index).get(CONSUMER_SECRET);
        String token = keys.get(index).get(TOKEN);
        String tokenSecret = keys.get(index).get(TOKEN_SECRET);

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        // +48.987386 is the northern most latitude 
        // +18.005611 is the southern most latitude 
        // -124.626080 is the west most longitude 
        // -62.361014 is a east most longitude 
//        double sw_latitude = +18.005611;
//        double sw_longitude = -124.626080;
//        double ne_latitude = +48.987386;
//        double ne_longitude = -62.361014;

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        List<String> zipcodes = readZipcodeFile("data/zipcodes.txt");
//        zipcodes = Arrays.asList("94062");
        System.out.println("Zipcode#:" + zipcodes.size());
        for (String zpc : zipcodes) {
            System.out.println("zipcode=" + zpc);
            int limit = 20;
            int offset = 0;
            for (;;) {
                List<Business> bresult = yelp.search("", zpc, limit, offset);

                session.beginTransaction();
                for (Business b : bresult) {
                    session.save(b);
                }
                session.getTransaction().commit();
                if (bresult == null || bresult.isEmpty()) {
                    System.out.println("result is empty, break.");
                    break;
                }
                offset += limit;
                System.out.println("zipcode=" + zpc + ", offset=" + offset + ", limit=" + limit + ", count=" + bresult.size());
            }
        }

    }

    public static List<String> readZipcodeFile(String filepath) {
        List<String> zipcodes = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                zipcodes.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return zipcodes;
    }

    public static String getBizWebsite(String yelp_url) {
        try {
            Document doc = Jsoup.connect(yelp_url).get();
//            System.out.println(doc);
            Elements biz_website_a = doc.select("div.biz-website a");
//            System.out.println(biz_website_a.toString());
            if (biz_website_a != null) {
                String url = biz_website_a.attr("href");
                if (url == null || url.length() < 15) {
                    return null;
                }
                url = url.substring(15);
                url = url.split("&src_bizid")[0];
                url = java.net.URLDecoder.decode(url, "UTF-8");
                return url;
            }
        } catch (IOException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void test2() {
        String url = getBizWebsite("http://www.yelp.com/biz/indo-restaurant-and-lounge-palo-alto");
        System.out.println(url);
    }

    public static void updateBusinessWebsites() {
        int count = 0;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        int offset = 0;
        int limit = 100;
        try {
            while (true) {
                Query query = session.createQuery("from Business");
                query.setMaxResults(limit);
                query.setFirstResult(offset);
                List<Business> bizs = (List<Business>) query.list();
                if (bizs == null || bizs.isEmpty()) {
                    break;
                }
                System.out.println("Total Results:" + bizs.size() + " Set=" + count);
                int updated = 0;

                session.beginTransaction();
                for (Business biz : bizs) {
                    if (biz.getUrl() == null || biz.getUrl().length() == 0) {
                        continue;
                    }
                    String website = getBizWebsite(biz.getUrl());
                    if (website != null) {
                        System.out.println(biz.getYelpId() + " " + biz.getUrl() + " " + website);
                        biz.setWebsite(website);
                        biz.setDomain(Utils.getDomain(website));
                        session.save(biz);
                        updated++;
                    }
                }
                session.getTransaction().commit();
                System.out.println("Updated#:" + updated);
                offset += limit;
                count++;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }

    }

    public static void main(String[] args) {
//        test2();
//        extractBizInfo();
        updateBusinessWebsites();
    }
}
