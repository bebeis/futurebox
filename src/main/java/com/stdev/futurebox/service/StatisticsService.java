package com.stdev.futurebox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final DataSource dataSource;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new LinkedHashMap<>();
        Connection con = DataSourceUtils.getConnection(dataSource);
        
        try {
            // 전체 FutureBox 수
            stats.put("totalBoxes", getTotalBoxes(con));
            
            // 일별 생성 통계 (최근 7일 기준으로 정렬 수정)
            stats.put("dailyCreation", getDailyCreation(con));
            
            // 리텐션 통계 (수신자가 발신자가 되는 비율)
            stats.put("retention", getRetention(con));
            
            // 개봉율
            stats.put("openRate", getOpenRate(con));
            
            // 기프티콘 타입 분포
            stats.put("gifticonTypes", getGifticonTypes(con));
            
            // 각 기능별 사용 통계 (LEFT JOIN 방식으로 변경)
            stats.put("futureNoteUsage", getFeatureUsage(con, "future_note"));
            stats.put("futureHologramUsage", getFeatureUsage(con, "future_hologram"));
            stats.put("futureFaceMirrorUsage", getFeatureUsage(con, "future_face_mirror"));
            stats.put("futureTarotUsage", getFeatureUsage(con, "future_tarot"));
            stats.put("futurePerfumeUsage", getFeatureUsage(con, "future_perfume"));
            
            // Value Meter 포함 비율
            stats.put("valueMeter", getValueMeterStats(con));
            
            // 기프티콘 이용 비율
            stats.put("gifticonUsage", getGifticonUsage(con));
            
            // 암호화 메시지 사용 비율
            stats.put("encryptionRate", getEncryptionRate(con));
            
            return stats;
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    private Long getTotalBoxes(Connection con) throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_box";
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        }
    }

    private List<Map<String, Object>> getDailyCreation(Connection con) throws SQLException {
        String sql = "SELECT DATE(created_at) as date, COUNT(*) as count "
                   + "FROM future_box "
                   + "WHERE created_at >= CURRENT_DATE - INTERVAL '7 days' "
                   + "GROUP BY DATE(created_at) "
                   + "ORDER BY date ASC";
        
        List<Map<String, Object>> result = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> day = new LinkedHashMap<>();
                day.put("date", rs.getDate("date").toString());
                day.put("count", rs.getLong("count"));
                result.add(day);
            }
            return result;
        }
    }

    private Map<String, Object> getRetention(Connection con) throws SQLException {
        String sql = "SELECT "
                   + "COUNT(DISTINCT f1.sender) as total_senders, "
                   + "COUNT(DISTINCT CASE WHEN f2.id IS NOT NULL THEN f1.receiver END) as returning_senders "
                   + "FROM future_box f1 "
                   + "LEFT JOIN future_box f2 ON f1.receiver = f2.sender";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                double totalSenders = rs.getDouble("total_senders");
                double returningSenders = rs.getDouble("returning_senders");
                result.put("rate", totalSenders > 0 ? (returningSenders / totalSenders) * 100 : 0);
            }
            return result;
        }
    }

    private Map<String, Object> getOpenRate(Connection con) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(CASE WHEN is_opened = true THEN 1 END) * 100.0 / " +
                    "NULLIF(COUNT(*), 0) as rate " +
                    "FROM future_box";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("rate"));
            }
            return result;
        }
    }

    private List<Map<String, Object>> getGifticonTypes(Connection con) throws SQLException {
        String sql = "SELECT t.name, COUNT(b.id) as count " +
                    "FROM future_gifticon_types t " +
                    "LEFT JOIN future_box b ON t.id = b.future_gifticon_type " +
                    "GROUP BY t.id, t.name";
        
        List<Map<String, Object>> result = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> type = new LinkedHashMap<>();
                type.put("name", rs.getString("name"));
                type.put("count", rs.getLong("count"));
                result.add(type);
            }
            return result;
        }
    }

    private Map<String, Object> getFeatureUsage(Connection con, String tableName) throws SQLException {
        String sql = "SELECT "
                   + "COUNT(DISTINCT b.id) * 100.0 / NULLIF((SELECT COUNT(*) FROM future_box), 0) as usage_rate "
                   + "FROM future_box b "
                   + "LEFT JOIN " + tableName + " f ON b.id = f.box_id "
                   + "WHERE f.id IS NOT NULL";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("usage_rate"));
            }
            return result;
        }
    }

    private Map<String, Object> getValueMeterRate(Connection con) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(CASE WHEN future_value_meter_included = true THEN 1 END) * 100.0 / " +
                    "NULLIF(COUNT(*), 0) as rate " +
                    "FROM future_box";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("rate"));
            }
            return result;
        }
    }

    private Map<String, Object> getEncryptionRate(Connection con) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(CASE WHEN encrypted_message IS NOT NULL THEN 1 END) * 100.0 / " +
                    "NULLIF(COUNT(*), 0) as rate " +
                    "FROM future_note";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("rate"));
            }
            return result;
        }
    }

    private Map<String, Object> getGifticonUsage(Connection con) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(CASE WHEN future_gifticon_type IS NOT NULL THEN 1 END) * 100.0 / " +
                    "NULLIF(COUNT(*), 0) as rate " +
                    "FROM future_box";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("rate"));
            }
            return result;
        }
    }

    private Map<String, Object> getValueMeterStats(Connection con) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(CASE WHEN future_value_meter_included = true THEN 1 END) * 100.0 / " +
                    "NULLIF(COUNT(*), 0) as rate " +
                    "FROM future_box";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            Map<String, Object> result = new LinkedHashMap<>();
            if (rs.next()) {
                result.put("rate", rs.getDouble("rate"));
            }
            return result;
        }
    }

    private void close(PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { log.error("ResultSet 닫기 실패", e); }
        }
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { log.error("Statement 닫기 실패", e); }
        }
    }
} 