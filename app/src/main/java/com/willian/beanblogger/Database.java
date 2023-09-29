package com.willian.beanblogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;

public class Database {

    private HashMap<String, Post> db;

    private final HumanReadableIdGenerator generator;

    public Database() {
        this.db = new HashMap<String, Post>();
        this.generator = new PermutationBasedHumanReadableIdGenerator();
    }

    public List<Post> getAllPosts() {
        return new ArrayList<>(this.db.values());
    }

    public Optional<Post> searchPost(String ID) {
        @Nullable var post = db.get(ID);
        return post == null ? Optional.empty() : Optional.of(post);
    }

    public void newPost(String title, String author, String content) {
        var post = new Post(title, author, content, this.generator);
        db.put(post.getId(), post);
    }
}
