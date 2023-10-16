package com.willian.beanblogger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.kkuegler.HumanReadableIdGenerator;

public class Post {

    private final String ID;
    private final String author;
    private final PostTime instant;
    private String title;
    private String content;
    private Optional<PostTime> edit;

    public Post(String title, String author, String content, HumanReadableIdGenerator generator) {
        this.ID = generator.generate();
        this.instant = new PostTime();
        this.edit = Optional.empty();
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public List<String> splitContentParagraphs() {
        return Arrays.asList(this.content.split("[\\r\\n]+"));
    }

    public String getId() { return this.ID; }
    public String getAuthor() { return this.author; }
    public PostTime getInstant() { return this.instant; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }
    public Optional<PostTime> getEdit() { return this.edit; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) {
        this.edit = Optional.of(new PostTime());
        this.content = content;
    }
}
