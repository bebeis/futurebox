package com.stdev.futurebox.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    // 실제 환경에서는 데이터베이스에서 조회하거나 보안된 방식으로 처리해야 합니다
    private static final String ADMIN_USERNAME = "2047future";
    private static final String ADMIN_PASSWORD = "stdev2024";

    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
} 