package com.gamepan.gameboard.domain.comment.service;

import com.gamepan.gameboard.domain.comment.dto.CommentRequestDto;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.repository.CommentRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import  com.gamepan.gameboard.global.exception.ErrorCode;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    //댓글 생성
    public Comment createComment(Long postId, User currentUser, CommentRequestDto dto) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .post(post)
                .user(currentUser)
                .content(dto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        postRepository.incrementCommentCount(postId);
        post.setCommentCount(post.getCommentCount() + 1);

        return savedComment;
    }

    // 전체 댓글 조회
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // 게시글별 댓글 조회
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findAllByPostId(postId);
    }

    // 내가 댓글 단 게시글 조회
    @Transactional
    public Page<Post> findCommentedPostsByUser(Long userId, Pageable pageable) {
        return commentRepository.findCommentedPostsByUser(userId, pageable);
    }

    // 댓글 수정
    public Comment updateComment(Long id, User currentUser, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        authorizationService.hasCommentPermission(comment, currentUser, ErrorCode.COMMENT_FORBIDDEN);

        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }


    // 댓글 삭제 (작성자 또는 관리자 가능)
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        Post post= commentRepository.findPostByCommentId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        authorizationService.hasCommentPermission(comment, currentUser, ErrorCode.COMMENT_FORBIDDEN);

        postRepository.decrementCommentCount(post.getId());
        post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
        commentRepository.delete(comment);
    }

}
