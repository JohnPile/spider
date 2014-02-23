package pile.spider.model;

import com.google.common.base.Objects;

public class Company implements Comparable<Company> {
    private final String name;
    private final String website;

    /**
     * Company metadata
     *
     * @param name    Name of company
     * @param website Company's main website
     */
    public Company(String name, String website) {
        this.name = Objects.firstNonNull(name, "n/a");
        this.website = normalizeWebsite(Objects.firstNonNull(website, "n/a"));
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public int compareTo(Company that) {
        int primary = name.compareTo(that.name);
        return primary == 0 ? website.compareTo(that.website) : primary;
    }

    private String normalizeWebsite(String site) {
        return site.replaceAll(".*//", "").toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Company) {
            Company that = (Company) obj;
            return this.compareTo(that) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash = 7 * hash + name.hashCode();
        hash = 7 * hash + website.hashCode();
        return hash;
    }
}
