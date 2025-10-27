package com.gamepan.gameboard.domain.board.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDto {
    private String code;
    private String name;
    private String description;

}
