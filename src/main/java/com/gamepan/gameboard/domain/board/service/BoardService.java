package com.gamepan.gameboard.domain.board.service;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;


    // 게시판 전체조회
    public List<Board> getAllActiveBoards() {
        return boardRepository.findAllByIsDeletedFalse();
    }

    // 게시판 단건 조회
    public Board getBoard(Long id) {
        return boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }
}
