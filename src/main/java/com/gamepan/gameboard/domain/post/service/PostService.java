package com.gamepan.gameboard.domain.post.service;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    // 게시글 생성 메서드
    public Post createPost(Long boardId, Long userId, PostRequestDto dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

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
        return postRepository.findAllByBoardId(boardId);
    }

    // 상세조회
    public Post getPost(Long id) {
        // 아이디로 게시글 조회, 없으면 예외
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
    }

    // 게시글 수정
    public Post updatePost(Long id, Long currentUserId,PostRequestDto dto) {
        // 기존 기시글 찾기. 없으면 예외
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "수정 할 게시글이 없습니다."));

        if(!post.getUser().getId().equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        // 기존 엔티티 변경 -> 이미 있는거를 조회해서, 제목이랑 내용 수정하기 위함.
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // 엔티티 수정본 저장
        return postRepository.save(post);
    }

    // 삭제
    public  void deletePost(Long id, Long currentUserId) {
        // 게시글 있는지 확인.
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제 할 게시글이 없습니다."));

        //게시글의 유저 아이디가 현재 아이디와 같은지 확인 (작성자인지 확인)(관리자 권한도 여기서 추가 가능)
        if(!post.getUser().getId().equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
        // 존재하면 삭제
        postRepository.deleteById(id);
    }


}
