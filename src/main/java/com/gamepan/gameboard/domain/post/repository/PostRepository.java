package com.gamepan.gameboard.domain.post.repository;

import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판 내 모든 게시글 조회 (Soft Delete 제외)
    List<Post> findAllByBoardId(Long boardId);

    // 삭제된 게시글도 포함해서 조회 (복구용)
    /*@Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdIncludingDeleted(@Param("id") Long id);*/

    @Query(value = "SELECT * FROM post WHERE id = :id", nativeQuery = true)
    Optional<Post> findByIdIncludingDeleted(@Param("id") Long id);

    @Query(value = "SELECT * FROM post WHERE board_id = :boardId", nativeQuery = true)
    List<Post> findAllByBoardIdIncludingDeleted(@Param("boardId") Long boardId);
}
