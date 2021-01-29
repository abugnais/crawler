package com.github.abugnais.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Crawler {

    public SiteMap crawl(URL baseUrl) {
        final var pages  = new HashSet<Page>();
        final var visitedUrls = new HashSet<String>();
        final var urlQueue = new LinkedList<String>();
        final var brokenUrls = new HashSet<String>();
        final var malformedUrls = new HashSet<String>();
        final var externalUrls = new HashSet<String>();

        urlQueue.add(baseUrl.toString());

        while (!urlQueue.isEmpty()) {
            final var currentUrl = urlQueue.poll();

            if (visitedUrls.contains(currentUrl)) {
                continue;
            }

            visitedUrls.add(currentUrl);

            try {
                final var currentPageUrl = new URL(currentUrl);
                final var page = parse(currentPageUrl, baseUrl);

                pages.add(page);
                urlQueue.addAll(page.children);
            } catch (DifferentDomainException exception) {
                  externalUrls.add(currentUrl);
            } catch (MalformedURLException e) {
                malformedUrls.add(currentUrl);
            } catch (Exception e) {
                brokenUrls.add(currentUrl);
            }
        }
        return new SiteMap(baseUrl.toString(), pages, brokenUrls, malformedUrls, externalUrls);
    }

    private Page parse(URL url, URL baseUrl) throws IOException {

        if (isDifferentDomain(url, baseUrl)) {
            throw new DifferentDomainException(url.toString());
        }

        final var document = Jsoup.connect(url.toString()).get();
        final var scripts = getScripts(document);
        final var styles = getStyles(document);
        final var urls = getUrls(document);

        return new Page(url.toString(), styles, scripts, urls);
    }

    private boolean isDifferentDomain(URL currentURL, URL baseURL) {
        final var baseUrlHost = removeWWW(baseURL.getHost());
        final var urlHost = removeWWW(currentURL.getHost());

        return !Objects.equals(urlHost, baseUrlHost);
    }

    private Set<String> getScripts(Document document) {
        return document.select("script[src]").stream()
                .map(it -> it.absUrl("src"))
                .collect(Collectors.toSet());
    }

    private Set<String> getStyles(Document document) {
        return document.select("link[rel=stylesheet][href]").stream()
                .map(it -> it.absUrl("href"))
                .collect(Collectors.toSet());
    }

    private Set<String> getUrls(Document document) {
        return document.select("a[href]").stream()
                .map(it -> it.absUrl("href"))
                .collect(Collectors.toSet());
    }

    private String removeWWW(String host) {
        return host.replaceFirst("^www.", "");
    }
}
