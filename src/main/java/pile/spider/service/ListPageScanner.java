package pile.spider.service;

import org.jsoup.select.Elements;

import java.net.URL;

public interface ListPageScanner {

    Elements fetchPageItems();

    URL nextPage();

}
