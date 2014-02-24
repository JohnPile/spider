package pile.spider.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

public interface ListPageScanner {

    Elements extractPageItems();

    URL nextPage(URL currentPage);

    void setDocument(Document document);

}
