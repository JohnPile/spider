package pile.spider.exec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import pile.spider.exec.workers.WebRunner;
import pile.spider.model.AllowedSite;
import pile.spider.model.PageScannerFactories;
import pile.spider.model.SpiderSettings;
import pile.spider.module.SpiderModule;
import pile.spider.service.ListPageScanner;
import pile.spider.service.PageScannerFactory;
import pile.spider.service.impl.JSoupConnectionFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

/**
 * Main entry point for command line utility to crawl a site for content.
 */
public class Spider {

    private static final Logger logger = Logger.getLogger(Spider.class);
    private PageScannerFactories pageScannerFactories;
    private JSoupConnectionFactory connectionFactory;

    @Inject
    public Spider(JSoupConnectionFactory connectionFactory, PageScannerFactories pageScannerFactories) {
        this.connectionFactory=connectionFactory;
        this.pageScannerFactories = pageScannerFactories;
    }

    public void crawlWebsite(SpiderSettings spiderSettings) throws IOException {
        PageScannerFactory pageScannerFactory = pageScannerFactories.getPageScannerFactory(spiderSettings.getSite());
        WebRunner webRunner = new WebRunner(connectionFactory, pageScannerFactory, spiderSettings.getMaxSearchPages(), spiderSettings.getPoolSize());
        logger.info("Spider ready to crawl " + spiderSettings.getSite());
        webRunner.crawl();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SpiderModule());
        SpiderSettings spiderSettings = new SpiderSettings(args, AllowedSite.TechCrunch, 100, 20);
        Spider spider = injector.getInstance(Spider.class);
        try {
            spider.crawlWebsite(spiderSettings);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }

    }

}
