package com.willian.beanblogger;

public enum NewPostError {
    NoTitle("You need to enter a title"),
    NoAuthor("You need to enter an author"),
    NoContent("You need to add some text content");

    private String summary;

    private NewPostError(String summary) {
        this.summary = summary;
    }

    public String getSummary() { return this.summary; }
}
