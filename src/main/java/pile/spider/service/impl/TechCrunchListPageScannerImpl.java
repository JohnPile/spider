package pile.spider.service.impl;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pile.spider.service.ListPageScanner;

import java.net.URL;

public class TechCrunchListPageScannerImpl implements ListPageScanner {

    private Document document;

    public TechCrunchListPageScannerImpl(Document document) {
        this.document=document;
    }

    @Override
    /**
     * Provide a set of Elements, each representing an item from a page presenting multiple items.
     */
    public Elements fetchPageItems() {
        return null;
    }

    @Override
    /**
     * Provide the URL navigating to the next page.
     */
    public URL nextPage() {
        return null;
    }

}
