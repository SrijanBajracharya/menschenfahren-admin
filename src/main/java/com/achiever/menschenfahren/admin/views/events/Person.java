package com.achiever.menschenfahren.admin.views.events;

public class Person {

    private String image;
    private String name;
    private String date;
    private String post;
    private String likes;
    private String comments;
    private String shares;

    public Person() {
    }

    public Person(final String name, final String date, final String post, final String likes, final String comments, final String shares) {
        this.name = name;
        this.date = date;
        this.post = post;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
    }

    public Person(final String image, final String name, final String date, final String post, final String likes, final String comments, final String shares) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.post = post;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getPost() {
        return post;
    }

    public void setPost(final String post) {
        this.post = post;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(final String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(final String shares) {
        this.shares = shares;
    }
}