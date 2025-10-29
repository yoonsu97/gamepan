package com.gamepan.gameboard.domain.comment.repository;

import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
    List<Comment> findAll();

    // 내가 쓴 댓글의 게시글을 가져옴
    @Query("""
        SELECT DISTINCT c.post
        FROM Comment c
        WHERE c.user.id = :userId
          AND c.post.isDeleted = false
        ORDER BY c.post.createdAt DESC
    """)
    Page<Post> findCommentedPostsByUser(@Param("userId") Long userId, Pageable pageable);
}
