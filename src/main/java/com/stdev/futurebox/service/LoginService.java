package com.stdev.futurebox.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String ADMIN_USERNAME = "2047future";
    private static final String ADMIN_PASSWORD = "stdev2024";

    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
} 