package com.gamepan.gameboard.domain.board.service;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private  final BoardRepository boardRepository;

    // 게시판 생성
    public Board createBoard(BoardRequestDto dto) {
        Board board = Board.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return boardRepository.save(board);
    }

    // 게시판 전체조회
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 게시판 단건 조회
    public Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다."));
    }

    // 게시판 수정
    public Board updateBoard(long id, BoardRequestDto dto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시판을 찾을 수 없습니다."));

        board.setCode(dto.getCode());
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());

        return boardRepository.save(board);
    }

    // 게시판 삭제
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 게시글이 존재하지 않습니다.");
        }
        boardRepository.deleteById(id);
    }
}
