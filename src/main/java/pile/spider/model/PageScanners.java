package pile.spider.model;

import pile.spider.service.ListPageScanner;
import pile.spider.service.impl.TechCrunchListPageScannerImpl;
import pile.spider.util.exceptions.InconceivableException;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PageScanners {

    public static final String TECHCRUNCH_SITE="http://techcrunch.com";

    private Map<AllowedSite, ListPageScanner> pageScanners;
    private Map<AllowedSite, URL> startingPages;

    @Inject
    public PageScanners(TechCrunchListPageScannerImpl techCrunchPageScanner) {
        // Register Page Scanners for all supported sites
        pageScanners = new HashMap<AllowedSite, ListPageScanner>();
        pageScanners.put(AllowedSite.TechCrunch, techCrunchPageScanner);

        // Register Starting Pages for all supported sites
        startingPages = new HashMap<AllowedSite, URL>();
        startingPages.put(AllowedSite.TechCrunch, url(TECHCRUNCH_SITE));
    }

    private URL url(String site) {
        try {
            return new URL(site);
        } catch (MalformedURLException e) {
            throw new InconceivableException("This should never happen for a pre-screened URL", e);
        }
    }

    public ListPageScanner getPageScanner(AllowedSite allowedSite) {
        return pageScanners.get(allowedSite);
    }

    public URL getStartingPage(AllowedSite allowedSite) {
        return startingPages.get(allowedSite);
    }

}
