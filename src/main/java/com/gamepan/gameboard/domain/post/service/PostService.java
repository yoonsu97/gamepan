package com.gamepan.gameboard.domain.post.service;

import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 게시글 관련 비즈니스 로직 처리를 위한 서비스
@Service
@RequiredArgsConstructor //postRepository 의존 주입 생성자 자동 생성
@Transactional // DB 관련 작업 Transactional 묶어서 작업
public class PostService {
    private final PostRepository postRepository; //DB 접근을 위한 Repository 의존성 주입

    // 게시글 생성 메서드
    public Post createPost(PostRequestDto dto) {
        Post post = Post.builder()
                .board(dto.getBoard())
                .user(dto.getUser())
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
    public Post updatePost(Long id, PostRequestDto dto) {
        // 기존 기시글 찾기. 없으면 예외
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시글을 찾을 수 없습니다"));
        // 기존 엔티티 변경 -> 이미 있는거를 조회해서, 제목이랑 내용 수정하기 위함.
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // 엔티티 수정본 저장
        return postRepository.save(post);
    }

    // 삭제
    public  void deletePost(Long id) {
        // 게시글 있는지 확인.
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 게시글이 존재하지 않습니다.");
        }
        // 존재하면 삭제
        postRepository.deleteById(id);
    }


}
