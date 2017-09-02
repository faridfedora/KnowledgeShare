package com.example.faridfedora.knowledgeshare;

/**
 * Created by faridfedora on 5/31/17.
 */

public class PostModel {
    private int id;
    private String subject;
    private String tag;

    public PostModel(int id, String subject, String tag) {
        this.id = id;
        this.subject = subject;
        this.tag = tag;
    }


    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getTag() {
        return tag;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
