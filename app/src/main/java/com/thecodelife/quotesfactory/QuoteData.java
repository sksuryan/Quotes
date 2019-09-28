package com.thecodelife.quotesfactory;

public class QuoteData {
    private String quote,authorName;

    public QuoteData(String quote, String authorName) {
        this.quote = "\"" + quote + "\"";
        this.authorName = authorName;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthorName() {
        return authorName;
    }
}
