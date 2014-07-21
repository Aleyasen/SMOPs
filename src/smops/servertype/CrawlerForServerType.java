/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package smops.servertype;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;
import java.io.File;
import java.util.Set;
import org.apache.http.Header;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import smops.HibernateUtil;

import smops.Utils;
import smops.dao.BusinessManager;
import smops.hibernate.Business;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class CrawlerForServerType extends WebCrawler {

    static String rootFolderName;
    static File rootFolder;
    static String result_file;
    static Set<String> validDomains;

    static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    static Session session = sessionFactory.openSession();

    public static void configure(String rootFolderName, String result_file, Set<String> validDomns) {
        System.out.println("start configuring...");
        CrawlerForServerType.result_file = result_file;
        CrawlerForServerType.rootFolderName = rootFolderName;
        CrawlerForServerType.validDomains = validDomns;
        rootFolder = new File(CrawlerForServerType.rootFolderName);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     *
     * @param url
     * @return
     */
    @Override
    public boolean shouldVisit(WebURL url) {

        String href = url.getURL().toLowerCase();

        if (href.contains(".css") || href.contains(".js")) {
            return false;
        } else {
//            System.out.println("shoudVisit=" + url);
            return true;
        }
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     *
     * @param page
     */
    @Override
    public void visit(Page page) {

//        System.out.println("starting visit");
        String url = page.getWebURL().getURL();
        int docid = page.getWebURL().getDocid();
        final short depth = page.getWebURL().getDepth();
//        System.out.println("depth=" + depth);
        if (depth > 0) {
            return;
        }
        String domain = page.getWebURL().getDomain();
        if (!validDomains.contains(domain)) {
            System.out.println("ignore " + url);
            return;
        }
        String path = page.getWebURL().getPath();
        System.out.println("visit " + url);
//        String subDomain = page.getWebURL().getSubDomain();
//        String parentUrl = page.getWebURL().getParentUrl();

//        String anchor = page.getWebURL().getAnchor();
//        System.out.println("Docid: " + docid);
//        System.out.println("URL: " + url);
//        System.out.println("Domain: '" + domain + "'");
//        System.out.println("Sub-domain: '" + subDomain + "'");
//        System.out.println("Path: '" + path + "'");
//        System.out.println("Parent page: " + parentUrl);
//        System.out.println("Anchor text: " + anchor);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
//            List<WebURL> links = htmlParseData.getOutgoingUrls();

//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();

        if (responseHeaders != null) {
            System.out.println("Response headers:");
            for (Header header : responseHeaders) {
//                System.out.println("\t" + header.getName() + ": " + header.getValue());
                if (header.getName().toLowerCase().equals("server")) {
                    System.out.println("domain=" + domain);
                    Business biz = BusinessManager.getByWebsite(domain, session);
                    if (biz == null) {
                        System.out.println("biz is null!");
                    } else {
                        System.out.println("biz_id=" + biz.getId());
                        session.beginTransaction();
                        biz.setServerType(header.getValue());
                        System.out.println("server type set to " + header.getValue());
                        session.save(biz);
                        session.getTransaction().commit();
                    }
                }
            }
        }
        System.out.println("=============");
    }
}
