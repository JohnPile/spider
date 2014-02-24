package pile.spider.exec.workers;

import org.jsoup.nodes.Document;
import pile.spider.service.ListPageScanner;
import pile.spider.service.PageScannerFactory;
import pile.spider.util.exceptions.InconceivableException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hands a sequential list page URL to the next thread
 */
class PageManager {
    private ListPageScanner listPageScanner;
    private URL currentPage;
    private int maxPages;
    private int pageCt;

    public PageManager(PageScannerFactory pageScannerFactory, URL startingPage, int maxPages) throws IOException {
        // Load the page the first time to determine the last page so we know if we reach the end.
        Document listDocument=pageScannerFactory.getStartingConnection().get();
        this.listPageScanner=pageScannerFactory.newListPageScanner(listDocument);
        this.currentPage=startingPage;
        this.maxPages=maxPages<=0 ? Integer.MAX_VALUE : maxPages;
    }

    synchronized URL next() throws InterruptedException {
        if (++pageCt>=maxPages) {
            throw new InterruptedException("User-specified limit of " + maxPages + " pages reached.");
        }
        currentPage=listPageScanner.nextPage(currentPage);
        return currentPage;
    }
}
