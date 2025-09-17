package com.project.documentsearch.model;

public class Document {

    private String id;
    private String title;
    private String content;
    private String path;

    public Document() {
    }

    public Document(String id, String title, String content, String path) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.path = path;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(50, content.length())) + "..." : "") + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}



