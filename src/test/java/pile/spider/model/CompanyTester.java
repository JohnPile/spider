package pile.spider.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompanyTester {

    @Test
    public void testConstructorGoodData() throws Exception {
        String name = "Example Corp";
        String website = "www.example.com";
        Company company = new Company(name, website);
        assertEquals(name, company.getName());
        assertEquals(website, company.getWebsite());
    }

    @Test
    public void testConstructorNullData() throws Exception {
        Company company = new Company(null, null);
        assertEquals("n/a", company.getName());
        assertEquals("n/a", company.getWebsite());
    }

}