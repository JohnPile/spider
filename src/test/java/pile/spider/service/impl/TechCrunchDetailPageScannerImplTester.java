package pile.spider.service.impl;

import com.google.common.collect.Multimap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import pile.spider.service.DetailPageScanner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TechCrunchDetailPageScannerImplTester {

    @Test
    /**
     * Test that Scanner can pick out company name from simple data fragment.
     */
    public void testCompanyNameGoogle() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_GOOGLE);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        Collection<String> companyNameFields=fields.get("companyName");
        assertTrue(companyNameFields.contains("Google"));
        Collection<String> companyWebsiteFields=fields.get("companyWebsite");
        assertTrue(companyWebsiteFields.contains("n/a"));
    }

    @Test
    /**
     * Test that Scanner can pick out company name from difficult data fragment.
     */
    public void testCompanyNameHyv() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_HYV);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        Collection<String> companyNameFields=fields.get("companyName");
        assertTrue(companyNameFields.contains("Hyv"));
        Collection<String> companyWebsiteFields=fields.get("companyWebsite");
        assertTrue(companyWebsiteFields.contains("hyv.co"));
    }

    @Test
    public void testArticleTitleGoogle() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_GOOGLE);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        String articleTitle=fields.get("articleTitle").toArray(new String[] {})[0];
        assertEquals("The Google Smartwatch Is Real, And It’s Coming Soon (But Maybe Not Too Soon)", articleTitle);
    }

    @Test
    public void testArticleTitleHyv() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_HYV);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        String articleTitle=fields.get("articleTitle").toArray(new String[] {})[0];
        assertEquals("Meet Hyv, A Startup That Can’t Wait For Phone Unlocking To Be Made Legal", articleTitle);
    }

    @Test
    public void testArticleUrlGoogle() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_GOOGLE);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        String articleUrl=fields.get("articleUrl").toArray(new String[] {})[0];
        assertEquals("http://techcrunch.com/2014/02/22/the-google-smartwatch-is-real-and-its-coming-soon-but-maybe-not-too-soon/", articleUrl);
    }

    @Test
    public void testArticleUrlHyv() {
        Document doc= Jsoup.parse(TechCrunchSampleData.DETAIL_PAGE_HYV);
        DetailPageScanner scanner=new TechCrunchDetailPageScannerImpl();
        Multimap<String,String> fields=scanner.extractFields(doc);
        String articleUrl=fields.get("articleUrl").toArray(new String[] {})[0];
        assertEquals("http://techcrunch.com/2014/02/21/meet-hyv-a-startup-that-cant-wait-for-phone-unlocking-to-be-made-legal/", articleUrl);
    }
}
