package com.example.batchforcdef.service;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CustomLineTokenizer extends AbstractLineTokenizer {
    private static final Pattern DEFAULT_DELIMITER_PATTERN = Pattern.compile("\\s+");

    private Pattern delimiterPattern = DEFAULT_DELIMITER_PATTERN;

    @Override
    protected List<String> doTokenize(String line) {
        return Arrays.asList(delimiterPattern.split(line.trim()));
    }

    public void setDelimiterPattern(Pattern delimiterPattern) {
        this.delimiterPattern = delimiterPattern;
    }
}

