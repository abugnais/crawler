package com.github.abugnais.crawler;

public class DifferentDomainException extends RuntimeException {
    public DifferentDomainException(String url) {
        super("url " + url + " has a different domain");
    }
}
