package ru.itmo.web.lesson4.model;

public class Post {
    private final long id;
    private final String title;
    private final String text;
    private final long user_id;
    private final long likes;
    private final long time;
    private final long comments;


    public Post(long id, String title, String text, long user_id, long likes, long time, long comments) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.user_id = user_id;
        this.likes = likes;
        this.time = time;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public long getUser_id() {
        return user_id;
    }

    public long getLikes() {
        return likes;
    }

    public long getTime() {
        return time;
    }

    public long getComments() {
        return comments;
    }
}
