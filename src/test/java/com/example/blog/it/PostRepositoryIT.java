package com.example.blog.it;

import com.example.blog.Post;
import com.example.blog.PostComment;
import com.example.blog.PostCommentRepository;
import com.example.blog.PostRepository;
import com.example.support.PostgresTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureDataSourceInitialization;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataSourceInitialization
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryIT extends PostgresTestSupport {

    @Autowired
    private PostRepository posts;

    @Autowired
    private PostCommentRepository comments;

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsBothSidesOfTheRelationshipAcrossReload() {
        Post post = new Post("First post", "Post content");
        post.addComment("Alice", "First comment");
        post.addComment("Bob", "Second comment");

        posts.saveAndFlush(post);
        Long postId = post.getId();
        entityManager.clear();

        Post reloaded = posts.findById(postId).orElseThrow();
        assertThat(reloaded.getTitle()).isEqualTo("First post");
        assertThat(reloaded.getComments()).hasSize(2);
        assertThat(reloaded.getComments()).extracting(PostComment::getAuthor)
                .containsExactly("Alice", "Bob");
        assertThat(reloaded.getComments()).allSatisfy(comment -> {
            assertThat(comment.getId()).isNotNull();
            assertThat(comment.getPost().getId()).isEqualTo(postId);
        });
    }

    @Test
    void removingACommentDeletesTheOrphanWithoutDeletingItsSibling() {
        Post post = new Post("Post", "Content");
        PostComment removed = post.addComment("Alice", "Remove me");
        PostComment retained = post.addComment("Bob", "Keep me");
        posts.saveAndFlush(post);
        Long removedId = removed.getId();
        Long retainedId = retained.getId();
        Long postId = post.getId();
        entityManager.clear();

        Post reloaded = posts.findById(postId).orElseThrow();
        PostComment orphan = reloaded.getComments().stream()
                .filter(comment -> comment.getId().equals(removedId)).findFirst().orElseThrow();
        reloaded.removeComment(orphan);
        posts.flush();
        entityManager.clear();

        assertThat(comments.findById(removedId)).isEmpty();
        assertThat(comments.findById(retainedId)).isPresent();
        assertThat(posts.findById(postId).orElseThrow().getComments())
                .extracting(PostComment::getId).containsExactly(retainedId);
    }

    @Test
    void deletingAPostCascadesToItsCommentsAndLeavesOtherPostsIntact() {
        Post deletedPost = new Post("Delete me", "Content");
        PostComment deletedComment = deletedPost.addComment("Alice", "Delete me too");
        Post retainedPost = new Post("Keep me", "Content");
        PostComment retainedComment = retainedPost.addComment("Bob", "Keep me too");
        posts.saveAndFlush(deletedPost);
        posts.saveAndFlush(retainedPost);
        Long deletedId = deletedPost.getId();
        Long deletedCommentId = deletedComment.getId();
        Long retainedId = retainedPost.getId();
        Long retainedCommentId = retainedComment.getId();
        entityManager.clear();

        posts.deleteById(deletedId);
        posts.flush();
        entityManager.clear();

        assertThat(posts.findById(deletedId)).isEmpty();
        assertThat(comments.findById(deletedCommentId)).isEmpty();
        assertThat(posts.findById(retainedId)).isPresent();
        assertThat(comments.findById(retainedCommentId)).isPresent();
    }
}
