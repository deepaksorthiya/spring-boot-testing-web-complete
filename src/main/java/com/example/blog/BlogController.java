package com.example.blog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        PostResponse response = service.createPost(request);
        return ResponseEntity.created(URI.create("/api/posts/" + response.id())).body(response);
    }

    @GetMapping
    public PagedModel<PostSummary> listPosts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return new PagedModel<>(service.listPosts(PageRequest.of(page, size, Sort.by("id"))));
    }

    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable @Positive long postId) {
        return service.getPost(postId);
    }

    @PutMapping("/{postId}")
    public PostResponse updatePost(@PathVariable @Positive long postId,
                                   @Valid @RequestBody PostRequest request) {
        return service.updatePost(postId, request);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable @Positive long postId) {
        service.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable @Positive long postId,
                                                     @Valid @RequestBody CommentRequest request) {
        CommentResponse response = service.addComment(postId, request);
        return ResponseEntity.created(URI.create("/api/posts/" + postId + "/comments/" + response.id()))
                .body(response);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public CommentResponse getComment(@PathVariable @Positive long postId,
                                      @PathVariable @Positive long commentId) {
        return service.getComment(postId, commentId);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public CommentResponse updateComment(@PathVariable @Positive long postId,
                                         @PathVariable @Positive long commentId,
                                         @Valid @RequestBody CommentRequest request) {
        return service.updateComment(postId, commentId, request);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable @Positive long postId,
                                              @PathVariable @Positive long commentId) {
        service.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
