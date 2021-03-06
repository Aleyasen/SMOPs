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

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import smops.Utils;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawlControllerFromFile {

    static String crawlDataIntermediateDir = "crawl-int";

    public static void main(String[] args) {
//
        String rootFolder = "crawl-10000";
        String resultFile = "crawled-list-10000.txt";
        final List<String> websites = Utils.readFileLineByLine("websites_10000.txt");
        Set<String> validWebsites = new HashSet<>(websites);
        int offset = 1300;
        int max = 10000;
        int limit = 100;
        while (true) {
            if (offset == max) {
                break;
            }
            if (offset > max) {
                offset = max;
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>> offset=" + offset + " limit=" + limit);
            crawl(rootFolder, 100, resultFile, validWebsites, offset, limit);
            offset += limit;
        }
    }

    public static void crawl(String rootFolder, int numberOfCrawlers, String result_file, Set<String> validDomains, int offset, int limit) {

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

            config.setFollowRedirects(true);

            /*
             * Be polite: Make sure that we don't send more than 1 request per
             * second (500 milliseconds between requests).
             */
            config.setPolitenessDelay(50);
            
            config.setMaxOutgoingLinksToFollow(100);

            /*
             * You can set the maximum crawl depth here. The default value is -1 for
             * unlimited depth
             */
            config.setMaxDepthOfCrawling(2);

            /*
             * You can set the maximum number of pages to crawl. The default value
             * is -1 for unlimited number of pages
             */
            config.setMaxPagesToFetch(-1);
            


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
            final List<String> all_websites = Utils.readFileLineByLine("websites_10000.txt");
            List<String> websites_for_this_run = new ArrayList<String>();
            for (int i = offset; i < offset + limit; i++) {
                websites_for_this_run.add(all_websites.get(i));
            }
            int counter = 1;
            for (String w : websites_for_this_run) {
                String w_str = "http://www." + w;
                controller.addSeed(w_str);
                System.out.println(counter + " ) added website: " + w_str);
                counter++;
            }

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            BasicCrawlerFromFile.configure(rootFolder, result_file, validDomains);
            controller.start(BasicCrawlerFromFile.class, numberOfCrawlers);
        } catch (Exception ex) {
            Logger.getLogger(BasicCrawlControllerFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
