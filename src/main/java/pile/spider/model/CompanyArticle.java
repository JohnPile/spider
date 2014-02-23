package pile.spider.model;

import com.google.common.base.Preconditions;

public class CompanyArticle {
    private final Article article;
    private final Company company;

    public CompanyArticle(Company company, Article article) {
        Preconditions.checkNotNull(company, "Company is a required field.");
        Preconditions.checkNotNull(article, "Article is a required field.");
        this.company = company;
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    public Company getCompany() {
        return company;
    }

}
