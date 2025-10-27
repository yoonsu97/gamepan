package com.gamepan.gameboard.domain.post.dto;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto { // 요청
    private Board board;
    private User user;
    private String title;
    private String content;

}