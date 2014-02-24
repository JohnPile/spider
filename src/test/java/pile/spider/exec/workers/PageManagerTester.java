package pile.spider.exec.workers;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.junit.Test;
import pile.spider.service.ListPageScanner;
import pile.spider.service.PageScannerFactory;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageManagerTester {

    private static final String FIRST_PAGE="http://example.com";
    private static final String SECOND_PAGE="http://example.com/page/2";

    @Test
    public void testNextValue() throws Exception {
        URL firstPage=new URL("http://example.com");
        URL secondPage=new URL("http://example.com/page/2");
        PageScannerFactory pageScannerFactory=mock(PageScannerFactory.class);
        ListPageScanner listPageScanner=mock(ListPageScanner.class);
        URL startingPage=firstPage;
        Connection startingConnection=mock(Connection.class);
        when(pageScannerFactory.getStartingConnection()).thenReturn(startingConnection);
        when(pageScannerFactory.newListPageScanner(any(Document.class))).thenReturn(listPageScanner);
        when(listPageScanner.nextPage(firstPage)).thenReturn(secondPage);
        PageManager pageManager=new PageManager(pageScannerFactory, startingPage, 0);
        assertEquals(secondPage, pageManager.next());
    }

    @Test
    public void testNextValueMaxPageLimit() throws Exception {
        URL firstPage=new URL("http://example.com");
        URL secondPage=new URL("http://example.com/page/2");
        PageScannerFactory pageScannerFactory=mock(PageScannerFactory.class);
        ListPageScanner listPageScanner=mock(ListPageScanner.class);
        URL startingPage=firstPage;
        Connection startingConnection=mock(Connection.class);
        when(pageScannerFactory.getStartingConnection()).thenReturn(startingConnection);
        when(pageScannerFactory.newListPageScanner(any(Document.class))).thenReturn(listPageScanner);
        when(listPageScanner.nextPage(firstPage)).thenReturn(secondPage);
        try {
            PageManager pageManager=new PageManager(pageScannerFactory, startingPage, 1);
            pageManager.next();
            fail("Expected exception when last page reached.");
        } catch (InterruptedException ex) {
            // expected
        }

    }

}
