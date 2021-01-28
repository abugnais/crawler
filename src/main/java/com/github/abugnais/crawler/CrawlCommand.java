package com.github.abugnais.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import picocli.CommandLine;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class CrawlCommand implements Callable<String> {

    public static Logger log = Logger.getLogger(CrawlCommand.class.getName());

    @CommandLine.Parameters(index = "0", description = "The url to crawl")
    private URL url;

    @Override
    public String call() throws Exception {
        log.info("crawling " + url.toString() + ", this may take some time");

        final var sitemap = new Crawler().crawl(url);
        final var objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        log.info("finished crawling " + url.toString());

        return objectMapper.writeValueAsString(sitemap);
    }
}
