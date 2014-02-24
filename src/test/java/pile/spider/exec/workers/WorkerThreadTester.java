package pile.spider.exec.workers;

import com.google.common.collect.Multimap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pile.spider.service.DetailPageScanner;
import pile.spider.service.ListPageScanner;
import pile.spider.service.OutputProcessor;
import pile.spider.service.PageScannerFactory;
import pile.spider.service.impl.JSoupConnectionFactory;
import pile.spider.service.impl.TechCrunchListPageScannerImpl;
import pile.spider.service.impl.TechCrunchSampleData;

import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class WorkerThreadTester {

    @Test
    public void testWork() throws Exception {
        // Concrete implementation from sample data
        Document listDoc = Jsoup.parse(TechCrunchSampleData.TWO_BLOCKS);
        ListPageScanner listPageScanner = new TechCrunchListPageScannerImpl(listDoc);

        // Fakes
        URL listUrl = new URL("http://example.com");
        JSoupConnectionFactory jSoupConnectionFactory = mock(JSoupConnectionFactory.class);
        Connection connection = mock(Connection.class);
        DetailPageScanner detailPageScanner = mock(DetailPageScanner.class);
        PageScannerFactory pageScannerFactory = mock(PageScannerFactory.class);
        OutputProcessor outputProcessor = mock(OutputProcessor.class);

        // Actions
        when(jSoupConnectionFactory.newConnection(any(URL.class))).thenReturn(connection);
        when(jSoupConnectionFactory.newConnection(any(String.class))).thenReturn(connection);
        when(connection.get()).thenReturn(listDoc);
        when(pageScannerFactory.newDetailPageScanner()).thenReturn(detailPageScanner);
        when(pageScannerFactory.newListPageScanner(null)).thenReturn(listPageScanner);
        when(detailPageScanner.extractDetailRef(any(Element.class))).thenReturn("http://example.com/detail");

        WorkerThread worker = new WorkerThread(listUrl, jSoupConnectionFactory, pageScannerFactory, outputProcessor);
        worker.run();

        // Verify
        ArgumentCaptor<Multimap> multimap1 = ArgumentCaptor.forClass(Multimap.class);
        verify(outputProcessor, times(2)).processOutput(multimap1.capture());
    }
}
