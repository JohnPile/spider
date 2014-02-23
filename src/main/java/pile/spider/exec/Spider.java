package pile.spider.exec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import pile.spider.model.AllowedSite;
import pile.spider.model.PageScanners;
import pile.spider.model.SpiderSettings;
import pile.spider.module.SpiderModule;
import pile.spider.service.ListPageScanner;

import javax.inject.Inject;
import java.net.URL;

/**
 * Main entry point for command line utility to crawl a site for content.
 */
public class Spider {

    private static final Logger logger = Logger.getLogger(Spider.class);
    private PageScanners pageScanners;

    @Inject
    public Spider(PageScanners pageScanners) {
        this.pageScanners = pageScanners;
    }

    public void crawlWebsite(SpiderSettings spiderSettings) {
        ListPageScanner listPageScanner = pageScanners.getPageScanner(spiderSettings.getSite());
        URL startingPage = pageScanners.getStartingPage(spiderSettings.getSite());
        WebRunner webRunner = new WebRunner(listPageScanner, spiderSettings.getPoolSize(), System.out);
        logger.info("Spider ready to crawl " + startingPage.toString());
        webRunner.crawl(startingPage);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SpiderModule());
        SpiderSettings spiderSettings = new SpiderSettings(args, AllowedSite.TechCrunch, 10, 10);
        Spider spider = injector.getInstance(Spider.class);
        spider.crawlWebsite(spiderSettings);
    }

}
