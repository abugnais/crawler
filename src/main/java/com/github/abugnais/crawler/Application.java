package com.github.abugnais.crawler;

import picocli.CommandLine;

import java.io.IOException;

public class Application {
    public static void main(String... args) throws IOException {
        final var crawlCommand = new CrawlCommand();
        final var command = new CommandLine(crawlCommand);
        final var exitCode = command.execute(args);
        final var result = command.getExecutionResult();

        System.out.println(result);
        System.exit(exitCode);
    }
}
