package com.gamepan.gameboard.domain.post.dto;

import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto { // 요청
    private String title;
    private String content;
}
