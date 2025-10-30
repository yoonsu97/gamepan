package com.gamepan.gameboard.domain.report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportPostRequest {
    @NotNull
    private Long postId;  // 신고할 게시글 ID
}
