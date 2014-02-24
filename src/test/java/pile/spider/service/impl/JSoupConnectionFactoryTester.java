package pile.spider.service.impl;

import org.jsoup.Connection;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class JSoupConnectionFactoryTester {

    @Test
    public void testCreationUsingValidSite() {
        JSoupConnectionFactory factory = new JSoupConnectionFactory();
        Connection conn = factory.newConnection("http://127.0.0.1");
        assertNotNull(conn);
    }

    @Test
    public void testCreationUsingNullSite() {
        JSoupConnectionFactory factory = new JSoupConnectionFactory();
        try {
            factory.newConnection("");
            fail("Expected failure from required URL");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testCreationUsingMalformedURL() {
        JSoupConnectionFactory factory = new JSoupConnectionFactory();
        try {
            factory.newConnection("ABC");
            fail("Expected failure from malformed URL");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }


}
