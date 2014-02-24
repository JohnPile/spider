package pile.spider.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

public interface PageScannerFactory {

    ListPageScanner newListPageScanner(Document doc);

    DetailPageScanner newDetailPageScanner();

    String getStartingPage();

    Connection getStartingConnection();

    OutputProcessor getOutputProcessor();

}