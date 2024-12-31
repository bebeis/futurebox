package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.dto.DailyStatistics;
import com.stdev.futurebox.dto.TypeStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class StatisticsRepository {

    public List<DailyStatistics> getDailyStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            WITH daily_stats AS (
                SELECT 
                    DATE(created_at) as date,
                    COUNT(*) as total_count,
                    SUM(CASE WHEN is_opened THEN 1 ELSE 0 END) as opened_count,
                    SUM(CASE WHEN future_movie_type IS NOT NULL THEN 1 ELSE 0 END) as movie_count,
                    SUM(CASE WHEN future_gifticon_type IS NOT NULL THEN 1 ELSE 0 END) as gifticon_count,
                    SUM(CASE WHEN future_invention_type IS NOT NULL THEN 1 ELSE 0 END) as invention_count
                FROM future_box
                WHERE DATE(created_at) BETWEEN ? AND ?
                GROUP BY DATE(created_at)
            )
            SELECT 
                date,
                total_count,
                opened_count,
                movie_count,
                gifticon_count,
                invention_count
            FROM daily_stats
            ORDER BY date ASC
        """;

        List<DailyStatistics> statistics = new ArrayList<>();
        
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    statistics.add(new DailyStatistics(
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("total_count"),
                        rs.getLong("opened_count"),
                        rs.getLong("movie_count"),
                        rs.getLong("gifticon_count"),
                        rs.getLong("invention_count")
                    ));
                }
            }
        }
        
        return statistics;
    }

    public List<TypeStatistics> getTypeStatistics() throws SQLException {
        String sql = """
            SELECT 
                '영화' AS type_category,
                m.name AS type_name,
                COUNT(*) AS count
            FROM future_box fb
            LEFT JOIN future_movie_types m ON fb.future_movie_type = m.id
            GROUP BY m.name
            UNION ALL
            SELECT 
                '기프티콘' AS type_category,
                g.name AS type_name,
                COUNT(*) AS count
            FROM future_box fb
            LEFT JOIN future_gifticon_types g ON fb.future_gifticon_type = g.id
            GROUP BY g.name
            UNION ALL
            SELECT 
                '발명품' AS type_category,
                i.name AS type_name,
                COUNT(*) AS count
            FROM future_box fb
            LEFT JOIN future_invention_types i ON fb.future_invention_type = i.id
            GROUP BY i.name
        """;

        List<TypeStatistics> statistics = new ArrayList<>();
        
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String category = rs.getString("type_category");
                String typeName = rs.getString("type_name");
                Long count = rs.getLong("count");
                statistics.add(new TypeStatistics(category + " - " + typeName, count));
            }
        }
        
        return statistics;
    }

    public Long getCreateCount(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS create_count
            FROM future_box
            WHERE DATE(created_at) BETWEEN ? AND ?
        """;
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("create_count");
                }
            }
        }
        return 0L;
    }

}