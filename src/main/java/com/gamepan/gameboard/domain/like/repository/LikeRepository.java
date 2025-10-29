package com.gamepan.gameboard.domain.like.repository;

import com.gamepan.gameboard.domain.like.entity.Like;
import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);  // 해당 게시물 아이디에 해당 유저가 1번이라도 좋아요 했는지 확인
    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId); // 해당 게시물에 해당 유저의 좋아요 가져오기
    int countByPost_Id(Long postId); //
    void deleteByPost_IdAndUser_Id(Long postId, Long userId);

    // 내가 좋아요 한 게시글 가져오기(삭제 되지 않은 게시글)
    @Query("""
        SELECT p FROM Post p
        JOIN Like l ON l.post.id = p.id
        WHERE l.user.id = :userId
          AND p.isDeleted = false
        ORDER BY p.createdAt DESC
    """)
    Page<Post> findLikedPosts(@Param("userId") Long userId, Pageable pageable);

}
