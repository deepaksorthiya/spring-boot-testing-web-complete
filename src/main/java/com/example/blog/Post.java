package com.example.blog;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<PostComment> comments = new ArrayList<>();

    protected Post() {
    }

    public Post(String title, String content) {
        update(title, content);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostComment addComment(String author, String content) {
        PostComment comment = new PostComment(this, author, content);
        comments.add(comment);
        return comment;
    }

    public void removeComment(PostComment comment) {
        if (comments.remove(comment)) {
            comment.detachFromPost();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<PostComment> getComments() {
        return Collections.unmodifiableList(comments);
    }
}
