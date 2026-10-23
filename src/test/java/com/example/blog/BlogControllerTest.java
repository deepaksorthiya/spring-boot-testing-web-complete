package com.example.blog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BlogService service;

    @Test
    void createsPostWithLocationAndRepresentation() throws Exception {
        when(service.createPost(new PostRequest("First post", "Post content")))
                .thenReturn(new PostResponse(41L, "First post", "Post content", List.of()));

        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"First post","content":"Post content"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/posts/41"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(41))
                .andExpect(jsonPath("$.title").value("First post"))
                .andExpect(jsonPath("$.content").value("Post content"))
                .andExpect(jsonPath("$.comments").isEmpty());
    }

    @Test
    void returnsCommentsWithoutSerializingTheParentBackReference() throws Exception {
        when(service.getPost(41L)).thenReturn(new PostResponse(41L, "Title", "Body",
                List.of(new CommentResponse(72L, "Ada", "Helpful"))));

        mvc.perform(get("/api/posts/41"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].id").value(72))
                .andExpect(jsonPath("$.comments[0].author").value("Ada"))
                .andExpect(jsonPath("$.comments[0].content").value("Helpful"))
                .andExpect(jsonPath("$.comments[0].post").doesNotExist());
    }

    @Test
    void listsPostSummariesWithStablePagination() throws Exception {
        var pageable = PageRequest.of(1, 2, Sort.by("id").ascending());
        when(service.listPosts(pageable)).thenReturn(new PageImpl<>(
                List.of(new PostSummary(41L, "Title", "Body")), pageable, 3));

        mvc.perform(get("/api/posts").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(41))
                .andExpect(jsonPath("$.content[0].comments").doesNotExist())
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(3));
    }

    @Test
    void updatesPost() throws Exception {
        when(service.updatePost(41L, new PostRequest("Updated", "New content")))
                .thenReturn(new PostResponse(41L, "Updated", "New content", List.of()));

        mvc.perform(put("/api/posts/41").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Updated","content":"New content"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(41))
                .andExpect(jsonPath("$.content").value("New content"));
    }

    @Test
    void deletesPostWithNoResponseBody() throws Exception {
        mvc.perform(delete("/api/posts/41"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(service).deletePost(41L);
    }

    @Test
    void createsCommentWithinItsPost() throws Exception {
        when(service.addComment(41L, new CommentRequest("Ada", "Helpful")))
                .thenReturn(new CommentResponse(72L, "Ada", "Helpful"));

        mvc.perform(post("/api/posts/41/comments").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"author":"Ada","content":"Helpful"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/posts/41/comments/72"))
                .andExpect(jsonPath("$.id").value(72))
                .andExpect(jsonPath("$.author").value("Ada"));
    }

    @Test
    void retrievesCommentWithinItsPost() throws Exception {
        when(service.getComment(41L, 72L)).thenReturn(new CommentResponse(72L, "Ada", "Helpful"));
        mvc.perform(get("/api/posts/41/comments/72"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(72))
                .andExpect(jsonPath("$.content").value("Helpful"));
    }

    @Test
    void updatesCommentWithinItsPost() throws Exception {
        when(service.updateComment(41L, 72L, new CommentRequest("Ada", "Edited")))
                .thenReturn(new CommentResponse(72L, "Ada", "Edited"));
        mvc.perform(put("/api/posts/41/comments/72").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"author":"Ada","content":"Edited"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Edited"));
    }

    @Test
    void deletesCommentWithinItsPost() throws Exception {
        mvc.perform(delete("/api/posts/41/comments/72"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(service).deleteComment(41L, 72L);
    }

    @Test
    void reportsMissingPostAsProblemDetail() throws Exception {
        when(service.getPost(41L)).thenThrow(new BlogNotFoundException("Post 41 was not found"));
        mvc.perform(get("/api/posts/41"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Post 41 was not found"));
    }

    @ParameterizedTest
    @MethodSource("invalidPosts")
    void rejectsInvalidPostsBeforeCallingService(String body) throws Exception {
        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    static Stream<String> invalidPosts() {
        return Stream.of("{}", "{\"title\":\" \",\"content\":\"Body\"}",
                "{\"title\":\"Title\",\"content\":\"\"}",
                "{\"title\":\"" + "x".repeat(201) + "\",\"content\":\"Body\"}",
                "{\"title\":\"Title\",\"content\":\"" + "x".repeat(10001) + "\"}");
    }

    @ParameterizedTest
    @MethodSource("invalidComments")
    void rejectsInvalidCommentsBeforeCallingService(String body) throws Exception {
        mvc.perform(post("/api/posts/41/comments").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    static Stream<String> invalidComments() {
        return Stream.of("{}", "{\"author\":\" \",\"content\":\"Body\"}",
                "{\"author\":\"Ada\",\"content\":\"\"}",
                "{\"author\":\"" + "x".repeat(101) + "\",\"content\":\"Body\"}",
                "{\"author\":\"Ada\",\"content\":\"" + "x".repeat(2001) + "\"}");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/posts/0", "/api/posts/-1", "/api/posts/not-a-number",
            "/api/posts?page=-1", "/api/posts?size=0", "/api/posts?size=101",
            "/api/posts/41/comments/0"})
    void rejectsInvalidIdentifiersAndPagination(String uri) throws Exception {
        mvc.perform(get(uri)).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void rejectsMalformedJson() throws Exception {
        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/posts/41", "/api/posts/41/comments/72"})
    void validatesUpdateRequestsBeforeCallingService(String uri) throws Exception {
        mvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
