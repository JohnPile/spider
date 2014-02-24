package pile.spider.service.impl;

import com.google.common.collect.Multimap;
import pile.spider.service.OutputProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

public class TechCrunchOutputProcessor implements OutputProcessor {

    OutputStreamWriter writer;

    @Inject
    public TechCrunchOutputProcessor(@Named("TechCrunch") OutputStream out) {
        writer = new OutputStreamWriter(new BufferedOutputStream(out), Charset.forName("UTF-8"));
    }

    @Override
    public void processOutput(Multimap<String, String> outputRecord) throws IOException {
        String articleTitle = csvField(outputRecord.get("articleTitle"), null);
        String articleUrl = csvField(outputRecord.get("articleUrl"), null);
        String companyName = csvField(outputRecord.get("companyName"), "n/a");
        String companyWebsite = csvField(outputRecord.get("companyWebsite"), "n/a");
        if (articleTitle != null && articleUrl != null) {
            synchronized (this) {
                writer.append(companyName + "," + companyWebsite + "," + articleTitle + "," + articleUrl + "\n");
            }
        }
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    private String csvField(Collection<String> fields, String defaultField) {
        String field = first(fields, defaultField);
        if (field == null) {
            return null;
        }
        // TODO: data cleansing
        return "\"" + field.replaceAll("\"", "\\\"") + "\"";
    }

    private String first(Collection<String> fields, String defaultField) {
        Iterator<String> it = fields.iterator();
        if (!it.hasNext()) {
            return defaultField;
        }
        return it.next();
    }
}
