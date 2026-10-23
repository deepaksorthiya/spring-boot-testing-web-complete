package com.example.blog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private PostRepository posts;

    @Mock
    private PostCommentRepository comments;

    @InjectMocks
    private BlogService service;

    @Test
    void creatingPostReturnsThePersistedIdentityAndAnEmptyCommentList() {
        when(posts.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            ReflectionTestUtils.setField(post, "id", 42L);
            return post;
        });

        PostResponse response = service.createPost(new PostRequest("First post", "Post content"));

        assertThat(response).isEqualTo(new PostResponse(42L, "First post", "Post content", List.of()));
        verifyNoInteractions(comments);
    }

    @Test
    void readingPostMapsItsCommentsWithoutExposingTheParentEntity() {
        Post post = postWithId(42L);
        PostComment comment = post.addComment("Ada", "A useful comment");
        ReflectionTestUtils.setField(comment, "id", 7L);
        when(posts.findById(42L)).thenReturn(Optional.of(post));

        PostResponse response = service.getPost(42L);

        assertThat(response).isEqualTo(new PostResponse(42L, "Original title", "Original content",
                List.of(new CommentResponse(7L, "Ada", "A useful comment"))));
    }

    @Test
    void listingPostsPreservesPaginationAndMapsSummaries() {
        PageRequest pageRequest = PageRequest.of(2, 1, Sort.by("id"));
        Post post = postWithId(42L);
        when(posts.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(post), pageRequest, 5));

        Page<PostSummary> result = service.listPosts(pageRequest);

        assertThat(result.getContent()).containsExactly(new PostSummary(42L, "Original title", "Original content"));
        assertThat(result.getNumber()).isEqualTo(2);
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    void updatingPostPreservesItsIdentityAndExistingComments() {
        Post post = postWithId(42L);
        PostComment comment = post.addComment("Ada", "Keep this comment");
        ReflectionTestUtils.setField(comment, "id", 7L);
        when(posts.findById(42L)).thenReturn(Optional.of(post));
        when(posts.save(post)).thenReturn(post);

        PostResponse response = service.updatePost(42L, new PostRequest("Updated title", "Updated content"));

        assertThat(response).isEqualTo(new PostResponse(42L, "Updated title", "Updated content",
                List.of(new CommentResponse(7L, "Ada", "Keep this comment"))));
        assertThat(comment.getPost()).isSameAs(post);
        verifyNoInteractions(comments);
    }

    @Test
    void deletingPostDeletesTheExistingAggregate() {
        Post post = postWithId(42L);
        when(posts.findById(42L)).thenReturn(Optional.of(post));

        service.deletePost(42L);

        verify(posts).delete(post);
    }

    @ParameterizedTest(name = "missing post rejects {0}")
    @ValueSource(strings = {"read", "update", "delete", "add comment"})
    void missingPostIsRejectedWithoutWritingAnything(String operation) {
        when(posts.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            switch (operation) {
                case "read" -> service.getPost(42L);
                case "update" -> service.updatePost(42L, new PostRequest("Updated title", "Updated content"));
                case "delete" -> service.deletePost(42L);
                case "add comment" -> service.addComment(42L, new CommentRequest("Ada", "Comment"));
                default -> throw new AssertionError("Unknown operation: " + operation);
            }
        }).isInstanceOf(BlogNotFoundException.class);

        verify(posts, never()).save(any(Post.class));
        verify(posts, never()).delete(any(Post.class));
        verifyNoInteractions(comments);
    }

    @Test
    void addingCommentAssociatesItWithTheRequestedPostBeforeSaving() {
        Post post = postWithId(42L);
        when(posts.findById(42L)).thenReturn(Optional.of(post));
        when(comments.save(any(PostComment.class))).thenAnswer(invocation -> {
            PostComment comment = invocation.getArgument(0);
            assertThat(comment.getPost()).isSameAs(post);
            assertThat(post.getComments()).containsExactly(comment);
            ReflectionTestUtils.setField(comment, "id", 7L);
            return comment;
        });

        CommentResponse response = service.addComment(42L, new CommentRequest("Ada", "A useful comment"));

        assertThat(response).isEqualTo(new CommentResponse(7L, "Ada", "A useful comment"));
    }

    @Test
    void readingCommentUsesBothItsIdentityAndParentIdentity() {
        Post post = postWithId(42L);
        PostComment comment = post.addComment("Ada", "A useful comment");
        ReflectionTestUtils.setField(comment, "id", 7L);
        when(comments.findByIdAndPostId(7L, 42L)).thenReturn(Optional.of(comment));

        CommentResponse response = service.getComment(42L, 7L);

        assertThat(response).isEqualTo(new CommentResponse(7L, "Ada", "A useful comment"));
        verify(comments).findByIdAndPostId(7L, 42L);
        verifyNoMoreInteractions(comments);
    }

    @Test
    void updatingCommentPreservesItsIdentityAndOwnership() {
        Post post = postWithId(42L);
        PostComment comment = post.addComment("Ada", "Original comment");
        ReflectionTestUtils.setField(comment, "id", 7L);
        when(comments.findByIdAndPostId(7L, 42L)).thenReturn(Optional.of(comment));
        when(comments.save(comment)).thenReturn(comment);

        CommentResponse response = service.updateComment(42L, 7L, new CommentRequest("Grace", "Updated comment"));

        assertThat(response).isEqualTo(new CommentResponse(7L, "Grace", "Updated comment"));
        assertThat(comment.getPost()).isSameAs(post);
        assertThat(post.getComments()).containsExactly(comment);
    }

    @Test
    void deletingCommentRemovesItFromItsParentAndKeepsItsSibling() {
        Post post = postWithId(42L);
        PostComment removed = post.addComment("Ada", "Remove this comment");
        PostComment retained = post.addComment("Grace", "Keep this comment");
        ReflectionTestUtils.setField(removed, "id", 7L);
        when(comments.findByIdAndPostId(7L, 42L)).thenReturn(Optional.of(removed));

        service.deleteComment(42L, 7L);

        assertThat(post.getComments()).containsExactly(retained);
        assertThat(removed.getPost()).isNull();
        assertThat(retained.getPost()).isSameAs(post);
    }

    @ParameterizedTest(name = "missing or wrong-parent comment rejects {0}")
    @ValueSource(strings = {"read", "update", "delete"})
    void commentMustBelongToTheRequestedPost(String operation) {
        when(comments.findByIdAndPostId(7L, 84L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            switch (operation) {
                case "read" -> service.getComment(84L, 7L);
                case "update" -> service.updateComment(84L, 7L, new CommentRequest("Grace", "Updated comment"));
                case "delete" -> service.deleteComment(84L, 7L);
                default -> throw new AssertionError("Unknown operation: " + operation);
            }
        }).isInstanceOf(BlogNotFoundException.class);

        verify(comments).findByIdAndPostId(7L, 84L);
        verifyNoMoreInteractions(comments);
        verifyNoInteractions(posts);
    }

    private static Post postWithId(long id) {
        Post post = new Post("Original title", "Original content");
        ReflectionTestUtils.setField(post, "id", id);
        return post;
    }
}
