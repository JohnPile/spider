package pile.spider.service;

import com.google.common.collect.Multimap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface DetailPageScanner {

    /**
     * Extracts a URL ref to the detail document from an isolated section of the list document
     *
     * @param listItem Element containing a single item from the list document
     * @return reference to the detail document
     */
    public String extractDetailRef(Element listItem);

    /**
     * Multimap of name=value pairs, allowing multiple values
     *
     * @param detailDocument Detail page document
     * @return Multimap of name=value pairs, allowing multiple values
     */
    public Multimap<String, String> extractFields(Document detailDocument);

}
