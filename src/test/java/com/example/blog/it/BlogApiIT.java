package com.example.blog.it;

import com.example.AbstractIntegrationTestBase;
import com.example.blog.CommentRequest;
import com.example.blog.CommentResponse;
import com.example.blog.PostRequest;
import com.example.blog.PostResponse;
import com.example.blog.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests HTTP routing, transactions, DTO mapping, migrations, and PostgreSQL together. */
class BlogApiIT extends AbstractIntegrationTestBase {

    @Autowired
    private PostRepository posts;

    private final List<Long> createdPosts = new ArrayList<>();

    @AfterEach
    void removeTestPosts() {
        for (Long postId : createdPosts) {
            if (posts.existsById(postId)) {
                posts.deleteById(postId);
            }
        }
    }

    @Test
    void postAndCommentCrudRoundTripThroughPostgreSQL() {
        PostResponse post = createPost("Original title");
        String postUri = "/api/posts/" + post.id();
        CommentResponse comment = addComment(post.id(), "Alice", "First comment");
        CommentResponse secondComment = addComment(post.id(), "Bob", "Another comment");
        String commentUri = postUri + "/comments/" + comment.id();

        PostResponse loaded = restTestClient.get().uri(postUri).exchange()
                .expectStatus().isOk().expectBody(PostResponse.class).returnResult().getResponseBody();
        assertThat(loaded).isNotNull();
        assertThat(loaded.comments()).containsExactly(comment, secondComment);

        restTestClient.get().uri(commentUri).exchange().expectStatus().isOk()
                .expectBody(CommentResponse.class).isEqualTo(comment);

        PostResponse updatedPost = restTestClient.put().uri(postUri)
                .body(new PostRequest("Updated title", "Updated content"))
                .exchange().expectStatus().isOk()
                .expectBody(PostResponse.class).returnResult().getResponseBody();
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.title()).isEqualTo("Updated title");
        assertThat(updatedPost.content()).isEqualTo("Updated content");
        assertThat(updatedPost.comments()).containsExactly(comment, secondComment);

        restTestClient.put().uri(commentUri)
                .body(new CommentRequest("Alice Smith", "Edited comment"))
                .exchange().expectStatus().isOk()
                .expectBody(CommentResponse.class)
                .isEqualTo(new CommentResponse(comment.id(), "Alice Smith", "Edited comment"));
        restTestClient.get().uri(commentUri).exchange().expectStatus().isOk()
                .expectBody(CommentResponse.class)
                .isEqualTo(new CommentResponse(comment.id(), "Alice Smith", "Edited comment"));

        restTestClient.get().uri("/api/posts?page=0&size=1").exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content[0].id").isEqualTo(post.id().intValue())
                .jsonPath("$.content[0].title").isEqualTo("Updated title");

        restTestClient.delete().uri(commentUri).exchange().expectStatus().isNoContent();
        restTestClient.get().uri(commentUri).exchange().expectStatus().isNotFound();
        PostResponse afterRemoval = restTestClient.get().uri(postUri).exchange()
                .expectStatus().isOk().expectBody(PostResponse.class).returnResult().getResponseBody();
        assertThat(afterRemoval).isNotNull();
        assertThat(afterRemoval.title()).isEqualTo("Updated title");
        assertThat(afterRemoval.content()).isEqualTo("Updated content");
        assertThat(afterRemoval.comments()).containsExactly(secondComment);

        restTestClient.delete().uri(postUri).exchange().expectStatus().isNoContent();
        restTestClient.get().uri(postUri).exchange().expectStatus().isNotFound();
        restTestClient.get().uri(postUri + "/comments/" + secondComment.id())
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void cannotReadUpdateOrDeleteACommentThroughAnotherPost() {
        PostResponse owner = createPost("Owner");
        PostResponse other = createPost("Other post");
        CommentResponse comment = addComment(owner.id(), "Alice", "Owned comment");
        String wrongUri = "/api/posts/" + other.id() + "/comments/" + comment.id();

        restTestClient.get().uri(wrongUri).exchange().expectStatus().isNotFound();
        restTestClient.put().uri(wrongUri).body(new CommentRequest("Mallory", "Replacement"))
                .exchange().expectStatus().isNotFound();
        restTestClient.delete().uri(wrongUri).exchange().expectStatus().isNotFound();

        restTestClient.get().uri("/api/posts/" + owner.id() + "/comments/" + comment.id())
                .exchange().expectStatus().isOk()
                .expectBody(CommentResponse.class).isEqualTo(comment);
    }

    private PostResponse createPost(String title) {
        var result = restTestClient.post().uri("/api/posts")
                .body(new PostRequest(title, "Post content"))
                .exchange().expectStatus().isCreated()
                .expectBody(PostResponse.class).returnResult();
        PostResponse post = result.getResponseBody();
        assertThat(post).isNotNull();
        assertThat(post.id()).isPositive();
        createdPosts.add(post.id());
        assertThat(post.title()).isEqualTo(title);
        assertThat(post.comments()).isEmpty();
        assertThat(result.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result.getResponseHeaders().getLocation().getPath()).isEqualTo("/api/posts/" + post.id());
        return post;
    }

    private CommentResponse addComment(long postId, String author, String content) {
        var result = restTestClient.post().uri("/api/posts/" + postId + "/comments")
                .body(new CommentRequest(author, content))
                .exchange().expectStatus().isCreated()
                .expectBody(CommentResponse.class).returnResult();
        CommentResponse comment = result.getResponseBody();
        assertThat(comment).isNotNull();
        assertThat(comment.id()).isPositive();
        assertThat(comment.author()).isEqualTo(author);
        assertThat(comment.content()).isEqualTo(content);
        assertThat(result.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result.getResponseHeaders().getLocation().getPath())
                .isEqualTo("/api/posts/" + postId + "/comments/" + comment.id());
        return comment;
    }
}
