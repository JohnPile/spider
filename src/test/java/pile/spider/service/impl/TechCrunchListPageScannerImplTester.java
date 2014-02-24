package pile.spider.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import pile.spider.service.ListPageScanner;

import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TechCrunchListPageScannerImplTester {

    @Test
    public void testNextPageFromFirst() throws Exception {
        Document doc=spy(Jsoup.parse(TechCrunchSampleData.FIRST_PAGE));
        String thisPage="http://techcrunch.com";
        URL thisUrl=new URL(thisPage);
        when(doc.location()).thenReturn(thisPage);
        ListPageScanner listPageScanner=new TechCrunchListPageScannerImpl(doc);
        assertEquals("http://techcrunch.com/page/2", listPageScanner.nextPage(thisUrl).toString());
    }

    @Test
    public void testNextPageFromMiddle() throws Exception {
        Document doc=spy(Jsoup.parse(TechCrunchSampleData.MIDDLE_PAGE));
        String thisPage="http://techcrunch.com/page/100";
        URL thisUrl=new URL(thisPage);
        when(doc.location()).thenReturn(thisPage);
        ListPageScanner listPageScanner=new TechCrunchListPageScannerImpl(doc);
        assertEquals("http://techcrunch.com/page/101", listPageScanner.nextPage(thisUrl).toString());
    }

    @Test
    public void testNextPageFromLast() throws Exception {
        Document doc=spy(Jsoup.parse(TechCrunchSampleData.LAST_PAGE));
        String thisPage="http://techcrunch.com/page/9999";
        URL thisUrl=new URL(thisPage);
        when(doc.location()).thenReturn(thisPage);
        ListPageScanner listPageScanner=new TechCrunchListPageScannerImpl(doc);
        assertEquals(null, listPageScanner.nextPage(thisUrl));
    }

    @Test
    public void testListBreakdown() {
        Document doc=spy(Jsoup.parse(TechCrunchSampleData.TWO_BLOCKS));
        when(doc.location()).thenReturn("http://techcrunch.com");
        ListPageScanner listPageScanner=new TechCrunchListPageScannerImpl(doc);
        Elements articles=listPageScanner.extractPageItems();
        // Exactly two articles
        assertEquals(2, articles.size());
        Iterator<Element> iterator=articles.iterator();
        // First article
        Element article=iterator.next();
        assertFalse(article.toString().contains("STUFF_BEFORE"));
        assertFalse(article.toString().contains("STUFF_AFTER"));
        assertTrue(article.select("p.excerpt").first().text().startsWith("Building hardware"));
        // Second article
        article=iterator.next();
        assertFalse(article.toString().contains("STUFF_BEFORE"));
        assertFalse(article.toString().contains("STUFF_AFTER"));
        assertTrue(article.select("p.excerpt").first().text().startsWith("WhatsApp did"));

    }
}
