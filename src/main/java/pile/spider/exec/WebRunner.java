package pile.spider.exec;

import org.apache.log4j.Logger;
import pile.spider.service.ListPageScanner;

import java.io.OutputStream;
import java.net.URL;

public class WebRunner {

    private static final Logger logger=Logger.getLogger(WebRunner.class);

    private ListPageScanner listPageScanner;
    private int poolSize;
    private OutputStream out;

    public WebRunner(ListPageScanner listPageScanner, int poolSize, OutputStream out) {
        this.listPageScanner = listPageScanner;
        this.poolSize=poolSize;
        this.out=out;
    }

    public void crawl(URL startingPage) {

    }
}
