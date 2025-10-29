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
    public Comment createComment(Long postId,Long currentUserId,CommentRequestDto dto) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (post.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글에는 댓글을 작성할 수 없습니다.");
        }

        User user = userRepository.findByIdAndIsDeletedFalse(currentUserId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(dto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        post.increaseCommentCount();
        postRepository.save(post);

        return savedComment;
    }

    // 전체 댓글 조회
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // 게시글별 댓글 조회
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (post.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글의 댓글은 조회할 수 없습니다.");
        }

        return commentRepository.findAllByPostId(postId);
    }

    // 댓글 수정
    public Comment updateComment(Long id ,CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Post post = comment.getPost();
        if (post.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글의 댓글은 수정할 수 없습니다.");
        }

        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Post post = comment.getPost();
        if (post.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글의 댓글은 삭제할 수 없습니다.");
        }

        post.decreaseCommentCount();
        postRepository.save(post);

        commentRepository.delete(comment);
    }


}
