package com.example.blog;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void addingCommentSetsBothSidesOfTheRelationship() {
        Post post = new Post("JPA relationships", "A post with comments");

        PostComment comment = post.addComment("Ada", "Useful example");

        assertThat(post.getComments()).containsExactly(comment);
        assertThat(comment.getPost()).isSameAs(post);
        assertThat(comment.getAuthor()).isEqualTo("Ada");
        assertThat(comment.getContent()).isEqualTo("Useful example");
    }

    @Test
    void removingCommentClearsItsOwnerAndPreservesOtherComments() {
        Post post = new Post("JPA relationships", "A post with comments");
        PostComment removed = post.addComment("Ada", "First comment");
        PostComment retained = post.addComment("Grace", "Second comment");

        post.removeComment(removed);

        assertThat(post.getComments()).containsExactly(retained);
        assertThat(removed.getPost()).isNull();
        assertThat(retained.getPost()).isSameAs(post);
    }
}
