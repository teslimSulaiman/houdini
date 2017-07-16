package com.example.user.houdini.model;

/**
 * Created by USER on 7/16/2017.
 */

public class Info {

    private String title;
    private String id;

    public Info(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
