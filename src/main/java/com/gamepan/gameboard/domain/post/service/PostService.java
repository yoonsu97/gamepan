package com.gamepan.gameboard.domain.post.service;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 게시글 관련 비즈니스 로직 처리를 위한 서비스
@Service
@RequiredArgsConstructor //postRepository 의존 주입 생성자 자동 생성
@Transactional // DB 관련 작업 Transactional 묶어서 작업
public class PostService {
    private final PostRepository postRepository; //DB 접근을 위한 Repository 의존성 주입
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 게시글 생성 메서드
    public Post createPost(PostRequestDto dto) {
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시판 없음"));
        if (board.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시판에는 게시글을 작성할 수 없습니다.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Post post = Post.builder()
                .board(board)
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return postRepository.save(post);
    }

    // 게시판 내에 전체 게시글 조회
    public List<Post> getAllPosts(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시판 없음"));

        if (board.isDeleted()) {
            throw new IllegalStateException("삭제된 게시판의 게시글은 조회할 수 없습니다.");
        }
        return postRepository.findAllByBoardId(boardId);
    }

    // 상세조회
    public Post getPost(Long id) {
        // 아이디로 게시글 조회, 없으면 예외
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (post.isDeleted() || post.getBoard().isDeleted()) {
            throw new IllegalStateException("삭제된 게시글은 조회할 수 없습니다.");
        }

        return post;
    }

    // 게시글 수정
    public Post updatePost(Long id, PostRequestDto dto) {
        // 기존 기시글 찾기. 없으면 예외
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시글을 찾을 수 없습니다"));
        if (post.isDeleted() || post.getBoard().isDeleted()) {
            throw new IllegalStateException("삭제된 게시글은 수정할 수 없습니다.");
        }
        // 기존 엔티티 변경 -> 이미 있는거를 조회해서, 제목이랑 내용 수정하기 위함.
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // 엔티티 수정본 저장
        return postRepository.save(post);
    }

    //  게시글 삭제 (Soft Delete 적용)
    public void softDeletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 게시글이 존재하지 않습니다."));

        post.softDelete(); // BaseEntity의 softDelete() 메서드 호출
        postRepository.save(post);
    }

    //  게시글 복구
    public void restorePost(Long id) {
        Post post = postRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("복구할 게시글이 존재하지 않습니다."));

        // 게시판이 삭제된 경우 복구 불가
        if (post.getBoard().isDeleted()) {
            throw new IllegalStateException("삭제된 게시판의 게시글은 복구할 수 없습니다.");
        }

        // 이미 복구된 게시글이면 예외
        if (!post.isDeleted()) {
            throw new IllegalStateException("이미 활성화된 게시글입니다.");
        }

        post.restore();
        postRepository.save(post);
    }


}
