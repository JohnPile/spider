package pile.spider.exec.workers;

import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pile.spider.service.DetailPageScanner;
import pile.spider.service.ListPageScanner;
import pile.spider.service.OutputProcessor;
import pile.spider.service.PageScannerFactory;
import pile.spider.service.impl.JSoupConnectionFactory;
import pile.spider.util.exceptions.InconceivableException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class WorkerThread implements Runnable {

    private static final Logger LOGGER= Logger.getLogger(WorkerThread.class);
    private final JSoupConnectionFactory jSoupConnectionFactory;
    private final PageScannerFactory pageScannerFactory;
    private final OutputProcessor outputProcessor;
    private final URL listUrl;

    public WorkerThread(URL listUrl, JSoupConnectionFactory jSoupConnectionFactory, PageScannerFactory pageScannerFactory, OutputProcessor outputProcessor) {
        this.jSoupConnectionFactory = jSoupConnectionFactory;
        this.pageScannerFactory = pageScannerFactory;
        this.outputProcessor = outputProcessor;
        this.listUrl=listUrl;
    }

    @Override
    public void run() {
        ListPageScanner listPageScanner=pageScannerFactory.newListPageScanner(null);
        DetailPageScanner detailPageScanner=null;

        try {
            LOGGER.debug("==== " + listUrl + " ====");
            Connection conn=jSoupConnectionFactory.newConnection(listUrl);
            Document listDoc=conn.get();
            listPageScanner.setDocument(listDoc);
            Elements listItems=listPageScanner.extractPageItems();
            for (Element listItem: listItems) {
                // Retrieve the detail page
                detailPageScanner=pageScannerFactory.newDetailPageScanner();
                String detailUrl=detailPageScanner.extractDetailRef(listItem);

                Document detailDoc=jSoupConnectionFactory.newConnection(detailUrl).get();

                // Extract relevant fields into a multimap
                Multimap<String,String> fieldMap=detailPageScanner.extractFields(detailDoc);

                // Print output
                outputProcessor.processOutput(fieldMap);
            }
            outputProcessor.flush();
        } catch (IOException ex) {
            LOGGER.error("Skipping url: " + listUrl.toString() + " due to " + ex.getMessage());
        }

    }

}
