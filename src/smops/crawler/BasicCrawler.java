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
package smops.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import smops.Utils;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawler extends WebCrawler {

    static String currentURL;
    static String rootFolderName;
    static File rootFolder;
    static String domain;
    static String result_file;
    static int biz_id;

    public static void configure(String currentURL, String rootFolderName, int biz_id, String result_file) {
        BasicCrawler.currentURL = currentURL;
        BasicCrawler.result_file = result_file;
        BasicCrawler.biz_id = biz_id;
        domain = Utils.getDomain(currentURL);
        if (domain.startsWith("www.")) {
            domain = domain.substring(4);
        }
        String flat_url = currentURL.substring(5);
        flat_url = flat_url.replaceAll("/", "");
        //TODO: encode other rejected characters
        flat_url = biz_id + "-" + flat_url;
//        System.out.println("flaaaaaaaaaaaaat" + flat_url);
        BasicCrawler.rootFolderName = rootFolderName + "/" + flat_url;
        rootFolder = new File(BasicCrawler.rootFolderName);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
    }

    public static String getCurrentURL() {
        return currentURL;
    }

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|ico|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
//        System.out.println("domain==========" + domain);
        String href = url.getURL().toLowerCase();
//        System.out.println("hre=====" + href);

        if (href.contains(".css") || href.contains(".js")) {
            return false;
        }
        if (href.contains(domain)) {
            return true;
        }
        return false;
//        String href = url.getURL().toLowerCase();
//        System.out.println("shoudVisit=" + href);
//
//        System.out.println("result=" + (!FILTERS.matcher(href).matches()));
//
//        return !FILTERS.matcher(href).matches() && href.contains(domain);
    }

    public static void main(String[] args) {
        Utils.getDomain("http://whitehut.com/");
        Utils.getDomain("http://www.frigofoods.com/");
        Utils.getDomain("http://www.sixflags.com/newEngland/index.aspx");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        System.out.println("Docid: " + docid);
        System.out.println("URL: " + url);
        System.out.println("Domain: '" + domain + "'");
        System.out.println("Sub-domain: '" + subDomain + "'");
        System.out.println("Path: '" + path + "'");
        System.out.println("Parent page: " + parentUrl);
        System.out.println("Anchor text: " + anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
            String flat_path = path.replaceAll("/", "-");
            flat_path = flat_path.substring(1, flat_path.length());
            final String file_path = rootFolder.getAbsolutePath() + "/" + flat_path + "-" + docid + ".html";
            String crawl_info_line = biz_id + "\t" + url + "\t" + file_path + "\n";
            Utils.writeDataIntoFile(crawl_info_line, result_file);
            IO.writeBytesToFile(html.getBytes(), file_path);
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();

        if (responseHeaders != null) {
            System.out.println("Response headers:");
            for (Header header : responseHeaders) {
                System.out.println("\t" + header.getName() + ": " + header.getValue());
            }
        }

        System.out.println("=============");
    }
}
