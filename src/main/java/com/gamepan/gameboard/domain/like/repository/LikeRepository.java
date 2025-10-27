package com.gamepan.gameboard.domain.like.repository;

import com.gamepan.gameboard.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);  // 해당 게시물 아이디에 해당 유저가 1번이라도 좋아요 했는지 확인
    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId); // 해당 게시물에 해당 유저의 좋아요 가져오기
    int countByPost_Id(Long postId); //
    void deleteByPost_IdAndUser_Id(Long postId, Long userId);
}
