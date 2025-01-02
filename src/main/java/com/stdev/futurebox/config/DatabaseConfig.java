package com.stdev.futurebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv("DB_URL"));
        config.setUsername(System.getenv("DB_USERNAME"));
        config.setPassword(System.getenv("DB_PASSWORD"));
        config.setMaximumPoolSize(20); // 커넥션 풀 크기 설정
        config.setMinimumIdle(5);      // 최소 유휴 커넥션 수
        config.setIdleTimeout(300000); // 유휴 커넥션 타임아웃 (5분)
        config.setConnectionTimeout(20000); // 커넥션 타임아웃 (20초)
        
        return new HikariDataSource(config);
    }
} 