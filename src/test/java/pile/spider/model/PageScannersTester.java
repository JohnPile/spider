package pile.spider.model;

import org.junit.Test;
import pile.spider.service.OutputProcessor;
import pile.spider.service.impl.TechCrunchOutputProcessor;
import pile.spider.service.impl.TechCrunchPageScannerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PageScannersTester {

    @Test
    /**
     * This will fail if we ever add an AllowedSite but forget to register its PageScanner.
     */
    public void testAllAllowedSitesRepresented() {

        // First, validate that we'd fail when no AllowedSite is registered.
        PageScannerFactories pageScannerFactories = new PageScannerFactories(null);
        assertNull(pageScannerFactories.getPageScannerFactory(AllowedSite.TechCrunch));

        // Now, register our site and verify the whole list.
        OutputProcessor outputProcessor = new TechCrunchOutputProcessor(System.out);
        TechCrunchPageScannerFactory tcFactory = new TechCrunchPageScannerFactory(outputProcessor);
        pageScannerFactories = new PageScannerFactories(tcFactory);
        for (AllowedSite allowedSite : AllowedSite.values()) {
            assertNotNull(pageScannerFactories.getPageScannerFactory(allowedSite));
        }

    }
}
