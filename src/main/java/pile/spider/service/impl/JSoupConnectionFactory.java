package pile.spider.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * This class provides a seam to simplify stubbing of Jsoup static methods.
 */
public class JSoupConnectionFactory {

    public Connection newConnection(String site) {
        return Jsoup.connect(site);
    }
}
