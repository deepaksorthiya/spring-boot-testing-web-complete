package com.example.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostClient postClient;

    @InjectMocks
    private PostService postService;

    @Test
    void getPost_ReturnsPost() {
        Post mockPost = new Post(1, 1, "mock title", "mock body");
        when(postClient.getPost(1)).thenReturn(mockPost);

        Post result = postService.getPost(1);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.userId()).isEqualTo(1);
        assertThat(result.title()).isEqualTo("mock title");
        assertThat(result.body()).isEqualTo("mock body");
    }
}
