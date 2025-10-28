package com.gamepan.gameboard.domain.board.repository;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.post.entity.Post;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByCode(String code);

    @Query(value = "SELECT * FROM board WHERE id = :id", nativeQuery = true)
    Optional<Board> findByIdIncludingDeleted(@Param("id") Long id);
}
