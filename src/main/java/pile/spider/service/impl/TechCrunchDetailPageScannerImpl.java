package pile.spider.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pile.spider.model.InterpretationMap;
import pile.spider.service.DetailPageScanner;

import javax.inject.Inject;
import java.io.IOException;

public class TechCrunchDetailPageScannerImpl implements DetailPageScanner {

    private static final Logger LOGGER=Logger.getLogger(TechCrunchDetailPageScannerImpl.class);

    private class Interpretations {
        public InterpretationMap companyNameInterpretations;
        public InterpretationMap companyWebsiteInterpretations;
        public InterpretationMap articleTitleInterpretations;
        public InterpretationMap articleUrlInterpretations;

        public Interpretations() {
            this.companyNameInterpretations=new InterpretationMap();
            this.companyWebsiteInterpretations=new InterpretationMap();
            this.articleTitleInterpretations=new InterpretationMap();
            this.articleUrlInterpretations=new InterpretationMap();
        }
    }

    public TechCrunchDetailPageScannerImpl() {
    }

    public String extractDetailRef(Element listItem) {
        String articleRef = listItem.attr("data-permalink");
        if (articleRef==null || articleRef.length()==0) {
            articleRef = listItem.select("h2 a").attr("href");
        }
        if (Strings.isNullOrEmpty(articleRef)) {
            throw new IllegalArgumentException("Element listItem contains no recognized reference to detail page.");
        }
        return articleRef;
    }

    @Override
    public Multimap<String, String> extractFields(Document document) {
        Interpretations interpretations=new Interpretations();
        Multimap<String, String> fields=HashMultimap.create();
        interpretArticleTitle(document, interpretations);
        interpretArticleUrl(document, interpretations);
        interpretCompanyInfo(document, interpretations);
        String companyName=interpretations.companyNameInterpretations.topInterpretation().getName();
        String companyWebsite=interpretations.companyWebsiteInterpretations.topInterpretation().getName();
        String articleTitle=interpretations.articleTitleInterpretations.topInterpretation().getName();
        String articleUrl=interpretations.articleUrlInterpretations.topInterpretation().getName();
        fields.put("companyName", companyName==null ? "n/a" : companyName);
        fields.put("companyWebsite", companyWebsite==null ? "n/a" : companyWebsite);
        fields.put("articleTitle", articleTitle);
        fields.put("articleUrl", articleUrl);
        return fields;
    }

    private void interpretArticleTitle(Document document, Interpretations interpretations) {
        Elements titles=document.select("meta[name=sailthru.title]");
        if (titles.size()>0) {
            String articleTitle=titles.first().attr("content");
            interpretations.articleTitleInterpretations.addInterpretation(articleTitle, 50);
            return;
        }
        titles=document.select("h1.tweet-title");
        if (titles.size()>0) {
            String articleTitle=titles.first().attr("content");
            interpretations.articleTitleInterpretations.addInterpretation(articleTitle, 50);
        }
    }

    private void interpretArticleUrl(Document document, Interpretations interpretations) {
        Element canonical=document.select("link[rel=canonical]").first();
        if (canonical!=null) {
            interpretations.articleUrlInterpretations.addInterpretation(canonical.attr("href"), 100);
            return;
        }
        interpretations.articleUrlInterpretations.addInterpretation(document.location(), 100);
    }

    private void interpretCompanyInfo(Document document, Interpretations interpretations) {
        // There doesn't appear to be a 100% reliable solution.
        // So, we'll accumulate a confidence score, and possibly enforce a minimum requirement after studying it.

        // Company name is often in one of the tags
        Elements tags=document.select("meta[name=sailthru.tags]");
        if (tags.size()>0) {
            for (Element tag : tags) {
                String company=tag.attr("content").split(",")[0];
                interpretations.companyNameInterpretations.addInterpretation(company, 5);
            }
        }

        // Company name is often in first four words of title
        String title=document.select("meta[name=sailthru.title]").attr("content");
        if (!Strings.isNullOrEmpty(title)) {
            int i=0;
            for (String word: title.split("\\s+")) {
                if (++i==4) break;
                interpretations.companyNameInterpretations.addInterpretation(word, 10-i);
            }
        }

        // Find anchor tags in the article and look for matches in the first 4 words of our title.
        String articleTitle=interpretations.articleTitleInterpretations.topInterpretation().getName();
        String titleWords[]=articleTitle==null ? new String[0] : articleTitle.split("\\W");
        Elements anchors=document.select("div.article-entry a[target=_blank]");
        if (anchors.size()>0) {
            for (Element anchor : anchors) {
                String href=anchor.attr("href");
                String companyWords[]=anchor.text().split("\\W");
                int matchScore=0;
                int w=0;
                for (String companyWord : companyWords) {
                    w++;
                    for (int i=0; i<Math.min(4, titleWords.length); i++) {
                        if (companyWord.equals(titleWords[i])) {
                            // Prefer earlier word matches
                            matchScore+=Math.max(20, 1+companyWords.length*4)-i*w;
                        }
                    }
                }
                if (matchScore>0) {
                    interpretations.companyNameInterpretations.addInterpretation(anchor.text(), matchScore);
                    interpretations.companyWebsiteInterpretations.addInterpretation(href, matchScore);
                }
            }
        }

        // Company is often first company profiled in right column
        Elements profiles=document.select("ul.crunchbase-accordion strong.key");
        for (Element profile: profiles) {
            if ("Founded".equals(profile.text())) {
                String profileCompany=profile.parent().parent().parent().parent().select("h4.card-title").text();
                interpretations.companyNameInterpretations.addInterpretation(profileCompany, 100);
                Elements keys=profile.parent().parent().select("strong.key");
                for (Element key : keys) {
                    if ("Website".equals(key.text())) {
                        Element websiteElement=key.parent().select("a").first();
                        if (websiteElement!=null) {
                            String href=websiteElement.attr("href");
                            interpretations.companyWebsiteInterpretations.addInterpretation(href, 500);
                        }
                        break;

                    }
                }
                break;
            }
        }

    }

}
