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
        this.db.newPost("Lorem Ipsum", "John Doe", 
            "Lorem ipsum dolor sit amet consectetur adipiscing elit habitasse nascetur arcu orci nisl torquent rutrum aenean nisi primis felis fusce dui. Suscipit hac montes neque duis dignissim sem pharetra sit laoreet eu curabitur vivamus class eleifend. Sagittis porta suspendisse felis turpis vehicula ad habitant dignissim pulvinar himenaeos at consectetur morbi luctus faucibus ultricies euismod volutpat maecenas. Sollicitudin facilisis ligula platea litora maecenas molestie rhoncus fermentum velit porta eu dictumst laoreet donec class potenti etiam bibendum sagittis inceptos dapibus magna pharetra porttitor suscipit aptent convallis nulla lacinia eget.\n" +
            "Fames libero id nunc eu malesuada nisl feugiat quam purus enim quisque porttitor velit dolor augue etiam tempor dictumst neque mattis conubia facilisis ullamcorper scelerisque natoque lacus fusce proin pharetra magnis rhoncus. Ante condimentum mi vel odio class nullam nostra nam taciti vitae nec potenti maecenas sit sodales ligula tincidunt montes pretium eu. Ornare euismod mollis ex augue lacus aliquam habitant mi donec sollicitudin consequat rutrum finibus fames lobortis bibendum leo ullamcorper gravida ac turpis ultricies convallis dictumst erat amet. Nostra lorem semper nisi fringilla ac integer odio dolor fusce sociosqu sollicitudin habitasse lacinia mauris blandit montes imperdiet nunc urna. Aliquam semper rutrum amet nam cursus donec turpis ut interdum convallis felis finibus luctus risus posuere.\n" +
            "Enim massa hendrerit fames faucibus tempor porta mi laoreet habitasse ligula purus rutrum facilisis interdum donec varius fringilla nibh nam eleifend. Lacinia tempor augue quis ut tortor eleifend varius facilisis sagittis pharetra, feugiat habitant magna porttitor iaculis nisl pellentesque egestas maximus praesent habitasse congue nostra elementum luctus nam potenti euismod etiam torquent class. Hendrerit euismod cras egestas tempus congue parturient, ultrices finibus dictumst pharetra eleifend donec elementum sollicitudin lobortis magna nascetur dolor curabitur. Eros ac feugiat ridiculus fringilla fusce adipiscing, massa ad est ornare vitae facilisis parturient molestie leo mauris rutrum lectus a."
        );
    }

    private void setupRoutes() {
        final var engine = new ThymeleafTemplateEngine();

        get("/", (req, res) -> {
            final var posts = this.db.getAllPosts();

            var map = new HashMap<String, Object>();
            map.put("posts", posts);

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
