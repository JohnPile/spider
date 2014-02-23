package pile.spider.service.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pile.spider.service.ListPageScanner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechCrunchListPageScannerImpl implements ListPageScanner {

    private static final Pattern BASE_URL_PATTERN = Pattern.compile("([^:]+://[^/]+).*");
    private static final Pattern PAGE_NUMBER_PATTERN = Pattern.compile("(.*/)([0-9]+)");
    private Document document;


    public TechCrunchListPageScannerImpl(Document document) {
        this.document=document;
    }

    @Override
    /**
     * Provide a set of Elements, each representing an item from a page presenting multiple items.
     */
    public Elements fetchPageItems() {
        return document.select("li.river-block");
    }

    @Override
    /**
     * Provide the URL navigating to the next page.
     */
    public URL nextPage() {
        URL url=null;
        Element nextPage=document.select("ol.pagination li").last();
        if (nextPage!=null) nextPage=nextPage.select("a").first();
        if (nextPage!=null && nextPage.text().startsWith("Next")) {
            String ref=nextPage.attr("href");
            if (ref.startsWith("/")) {
                ref=ref.substring(1);
            }
            String baseUrl="";
            if (ref!=null && ref.length()>0) {
                if (!ref.startsWith("http") && document.location()!=null) {
                    Matcher m = BASE_URL_PATTERN.matcher(document.location());
                    if (m.find()) {
                        baseUrl=m.group(1);
                    }
                    try {
                        url=new URL(baseUrl + "/" + ref);
                    } catch (MalformedURLException ex) {
                        url=null;
                    }
                } else if (ref.startsWith("http")) {
                    try {
                        url=new URL(ref);
                    } catch (MalformedURLException ex) {
                        url=null;
                    }
                }
            }
        }
        if (url==null && document.location()!=null) {
            Matcher m = PAGE_NUMBER_PATTERN.matcher(document.location());
            if (m.find()) {
                String baseUrl=m.group(1);
                int pageno=Integer.parseInt(m.group(1))+1;
                try {
                    url=new URL(baseUrl + pageno);
                } catch (MalformedURLException e) {
                    url=null;
                }
            }
        }
        return url;
    }

}
