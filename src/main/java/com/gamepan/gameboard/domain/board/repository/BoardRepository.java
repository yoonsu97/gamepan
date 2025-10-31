package com.gamepan.gameboard.domain.board.repository;

import com.gamepan.gameboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByCodeAndIsDeletedFalse(String code);
    List<Board> findAllByIsDeletedFalse();
    List<Board> findAllByIsDeletedTrue();
    Optional<Board> findByIdAndIsDeletedFalse(Long id);
    Optional<Board> findByIdAndIsDeletedTrue(Long id);

}
