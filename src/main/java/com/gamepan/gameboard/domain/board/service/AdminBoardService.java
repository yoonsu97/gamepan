package com.gamepan.gameboard.domain.board.service;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gamepan.gameboard.global.exception.ErrorCode;


import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminBoardService {
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;


    // 게시판 생성 (중복 검사)
    public Board createBoard(User currentUser, BoardRequestDto dto) {
        if (boardRepository.existsByCodeAndIsDeletedFalse(dto.getCode())) {
            throw new BusinessException(ErrorCode.BOARD_DUPLICATE);
        }

        authorizationService.hasBoardPermission(currentUser, ErrorCode.BOARD_FORBIDDEN);

        Board board = Board.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return boardRepository.save(board);
    }

    // 삭제 되지 않은 게시판 전체조회
    public List<Board> getAllActiveBoards() {
        return boardRepository.findAllByIsDeletedFalse();
    }

     // 삭제된 게시판 목록 조회
    public List<Board> getAllDeletedBoards() {
        return boardRepository.findAllByIsDeletedTrue();
    }

    // 게시판 단건 조회
    public Board getBoard(Long id) {
        return boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }

    // 게시판 수정
    public Board updateBoard(long id, User currentUser, BoardRequestDto dto) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        authorizationService.hasBoardPermission(currentUser, ErrorCode.BOARD_FORBIDDEN);

        board.setCode(dto.getCode());
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());

        return board;
    }

    // 게시판 삭제 (Soft Delete )
    public void softDeleteBoard(Long id, User currentUser) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        if(currentUser == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        authorizationService.hasBoardPermission(currentUser, ErrorCode.BOARD_FORBIDDEN);

        board.softDelete();
    }

    // 게시판 복구 (게시글 포함 복구)
    public void restoreBoard(Long id, User currentUser) {
        Board board = boardRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        authorizationService.hasBoardPermission(currentUser, ErrorCode.BOARD_FORBIDDEN);

        board.restore(); // 게시판 및 하위 게시글 복구
    }


}

