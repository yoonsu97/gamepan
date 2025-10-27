package com.gamepan.gameboard.domain.post.dto;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto { // 응답
    private Long id;
    private Board board;
    private User user;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .user(post.getUser())
                .board(post.getBoard())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .build();
    }
}


