package pile.spider.service.impl;

import com.google.common.base.Strings;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pile.spider.service.DetailPageScanner;
import pile.spider.service.ListPageScanner;
import pile.spider.service.OutputProcessor;
import pile.spider.service.PageScannerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TechCrunchPageScannerFactory implements PageScannerFactory {

    private OutputProcessor outputProcessor;

    @Inject
    public TechCrunchPageScannerFactory(@Named("TechCrunch") OutputProcessor outputProcessor) {
        this.outputProcessor=outputProcessor;
    }

    public ListPageScanner newListPageScanner(Document doc) {
        return new TechCrunchListPageScannerImpl(doc);
    }

    public DetailPageScanner newDetailPageScanner() {
        return new TechCrunchDetailPageScannerImpl();
    }

    public String getStartingPage() {
        return "http://techcrunch.com";
    }

    public Connection getStartingConnection() {
        return new JSoupConnectionFactory().newConnection(getStartingPage());
    }

    public OutputProcessor getOutputProcessor() {
        return outputProcessor;
    }

}
