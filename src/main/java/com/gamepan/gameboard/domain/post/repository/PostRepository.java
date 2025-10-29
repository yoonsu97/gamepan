package com.gamepan.gameboard.domain.post.repository;

import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판 내 모든 게시글 조회 (Soft Delete 제외)
    List<Post> findAllByBoardIdAndIsDeletedFalse(Long boardId);
    List<Post> findAllByIsDeletedFalse();
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    List<Post> findTop10ByBoardIdAndIsDeletedFalseOrderByCreatedAtDesc(Long boardId);
    Page<Post> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId and p.isDeleted = false")
    int incrementViewCount(Long postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :postId and p.isDeleted = false")
    int incrementLikeCount(Long postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.likeCount = CASE WHEN p.likeCount > 0 THEN p.likeCount - 1 ELSE 0 END " +
            "where p.id = :postId and p.isDeleted = false")
    int decrementLikeCount(Long postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.commentCount = p.commentCount + 1 where p.id = :postId and p.isDeleted = false")
    int incrementCommentCount(Long postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.commentCount = CASE WHEN p.commentCount > 0 THEN p.commentCount - 1 ELSE 0 END " +
            "where p.id = :postId and p.isDeleted = false")
    int decrementCommentCount(Long postId);

    List<Post> findAllByBoardId(Long boardId);
}
