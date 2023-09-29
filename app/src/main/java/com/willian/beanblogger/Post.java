package com.willian.beanblogger;

import java.time.Instant;
import java.util.Optional;

import com.github.kkuegler.HumanReadableIdGenerator;

public class Post {

    private final String ID;
    private final String author;
    private final Instant date;
    private String title;
    private String content;
    private Optional<Instant> edit;

    public Post(String title, String author, String content, HumanReadableIdGenerator generator) {
        this.ID = generator.generate();
        this.date = Instant.now();
        this.edit = Optional.empty();
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getId() { return this.ID; }
    public String getAuthor() { return this.author; }
    public Instant getDate() { return this.date; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }
    public Optional<Instant> getEdit() { return this.edit; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) {
        this.edit = Optional.of(Instant.now());
        this.content = content;
    }
}
