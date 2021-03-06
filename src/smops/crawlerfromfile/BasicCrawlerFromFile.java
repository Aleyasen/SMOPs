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
package smops.crawlerfromfile;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import smops.Utils;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawlerFromFile extends WebCrawler {

    static String rootFolderName;
    static File rootFolder;
    static String result_file;
    static Set<String> validDomains;
    static Map<String, Integer> websitesPagesCount;
    static Set<String> overPagesWebsites;
    static int overPagesLimit = 1000;

    public static void configure(String rootFolderName, String result_file, Set<String> validDomns) {
        System.out.println("start configuring...");
        BasicCrawlerFromFile.result_file = result_file;
        BasicCrawlerFromFile.rootFolderName = rootFolderName;
        BasicCrawlerFromFile.validDomains = validDomns;
        BasicCrawlerFromFile.websitesPagesCount = new HashMap<>();
        BasicCrawlerFromFile.overPagesWebsites = new HashSet<>();
        rootFolder = new File(BasicCrawlerFromFile.rootFolderName);
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
        String domain = url.getDomain();
        if (href.contains(".css") || href.contains(".js")) {
            return false;
        }

        if (!validDomains.contains(domain)) {
            System.out.println("ignoreV " + url);
            return false;
        }
        if (overPagesWebsites.contains(domain)) {
            System.out.println("overV " + url);
            return false;
        }

//            System.out.println("shoudVisit=" + url);
        return true;
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
        String domain = page.getWebURL().getDomain();
        if (!validDomains.contains(domain)) {
            System.out.println("ignore " + url);
            return;
        }
        if (overPagesWebsites.contains(domain)) {
            System.out.println("over " + url);
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
            String flat_path = path.replaceAll("/", "-");
            String flat_domain = domain.replaceAll("/", "-");
            flat_path = flat_path.substring(1, flat_path.length());
            final String website_path = rootFolder.getAbsolutePath() + "/" + flat_domain;
            File website_path_file = new File(website_path);
            if (!website_path_file.exists()) {
                website_path_file.mkdirs();
            }
            final String file_path = website_path + "/" + flat_path + "-" + docid + ".html";
            String crawl_info_line = domain + "\t" + url + "\t" + file_path + "\n";
            Utils.writeDataIntoFile(crawl_info_line, result_file);
            IO.writeBytesToFile(html.getBytes(), file_path);
            Integer oldPCount = websitesPagesCount.get(domain);
            if (oldPCount == null) {
                oldPCount = 0;
                websitesPagesCount.put(domain, 0);
            }
            oldPCount++; // now it's new value
            websitesPagesCount.put(domain, oldPCount);
            if (oldPCount > overPagesLimit) {
                overPagesWebsites.add(domain);
            }
        }

//        Header[] responseHeaders = page.getFetchResponseHeaders();
//
//        if (responseHeaders != null) {
//            System.out.println("Response headers:");
//            for (Header header : responseHeaders) {
//                System.out.println("\t" + header.getName() + ": " + header.getValue());
//            }
//        }
//        System.out.println("=============");
    }
}
