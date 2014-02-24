package pile.spider.exec.workers;

import org.apache.log4j.Logger;
import pile.spider.service.OutputProcessor;
import pile.spider.service.PageScannerFactory;
import pile.spider.service.impl.JSoupConnectionFactory;
import pile.spider.util.exceptions.InconceivableException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

public class WebRunner {

    private static final Logger LOGGER = Logger.getLogger(WebRunner.class);

    private final JSoupConnectionFactory connectionFactory;
    private final PageScannerFactory pageScannerFactory;
    private final int maxPages;
    private final int poolSize;

    public WebRunner(JSoupConnectionFactory connectionFactory, PageScannerFactory pageScannerFactory, int maxPages, int poolSize) {
        this.connectionFactory = connectionFactory;
        this.pageScannerFactory = pageScannerFactory;
        this.maxPages = maxPages;
        this.poolSize = poolSize;
    }

    public void crawl() throws IOException {
        URL startingPage;
        try {
            startingPage = new URL(pageScannerFactory.getStartingPage());
        } catch (MalformedURLException ex) {
            throw new InconceivableException("Hard-coded valid URL.", ex);
        }
        PageManager pageManager = new PageManager(pageScannerFactory, startingPage, maxPages);
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        int corePoolSize = poolSize / 2;
        int maxPoolSize = poolSize;
        long keepAlive = 10;
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(corePoolSize), threadFactory, rejectionHandler);
        // Each thread processes one web page at a time
        OutputProcessor outputProcessor = pageScannerFactory.getOutputProcessor();
        try {
            URL listUrl = pageManager.next();
            while (listUrl != null) {
                LOGGER.debug("Assigning page: " + listUrl);
                executorPool.execute(new WorkerThread(listUrl, connectionFactory, pageScannerFactory, outputProcessor));
                listUrl = pageManager.next();
            }
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            // wakey wakey, eggs and bakey
        }
        //shut down the pool
        executorPool.shutdown();
    }

    private class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // log
        }
    }

}
