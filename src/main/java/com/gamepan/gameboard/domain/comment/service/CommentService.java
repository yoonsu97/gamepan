package com.gamepan.gameboard.domain.comment.service;

import com.gamepan.gameboard.domain.comment.dto.CommentRequestDto;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.repository.CommentRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 생성
    public Comment createComment(CommentRequestDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(dto.getContent())
                .build();

        return commentRepository.save(comment);
    }

    // 게시글별 댓글 조회
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    // 댓글 수정
    public Comment updateComment(Long id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(id);
    }


}
