package com.example.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlogService {

    private final PostRepository posts;
    private final PostCommentRepository comments;

    public BlogService(PostRepository posts, PostCommentRepository comments) {
        this.posts = posts;
        this.comments = comments;
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        Post post = posts.save(new Post(request.title(), request.content()));
        return toResponse(post);
    }

    public PostResponse getPost(long postId) {
        return toResponse(findPost(postId));
    }

    public Page<PostSummary> listPosts(Pageable pageable) {
        return posts.findAll(pageable)
                .map(post -> new PostSummary(post.getId(), post.getTitle(), post.getContent()));
    }

    @Transactional
    public PostResponse updatePost(long postId, PostRequest request) {
        Post post = findPost(postId);
        post.update(request.title(), request.content());
        return toResponse(posts.save(post));
    }

    @Transactional
    public void deletePost(long postId) {
        posts.delete(findPost(postId));
    }

    @Transactional
    public CommentResponse addComment(long postId, CommentRequest request) {
        Post post = findPost(postId);
        PostComment comment = post.addComment(request.author(), request.content());
        return toResponse(comments.save(comment));
    }

    public CommentResponse getComment(long postId, long commentId) {
        return toResponse(findComment(postId, commentId));
    }

    @Transactional
    public CommentResponse updateComment(long postId, long commentId, CommentRequest request) {
        PostComment comment = findComment(postId, commentId);
        comment.update(request.author(), request.content());
        return toResponse(comments.save(comment));
    }

    @Transactional
    public void deleteComment(long postId, long commentId) {
        PostComment comment = findComment(postId, commentId);
        comment.getPost().removeComment(comment);
    }

    private Post findPost(long postId) {
        return posts.findById(postId)
                .orElseThrow(() -> new BlogNotFoundException("Post " + postId + " was not found"));
    }

    private PostComment findComment(long postId, long commentId) {
        return comments.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new BlogNotFoundException(
                        "Comment " + commentId + " was not found for post " + postId));
    }

    private static PostResponse toResponse(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(),
                post.getComments().stream().map(BlogService::toResponse).toList());
    }

    private static CommentResponse toResponse(PostComment comment) {
        return new CommentResponse(comment.getId(), comment.getAuthor(), comment.getContent());
    }
}
