package com.gamepan.gameboard.controller;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.dto.BoardResponseDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
//@RequestMapping("/api/admin/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시판 생성(관리자 기능) - POST /boards
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto dto) {
        Board board = boardService.createBoard(dto);
        return ResponseEntity.ok(BoardResponseDto.from(board));
    }
    // 게시판 목록  보기- GET /boards
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.getAllBoards()
                .stream()
                .map(BoardResponseDto::from)
                .toList();
        return ResponseEntity.ok(boards);
    }
    // 게시판 1개 가져오기 - GET /boards/{board_id}
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long id) {
        Board board = boardService.getBoard(id);
        return ResponseEntity.ok(BoardResponseDto.from(board));
    }

    // 게시글 수정(작성자, 관리자 권한)  - PUT /posts/{postId}
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto dto) {
        Board updated = boardService.updateBoard(id,dto);
        return ResponseEntity.ok(BoardResponseDto.from(updated));
    }

    // 게시판 삭제(관리자 기능) -  DELETE /boards/{board_id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
