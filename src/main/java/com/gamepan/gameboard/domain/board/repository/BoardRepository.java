package com.gamepan.gameboard.domain.board.repository;

import com.gamepan.gameboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByCodeAndIsDeletedFalse(String code);

    List<Board> findAllByIsDeletedFalse();
}
