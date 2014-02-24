package pile.spider.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URL;

/**
 * This class provides a seam to simplify stubbing of Jsoup static methods.
 */
public class JSoupConnectionFactory {

    public Connection newConnection(String site) {
        return Jsoup.connect(site);
    }

    public Connection newConnection(URL site) {
        return Jsoup.connect(site.toString());
    }
}
