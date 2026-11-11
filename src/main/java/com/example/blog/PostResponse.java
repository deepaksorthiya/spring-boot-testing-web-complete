package com.example.blog;

import java.util.List;

public record PostResponse(Long id, String title, String content, List<CommentResponse> comments) {
}
