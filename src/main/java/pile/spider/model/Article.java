package pile.spider.model;

import com.google.common.base.Preconditions;

import java.net.URL;

public class Article implements Comparable<Article> {

    private final String title;
    private final URL url;

    /**
     * Article metadata
     *
     * @param title <em>Required</em> Title of an article
     * @param url   <em>Required</em> URL where article was found
     */
    public Article(String title, URL url) {
        Preconditions.checkNotNull(title, "Title is required.");
        Preconditions.checkNotNull(url, "URL is required.");
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public URL getUrl() {
        return url;
    }

    public int compareTo(Article that) {
        int primary = title.compareTo(that.title);
        return primary == 0 ? url.toString().compareTo(that.url.toString()) : primary;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Article) {
            Article that = (Article) obj;
            return this.compareTo(that) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash = 7 * hash + title.hashCode();
        hash = 7 * hash + url.hashCode();
        return hash;
    }
}
