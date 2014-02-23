package pile.spider.model;

import org.jsoup.Jsoup;
import org.junit.Test;
import pile.spider.service.impl.TechCrunchListPageScannerImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PageScannersTester {

    @Test
    /**
     * This will fail if we ever add an AllowedSite but forget to register its PageScanner.
     */
    public void testAllAllowedSitesRepresented() {

        // First, validate that we'd fail when no AllowedSite is registered.
        PageScanners pageScanners=new PageScanners(null);
        assertNull(pageScanners.getPageScanner(AllowedSite.TechCrunch));

        // Now, register our site and verify the whole list.
        pageScanners=new PageScanners(new TechCrunchListPageScannerImpl(Jsoup.parse("")));
        for (AllowedSite allowedSite : AllowedSite.values()) {
            assertNotNull(pageScanners.getPageScanner(allowedSite));
        }

    }
}
