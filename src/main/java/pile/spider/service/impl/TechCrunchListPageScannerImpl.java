package pile.spider.service.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pile.spider.service.ListPageScanner;
import pile.spider.util.exceptions.InconceivableException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Note: This is not safe for concurrent use.  Make sure each thread gets its own instance.
 */
public class TechCrunchListPageScannerImpl implements ListPageScanner {

    private static final Pattern BASE_URL_PATTERN = Pattern.compile("([^:]+://[^/]+).*");
    private static final Pattern PAGE_NUMBER_PATTERN = Pattern.compile("(.*/page/)([0-9]+)");
    private Integer lastPage=null;
    private Document document;

    public TechCrunchListPageScannerImpl(Document document) {
        this.document=document;
    }

    @Override
    public void setDocument(Document document) {
        this.document=document;
    }

    @Override
    /**
     * Provide a set of Elements, each representing an item from a page presenting multiple items.
     */
    public Elements extractPageItems() {
        return document.select("li.river-block");
    }

    private Integer getLastPage() {
        if (lastPage==null) {
            Element nav=document.select("ol.pagination li.ellipses").last().nextElementSibling();
            lastPage=Integer.valueOf(nav.select("a").first().attr("href").replaceAll(".*/",""));
        }
        return lastPage;
    }

    public synchronized URL nextPage(URL currentPage) {
        Matcher m = PAGE_NUMBER_PATTERN.matcher(currentPage.toString());
        if (m.find()) {
            String currPageNo=m.group(2);
            Integer nextPage=Integer.valueOf(Integer.parseInt(currPageNo)+1);
            if (nextPage.compareTo(getLastPage())>0) {
                // No more pages
                return null;
            }
            try {
                return new URL("http://techcrunch.com/page/" + nextPage);
            } catch (MalformedURLException ex) {
                throw new InconceivableException("Too simple to break.", ex);
            }
        }
        if ("http://techcrunch.com".equals(currentPage.toString())) {
            try {
                return new URL("http://techcrunch.com/page/2");
            } catch (MalformedURLException ex) {
                throw new InconceivableException("Why would a hard-coded URL become malformed?", ex);
            }
        }
        throw new IllegalStateException("Unrecognized page: " + currentPage.toString());
    }

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
