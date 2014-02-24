package pile.spider.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import pile.spider.service.OutputProcessor;
import pile.spider.service.impl.TechCrunchOutputProcessor;

import java.io.OutputStream;

public class SpiderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OutputProcessor.class).annotatedWith(Names.named("TechCrunch")).to(TechCrunchOutputProcessor.class);
        bind(OutputStream.class).annotatedWith(Names.named("TechCrunch")).toProvider(OutputStreamProvider.class);
    }


}
