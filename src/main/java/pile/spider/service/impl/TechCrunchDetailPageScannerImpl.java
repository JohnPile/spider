package pile.spider.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pile.spider.model.InterpretationMap;
import pile.spider.service.DetailPageScanner;

import javax.inject.Inject;

public class TechCrunchDetailPageScannerImpl implements DetailPageScanner {

    private static final Logger LOGGER=Logger.getLogger(TechCrunchDetailPageScannerImpl.class);
    private Document document;
    private InterpretationMap companyNameInterpretations;
    private InterpretationMap companyWebsiteInterpretations;
    private InterpretationMap articleTitleInterpretations;
    private InterpretationMap articleUrlInterpretations;

    @Inject
    public TechCrunchDetailPageScannerImpl(Document document) {
        this.document=document;
        this.companyNameInterpretations=new InterpretationMap();
        this.companyWebsiteInterpretations=new InterpretationMap();
        this.articleTitleInterpretations=new InterpretationMap();
        this.articleUrlInterpretations=new InterpretationMap();
    }

    @Override
    public Multimap<String, String> extractFields() {
        Multimap<String, String> fields=HashMultimap.create();
        interpretArticleTitle();
        interpretArticleUrl();
        interpretCompanyInfo();
        String companyName=companyNameInterpretations.topInterpretation().getName();
        String companyWebsite=companyWebsiteInterpretations.topInterpretation().getName();
        String articleTitle=articleTitleInterpretations.topInterpretation().getName();
        String articleUrl=articleUrlInterpretations.topInterpretation().getName();
        fields.put("companyName", companyName==null ? "n/a" : companyName);
        fields.put("companyWebsite", companyWebsite==null ? "n/a" : companyWebsite);
        fields.put("articleTitle", articleTitle);
        fields.put("articleUrl", articleUrl);
        return fields;
    }

    private void interpretArticleTitle() {
        Elements titles=document.select("meta[name=sailthru.title]");
        if (titles.size()>0) {
            String articleTitle=titles.first().attr("content");
            articleTitleInterpretations.addInterpretation(articleTitle, 50);
            return;
        }
        titles=document.select("h1.tweet-title");
        if (titles.size()>0) {
            String articleTitle=titles.first().attr("content");
            articleTitleInterpretations.addInterpretation(articleTitle, 50);
        }
    }

    private void interpretArticleUrl() {
        Element canonical=document.select("link[rel=canonical]").first();
        if (canonical!=null) {
            articleUrlInterpretations.addInterpretation(canonical.attr("href"), 100);
            return;
        }
        articleUrlInterpretations.addInterpretation(document.location(), 100);
    }

    private void interpretCompanyInfo() {
        // There doesn't appear to be a 100% reliable solution.
        // So, we'll accumulate a confidence score, and possibly enforce a minimum requirement after studying it.
        Elements tags=document.select("meta[name=sailthru.tags]");
        if (tags.size()>0) {
            // The unproven assumption is that the first tag is the company name.
            String company=tags.first().attr("content").split(",")[0];
            companyNameInterpretations.addInterpretation(company, 20);
        }

        // Find anchor tags in the article and look for matches in the first 4 words of our title.
        String titleWords[]=articleTitleInterpretations.topInterpretation().getName().split("\\W");
        Elements anchors=document.select("div.article-entry a[target=_blank]");
        if (anchors.size()>0) {
            for (Element anchor : anchors) {
                String href=anchor.attr("href");
                String companyWords[]=anchor.text().split("\\W");
                int matchCount=0;
                for (String companyWord : companyWords) {
                    for (int i=0; i<Math.max(4, titleWords.length); i++) {
                        if (companyWord.equals(titleWords[i])) {
                            matchCount++;
                        }
                    }
                }
                if (matchCount>0) {
                    companyNameInterpretations.addInterpretation(anchor.text(), 10*matchCount);
                    companyWebsiteInterpretations.addInterpretation(href, 10*matchCount);
                }
            }
        }


    }

}
