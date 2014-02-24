package pile.spider.module;

import javax.inject.Provider;
import java.io.OutputStream;

public class OutputStreamProvider implements Provider<OutputStream> {

    @Override
    public OutputStream get() {
        return System.out;
    }
}
