package com.github.abugnais.crawler;

import java.util.Objects;
import java.util.Set;

public class SiteMap {
    private final String baseUrl;
    private final Set<Page> pages;
    private final Set<String> broken;
    private final Set<String> malformed;
    private final Set<String> externalUrls;

    public SiteMap(String baseUrl, Set<Page> pages, Set<String> broken, Set<String> malformed, Set<String> externalUrls) {
        this.baseUrl = baseUrl;
        this.pages = pages;
        this.broken = broken;
        this.malformed = malformed;
        this.externalUrls = externalUrls;
    }

    public Set<String> getBroken() {
        return broken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Set<Page> getPages() {
        return pages;
    }

    public Set<String> getMalformed() {
        return malformed;
    }

    public Set<String> getExternalUrls() { return externalUrls; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteMap)) return false;
        SiteMap siteMap = (SiteMap) o;
        return  Objects.equals(getBaseUrl(), siteMap.getBaseUrl()) &&
                Objects.equals(getPages(), siteMap.getPages()) &&
                Objects.equals(getBroken(), siteMap.getBroken()) &&
                Objects.equals(getMalformed(), siteMap.getMalformed()) &&
                Objects.equals(getExternalUrls(), siteMap.getExternalUrls());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseUrl(), getPages(), getBroken(), getMalformed(), getExternalUrls());
    }
}
