package com.gamepan.gameboard.domain.post.service;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// 게시글 관련 비즈니스 로직 처리를 위한 서비스
@Service
@RequiredArgsConstructor //postRepository 의존 주입 생성자 자동 생성
@Transactional // DB 관련 작업 Transactional 묶어서 작업
public class PostService {
    private final PostRepository postRepository; //DB 접근을 위한 Repository 의존성 주입
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;

    // 게시글 생성 메서드
    public Post createPost(Long boardId, Long userId, PostRequestDto dto) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .board(board)
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        return postRepository.save(post);
    }

    //전체 게시글 조회(삭제 포함, admin에서 사용)
    public List<Post> getAllActivePosts() {
        return postRepository.findAllByIsDeletedFalse();
    }

    // 내 작성글 페이지네이션
    @Transactional
    public Page<Post> findPageByAuthor(Long userId, Pageable pageable) {
        return postRepository.findByUserIdAndActiveBoard(userId, pageable);

    }

    // 특정 게시판 게시글
    public List<PostResponseDto> getAllPosts(Long boardId) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (board.isDeleted()) {
            throw new IllegalStateException("삭제된 게시판의 게시글은 조회할 수 없습니다.");
        }

        return postRepository.findAllByBoardIdAndIsDeletedFalseOrderByIdDesc(boardId).stream()
                .map(PostResponseDto::from)
                .toList();
    }

    // 상세조회
    @Transactional
    public Post getPost(Long id) {
        // 아이디로 게시글 조회, 없으면 예외

        return postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public void increaseViewCount(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        postRepository.incrementViewCount(id);
        post.updateViewCount(post.getViewCount() + 1);
    }


    // 최근 생성 게시물을 10개 가져오기
    public List<Post> getRecentPostsByBoard(Long boardId) {
        return postRepository.findTop10ByBoardIdAndIsDeletedFalseOrderByCreatedAtDesc(boardId);
    }

    // 게시글 수정
    public Post updatePost(Long id, User currentUser ,PostRequestDto dto) {
        // 기존 게시글 찾기. 없으면 예외
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        authorizationService.hasPostPermission(post, currentUser,ErrorCode.POST_FORBIDDEN);

        // 기존 엔티티 변경 -> 이미 있는거를 조회해서, 제목이랑 내용 수정하기 위함.
        post.updateTitle(dto.getTitle());
        post.updateContent(dto.getContent());

        // 엔티티 수정본 저장
        return post;
    }

    //  게시글 삭제 (Soft Delete 적용)
    public void softDeletePost(Long id, User currentUser) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        authorizationService.hasPostPermission(post, currentUser,ErrorCode.POST_FORBIDDEN);

        post.softDelete(); // BaseEntity의 softDelete() 메서드 호출
    }

    //  게시글 복구
    public void restorePost(Long id, User currentUser) {
        Post post = postRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 게시판이 삭제된 경우 복구 불가
        if (post.getBoard().isDeleted()) {
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND);
        }

        authorizationService.hasPostPermission(post, currentUser,ErrorCode.POST_FORBIDDEN);

        post.restore();
    }

    // 검색
    public List<PostResponseDto> searchPosts(Long boardId, String keyword) {
        return postRepository.findByBoardIdAndTitleContainingIgnoreCase(boardId, keyword).stream()
                .map(PostResponseDto::from)
                .toList();
    }


}
