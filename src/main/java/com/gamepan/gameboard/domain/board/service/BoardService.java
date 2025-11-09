package com.gamepan.gameboard.domain.board.service;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;


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
