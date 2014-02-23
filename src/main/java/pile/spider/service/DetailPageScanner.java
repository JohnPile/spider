package pile.spider.service;

import com.google.common.collect.Multimap;

public interface DetailPageScanner {

    // Map of name=value pairs, allowing multiple values
    public Multimap<String,String> extractFields();

}
