package com.example.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(PostClient.class)
class PostClientTest {

    @Autowired
    private PostClient postClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void getPost_ReturnsPost() {
        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/1"))
                .andRespond(withSuccess(
                        """
                                {
                                  "userId": 1,
                                  "id": 1,
                                  "title": "sample title",
                                  "body": "sample body"
                                }
                                """,
                        MediaType.APPLICATION_JSON
                ));

        Post post = this.postClient.getPost(1);

        assertThat(post).isNotNull();
        assertThat(post.id()).isEqualTo(1);
        assertThat(post.userId()).isEqualTo(1);
        assertThat(post.title()).isEqualTo("sample title");
        assertThat(post.body()).isEqualTo("sample body");

        this.server.verify();
    }
}
