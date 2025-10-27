package com.gamepan.gameboard.domain.board.dto;

import com.gamepan.gameboard.domain.board.entity.Board;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {
    private Long id;                //게시판 고유ID

    private String code;               // 게시판 코드

    private String name;            // 게시판 이름

    private String description;

    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .code(board.getCode())
                .name(board.getName())
                .description(board.getDescription())
                .build();
    }
}
