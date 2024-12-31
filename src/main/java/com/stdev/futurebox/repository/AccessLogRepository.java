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
public class AccessLogRepository {

    private final DataSource dataSource;

    public void saveLog(String accessType, Long contentId, String ipAddress, String userAgent) {
        String sql = "INSERT INTO access_log (access_type, content_id, ip_address, user_agent, access_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accessType);
            pstmt.setLong(2, contentId);
            pstmt.setString(3, ipAddress);
            pstmt.setString(4, userAgent);
            pstmt.setTimestamp(5, Timestamp.valueOf(now));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("로그 저장 실패", e);
        }
    }
}
