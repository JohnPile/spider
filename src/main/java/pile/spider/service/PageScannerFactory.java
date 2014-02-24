package pile.spider.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pile.spider.service.impl.JSoupConnectionFactory;

import java.io.IOException;

public interface PageScannerFactory {

    ListPageScanner newListPageScanner(Document doc);

    DetailPageScanner newDetailPageScanner();

    String getStartingPage();

    Connection getStartingConnection();

    OutputProcessor getOutputProcessor();

}