package com.stdev.futurebox.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoginLogRepository {

    private final DataSource dataSource;

    public void saveLog(String username, String ipAddress, String userAgent, boolean isSuccess) {
        String sql = "INSERT INTO login_log (username, ip_address, user_agent, login_status, attempt_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, ipAddress);
            pstmt.setString(3, userAgent);
            pstmt.setString(4, isSuccess ? "SUCCESS" : "FAIL");
            pstmt.setTimestamp(5, Timestamp.valueOf(now));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("로그인 로그 저장 실패", e);
        }
    }
} 