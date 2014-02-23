package pile.spider.model;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ArticleTester {

    @Test
    public void testConstructorGoodData() throws Exception {
        String title = "title";
        URL url = new URL("http://www.example.com");
        Article article = new Article(title, url);
        assertEquals(title, article.getTitle());
        assertEquals(url, article.getUrl());
    }

    @Test
    public void testConstructorNullChecks() throws Exception {
        try {
            new Article("title", null);
            fail("NPE expected due to null url");
        } catch (NullPointerException ex) {
            // expected
        }
        try {
            new Article(null, new URL("http://www.example.com"));
            fail("NPE expected due to null title");
        } catch (NullPointerException ex) {
            // expected
        }
    }

}
