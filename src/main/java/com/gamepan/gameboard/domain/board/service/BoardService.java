package com.gamepan.gameboard.domain.board.service;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.post.service.PostService;
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


    // 게시판 생성 (중복 검사)
    public Board createBoard(BoardRequestDto dto) {
        if (boardRepository.existsByCodeAndIsDeletedFalse(dto.getCode())) {
            throw new IllegalArgumentException("이미 존재하는 게시판 코드입니다: " + dto.getCode());
        }

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

    // 게시판 삭제 (Soft Delete + 게시글까지 함께 삭제)
    public void softDeleteBoard(Long id, Long currentUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));


        board.softDelete();
        List<Post> posts = postRepository.findAllByBoardId(id);

        for (Post post : posts) {
            postService.softDeletePost(post.getId(),currentUserId);
        }
        boardRepository.save(board);
    }

    // 게시판 복구 (게시글 포함 복구)
    public void restoreBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("복구할 게시판이 존재하지 않습니다."));

        if (!board.isDeleted()) {
            throw new IllegalStateException("이미 활성화된 게시판입니다.");
        }

        board.restore(); // 게시판 및 하위 게시글 복구
        List<Post> posts = postRepository.findAllByBoardId(id);

        for (Post post : posts) {
            postService.restorePost(post.getId());
        }
        boardRepository.save(board);
    }
}
