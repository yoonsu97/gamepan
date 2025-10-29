package com.gamepan.gameboard.domain.post.repository;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판 내 모든 게시글 조회 (Soft Delete 제외)
    List<Post> findAllByBoardIdAndIsDeletedFalse(Long boardId);
    List<Post> findAllByIsDeletedFalse();
    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    List<Post> findAllByBoardId(Long boardId);
}
