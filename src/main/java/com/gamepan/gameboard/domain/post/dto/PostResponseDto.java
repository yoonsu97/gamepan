package com.gamepan.gameboard.domain.post.dto;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto { // 응답
    private Long id;
    private Long boardId;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .nickname(post.getUser() != null ? post.getUser().getNickname() : "익명")
                .boardId(post.getBoard().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}



