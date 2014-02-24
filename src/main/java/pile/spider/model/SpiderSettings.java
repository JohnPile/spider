package pile.spider.model;

public class SpiderSettings {

    private AllowedSite site;
    private int maxSearchPages;
    private int poolSize;

    public SpiderSettings(String[] args, AllowedSite defaultSite, int defaultMaxSearchPages, int defaultPoolSize) {
        site = args.length > 0 ? AllowedSite.valueOf(args[0]) : defaultSite;
        maxSearchPages = args.length > 1 ? Integer.parseInt(args[1]) : defaultMaxSearchPages;
        if (maxSearchPages == 0) {
            maxSearchPages = Integer.MAX_VALUE;
        }
        poolSize = args.length > 2 ? Integer.parseInt(args[2]) : defaultPoolSize;
    }

    public AllowedSite getSite() {
        return site;
    }

    public int getMaxSearchPages() {
        return maxSearchPages;
    }

    public int getPoolSize() {
        return poolSize;
    }
}
