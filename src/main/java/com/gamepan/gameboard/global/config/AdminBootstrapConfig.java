package com.gamepan.gameboard.global.config;

import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminBootstrapConfig {
    private final UserService userService; // <- Repository 대신 Service 주입

    @Value("${app.initial-admin-username:}") // application-*.yml 에 설정해둔 초대코드
    private String seedUsername;


    @Bean
    public ApplicationRunner bootstrapAdmin() {
        return args -> {
            // 1) 이미 ADMIN 있으면 종료(멱등성)
            if (userService.existsByRole(Role.ADMIN)) {
                log.info("[AdminBootstrap] 이미 ADMIN이 존재합니다.");
                return;
            }

            // 2) 환경변수 기반 승격
            if (seedUsername == null || seedUsername.isBlank()) {
                log.warn("[AdminBootstrap] INITIAL_ADMIN_USERNAME가 존재 하지 않습니다.");
                return;
            }

            try {
                userService.promoteToAdminByUsername(seedUsername.trim()); // <- @Transactional(서비스)
                log.info("[AdminBootstrap] ADMIN 권한 부여 완료 : '{}'", seedUsername);
            } catch (IllegalArgumentException e) {
                log.warn("[AdminBootstrap] 시드 유저 '{}'가 없습니다.", seedUsername);
            }
        };
    }
}
