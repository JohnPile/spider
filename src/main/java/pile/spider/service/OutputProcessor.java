package pile.spider.service;

import com.google.common.collect.Multimap;

import java.io.IOException;

public interface OutputProcessor {

    public void processOutput(Multimap<String,String> outputRecord) throws IOException;

    public void flush() throws IOException;

}
