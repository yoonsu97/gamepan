package com.gamepan.gameboard.domain.board.repository;

import com.gamepan.gameboard.domain.board.entity.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
