package pile.spider.model;

import pile.spider.service.PageScannerFactory;
import pile.spider.service.impl.TechCrunchPageScannerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class PageScannerFactories {

    private Map<AllowedSite, PageScannerFactory> pageScannerFactories;

    @Inject
    public PageScannerFactories(TechCrunchPageScannerFactory techCrunchPageScannerFactory) {
        // Register Page Scanners for all supported sites
        pageScannerFactories = new HashMap<AllowedSite, PageScannerFactory>();
        pageScannerFactories.put(AllowedSite.TechCrunch, techCrunchPageScannerFactory);

    }

    public PageScannerFactory getPageScannerFactory(AllowedSite allowedSite) {
        return pageScannerFactories.get(allowedSite);
    }

}
