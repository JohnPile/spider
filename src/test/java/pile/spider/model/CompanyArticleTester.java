package pile.spider.model;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CompanyArticleTester {

    @Test
    public void testConstructorGoodData() throws Exception {
        Company company = new Company("Example Corp", "example.com");
        Article article = new Article("random title", new URL("http://example.com/article"));
        CompanyArticle companyArticle = new CompanyArticle(company, article);
        assertEquals(company, companyArticle.getCompany());
        assertEquals(article, companyArticle.getArticle());
    }

    @Test
    public void testConstructorInvalidData() throws Exception {
        Company company = new Company("Company", "example.com");
        Article article = new Article("random", new URL("http://example.com/article"));
        try {
            new CompanyArticle(company, null);
            fail("Expected NPE from missing article");
        } catch (NullPointerException ex) {
            // expected
        }
        try {
            new CompanyArticle(null, article);
            fail("Expected NPE from missing company");
        } catch (NullPointerException ex) {
            // expected
        }
    }
}
