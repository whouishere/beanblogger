package com.willian.beanblogger;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

public class Server {

    private Database db;

    public Server() {
        this.db = new Database();
    }

    public void run() {
        setupRoutes();
    }

    private void setupRoutes() {
        final var engine = new ThymeleafTemplateEngine();

        get("/", (req, res) -> {
            final var posts = this.db.getAllPosts();

            var map = new HashMap<String, Object>();
            map.put("posts", posts.isEmpty() ? null : posts);

            return new ModelAndView(map, "index");
        }, engine);

        get("/new", (req, res) -> new ModelAndView(new HashMap<String, Object>(), "new"), engine);

        post("/new", (req, res) -> {
            final String title = req.queryParams("title");
            final String author = req.queryParams("author");
            final String content = req.queryParams("content");

            final var tuple = authenticatePost(title, author, content);
            if (tuple.x.isEmpty()) {
                final var id = this.db.newPost(title, author, content);
                res.redirect("/" + id);
            }

            var map = new HashMap<String, Object>();
            map.putAll(tuple.x);
            map.putAll(tuple.y);

            return new ModelAndView(map, "new");
        }, engine);

        get("/:post", (req, res) -> {
            final var id = req.params("post");
            final var post = this.db.searchPost(id);

            if (post.isEmpty()) {
                halt(404, "404 - Post not found :(");
            }

            var map = new HashMap<String, Object>();
            map.put("post", post.get());

            return new ModelAndView(map, "post");
        }, engine);
    }

    /**
     * @param title
     * @param author
     * @param content
     * @return tuple with errors and already filled post fields
     */
    private Util.Tuple<Map<String, NewPostError>, Map<String, String>> authenticatePost(String title, String author, String content) {
        var errors = new HashMap<String, NewPostError>();
        var fields = new HashMap<String, String>();

        if (title.isBlank()) {
            errors.put("title_error", NewPostError.NoTitle);
        } else {
            fields.put("title", title);
        }

        if (author.isBlank()) {
            errors.put("author_error", NewPostError.NoAuthor);
        } else {
            fields.put("author", author);
        }

        if (content.isBlank()) {
            errors.put("content_error", NewPostError.NoContent);
        } else {
            fields.put("content", content);
        }

        return new Util.Tuple<Map<String, NewPostError>, Map<String, String>>(errors, fields);
    }
}
