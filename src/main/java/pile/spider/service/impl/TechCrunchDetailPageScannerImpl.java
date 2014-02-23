package pile.spider.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jsoup.nodes.Document;
import pile.spider.service.DetailPageScanner;

import javax.inject.Inject;

public class TechCrunchDetailPageScannerImpl implements DetailPageScanner {

    private Document document;

    @Inject
    public TechCrunchDetailPageScannerImpl(Document document) {
        this.document=document;
    }

    @Override
    public Multimap<String, String> extractFields() {
        Multimap<String, String> fields=HashMultimap.create();
        return fields;
    }
}
