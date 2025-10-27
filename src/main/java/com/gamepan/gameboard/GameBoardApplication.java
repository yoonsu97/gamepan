package com.gamepan.gameboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GameBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameBoardApplication.class, args);
    }

}
