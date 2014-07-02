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

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import smops.hibernate.Business;
import smops.dao.BusinessManager;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawlController {

    static String crawlDataIntermediateDir = "crawl-int";

    public static void main2(String[] args) {
//
        String rootFolder = "crawl-temp";
        String resultFile = "crawled-list-temp.txt";
        crawl(rootFolder, 1, "http://longmeadowlibrary.wordpress.com", 197, resultFile);
    }

    public static void main(String[] args) {
        String rootFolder = "crawl";
        String resultFile = "crawled-list.txt";
        final List<Business> bizlist = BusinessManager.getBusinesses(0, 100);
        for (Business biz : bizlist) {
            if (biz.getWebsite() != null && biz.getWebsite().length() > 0) {
                System.out.println("start crawling = " + biz.getWebsite());
                crawl(rootFolder, 1, biz.getWebsite(), biz.getId(), resultFile);
                System.out.println("crawling " + biz.getWebsite() + " done.");
//                BasicCrawler.getDomain(biz.getWebsite());
            }
        }
//        String url = "http://www.frigofoods.com/";
//        crawl(rootFolder, 2, url);
//        crawl(rootFolder, 2, "http://whitehut.com/");
//        crawl(rootFolder, 2, "http://mainstreet-deli.com/");
//        crawl(rootFolder, 2, "http://www.sixflags.com/newengland");
    }

    public static void crawl(String rootFolder, int numberOfCrawlers, String url, int biz_id, String result_file) {

        try {
            /*
             * crawlStorageFolder is a folder where intermediate crawl data is
             * stored.
             */

            /*
             * numberOfCrawlers shows the number of concurrent threads that should
             * be initiated for crawling.
             */
            CrawlConfig config = new CrawlConfig();

            config.setCrawlStorageFolder(crawlDataIntermediateDir);

            /*
             * Be polite: Make sure that we don't send more than 1 request per
             * second (500 milliseconds between requests).
             */
            config.setPolitenessDelay(100);

            /*
             * You can set the maximum crawl depth here. The default value is -1 for
             * unlimited depth
             */
            config.setMaxDepthOfCrawling(1);

            /*
             * You can set the maximum number of pages to crawl. The default value
             * is -1 for unlimited number of pages
             */
            config.setMaxPagesToFetch(10000);

            /*
             * Do you need to set a proxy? If so, you can use:
             * config.setProxyHost("proxyserver.example.com");
             * config.setProxyPort(8080);
             *
             * If your proxy also needs authentication:
             * config.setProxyUsername(username); config.getProxyPassword(password);
             */
            /*
             * This config parameter can be used to set your crawl to be resumable
             * (meaning that you can resume the crawl from a previously
             * interrupted/crashed crawl). Note: if you enable resuming feature and
             * want to start a fresh crawl, you need to delete the contents of
             * rootFolder manually.
             */
            config.setResumableCrawling(false);

            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            controller.addSeed(url);
            System.out.println("url====" + url);

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            BasicCrawler.configure(url, rootFolder, biz_id, result_file);
            controller.start(BasicCrawler.class, numberOfCrawlers);
        } catch (Exception ex) {
            Logger.getLogger(BasicCrawlController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
