package com.gamepan.gameboard.domain.post.repository;

import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판 내 모든 게시글 조회 (Soft Delete 제외)
    List<Post> findAllByBoardIdAndIsDeletedFalse(Long boardId);

    List<Post> findAllByBoardId(Long boardId);


}
