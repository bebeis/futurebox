package com.stdev.futurebox.repository;

import com.stdev.futurebox.dto.DailyStatistics;
import com.stdev.futurebox.dto.TypeStatistics;
import com.stdev.futurebox.dto.ItemStatistics;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StatisticsRepository {

    private final DataSource dataSource;

    public List<DailyStatistics> getDailyStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            WITH RECURSIVE dates AS (
                SELECT ?::date as date
                UNION ALL
                SELECT date + 1
                FROM dates
                WHERE date < ?::date
            ),
            daily_stats AS (
                SELECT 
                    DATE(fb.created_at) as stat_date,
                    COUNT(*) as total_count,
                    SUM(CASE WHEN fb.is_opened THEN 1 ELSE 0 END) as opened_count,
                    COUNT(DISTINCT fg.id) as gifticon_count,
                    COUNT(DISTINCT fn.id) as note_count,
                    COUNT(DISTINCT fh.id) as hologram_count,
                    COUNT(DISTINCT ft.id) as tarot_count,
                    COUNT(DISTINCT fp.id) as perfume_count
                FROM future_box fb
                LEFT JOIN future_gifticon_types fg ON fb.future_gifticon_type = fg.id
                LEFT JOIN future_note fn ON fb.id = fn.box_id
                LEFT JOIN future_hologram fh ON fb.id = fh.box_id
                LEFT JOIN future_tarot ft ON fb.id = ft.box_id
                LEFT JOIN future_perfume fp ON fb.id = fp.box_id
                WHERE DATE(fb.created_at) BETWEEN ? AND ?
                GROUP BY DATE(fb.created_at)
            )
            SELECT 
                d.date,
                COALESCE(s.total_count, 0) as total_count,
                COALESCE(s.opened_count, 0) as opened_count,
                COALESCE(s.gifticon_count, 0) as gifticon_count,
                COALESCE(s.note_count, 0) as note_count,
                COALESCE(s.hologram_count, 0) as hologram_count,
                COALESCE(s.tarot_count, 0) as tarot_count,
                COALESCE(s.perfume_count, 0) as perfume_count
            FROM dates d
            LEFT JOIN daily_stats s ON d.date = s.stat_date
            ORDER BY d.date ASC
        """;

        List<DailyStatistics> statistics = new ArrayList<>();
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                statistics.add(new DailyStatistics(
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("total_count"),
                        rs.getLong("opened_count"),
                        rs.getLong("gifticon_count"),
                        rs.getLong("note_count"),
                        rs.getLong("hologram_count"),
                        rs.getLong("tarot_count"),
                        rs.getLong("perfume_count"),
                        rs.getLong("faceMirror_count")
                ));
            }
        } finally {
            close(con, pstmt, rs);
        }
        
        return statistics;
    }

    public List<TypeStatistics> getTypeStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            WITH gifticon_stats AS (
                SELECT 
                    '기프티콘' AS type_category,
                    g.name AS type_name,
                    COUNT(*) AS count,
                    (
                        COUNT(*) * 100.0 
                        / NULLIF( 
                            (
                                SELECT COUNT(*) 
                                FROM future_box 
                                WHERE future_gifticon_type IS NOT NULL
                                AND DATE(created_at) BETWEEN ? AND ?
                            ), 0
                        )
                    ) AS percentage
                FROM future_box fb
                RIGHT JOIN future_gifticon_types g 
                       ON fb.future_gifticon_type = g.id
                WHERE DATE(fb.created_at) BETWEEN ? AND ?
                GROUP BY g.name
            )
            SELECT * FROM gifticon_stats
            ORDER BY type_category, count DESC
        """;

        List<TypeStatistics> statistics = new ArrayList<>();

        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            pstmt.setDate(5, Date.valueOf(startDate));
            pstmt.setDate(6, Date.valueOf(endDate));

            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String category = rs.getString("type_category");
                String typeName = rs.getString("type_name");
                Long count = rs.getLong("count");
                Double percentage = rs.getDouble("percentage");
                statistics.add(new TypeStatistics(category, typeName, count, percentage));
            }
        } finally {
            close(con, pstmt, rs);
        }
        return statistics;
    }

    public Long getCreateCount(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS create_count
            FROM future_box
            WHERE DATE(created_at) BETWEEN ? AND ?
        """;
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong("create_count");
            }
            return 0L;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<ItemStatistics> getItemStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
            WITH total_boxes AS (
                SELECT COUNT(*) AS total
                FROM future_box
                WHERE DATE(created_at) BETWEEN ? AND ?
            )
            SELECT
                'Tarot' AS item_name,
                COUNT(ft.id) AS count,
                COUNT(ft.id)*100.0/(SELECT total FROM total_boxes) AS percentage
            FROM future_box fb
            LEFT JOIN future_tarot ft ON fb.id = ft.box_id
            WHERE ft.id IS NOT NULL
              AND DATE(fb.created_at) BETWEEN ? AND ?
            
            UNION ALL
            
            SELECT
                'Perfume',
                COUNT(fp.id),
                COUNT(fp.id)*100.0/(SELECT total FROM total_boxes)
            FROM future_box fb
            LEFT JOIN future_perfume fp ON fb.id = fp.box_id
            WHERE fp.id IS NOT NULL
              AND DATE(fb.created_at) BETWEEN ? AND ?
              
            UNION ALL
            
            SELECT
                'Note',
                COUNT(fn.id),
                COUNT(fn.id)*100.0/(SELECT total FROM total_boxes)
            FROM future_box fb
            LEFT JOIN future_note fn ON fb.id = fn.box_id
            WHERE fn.id IS NOT NULL
              AND DATE(fb.created_at) BETWEEN ? AND ?
              
            UNION ALL
            
            SELECT
                'Hologram',
                COUNT(fh.id),
                COUNT(fh.id)*100.0/(SELECT total FROM total_boxes)
            FROM future_box fb
            LEFT JOIN future_hologram fh ON fb.id = fh.box_id
            WHERE fh.id IS NOT NULL
              AND DATE(fb.created_at) BETWEEN ? AND ?
              
            UNION ALL
            
            SELECT
                'FaceMirror',
                COUNT(fm.id),
                COUNT(fm.id)*100.0/(SELECT total FROM total_boxes)
            FROM future_box fb
            LEFT JOIN future_face_mirror fm ON fb.id = fm.box_id
            WHERE fm.id IS NOT NULL
              AND DATE(fb.created_at) BETWEEN ? AND ?
            
            ORDER BY count DESC
        """;

        List<ItemStatistics> statistics = new ArrayList<>();

        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            pstmt.setDate(5, Date.valueOf(startDate));
            pstmt.setDate(6, Date.valueOf(endDate));
            pstmt.setDate(7, Date.valueOf(startDate));
            pstmt.setDate(8, Date.valueOf(endDate));
            pstmt.setDate(9, Date.valueOf(startDate));
            pstmt.setDate(10, Date.valueOf(endDate));
            pstmt.setDate(11, Date.valueOf(startDate));
            pstmt.setDate(12, Date.valueOf(endDate));
            pstmt.setDate(13, Date.valueOf(startDate));
            pstmt.setDate(14, Date.valueOf(endDate));

            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                statistics.add(new ItemStatistics(
                    rs.getString("item_name"),
                    rs.getLong("count"),
                    rs.getDouble("percentage")
                ));
            }
        } finally {
            close(con, pstmt, rs);
        }
        return statistics;
    }

    public List<TypeStatistics> getGifticonTypeStatistics() throws SQLException {
        String sql = """
            WITH total_boxes AS (
                SELECT COUNT(*) as total FROM future_box
            )
            SELECT 
                '기프티콘' as category,
                g.name as type_name,
                COUNT(fb.future_gifticon_type) as count,
                COUNT(fb.future_gifticon_type)*100.0/(SELECT total FROM total_boxes) as percentage
            FROM future_box fb
            LEFT JOIN future_gifticon_types g ON fb.future_gifticon_type = g.id
            GROUP BY g.id, g.name
            ORDER BY count DESC
            """;

        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<TypeStatistics> statistics = new ArrayList<>();
            while (rs.next()) {
                statistics.add(new TypeStatistics(
                    rs.getString("category"),
                    rs.getString("type_name"),
                    rs.getLong("count"),
                    rs.getDouble("percentage")
                ));
            }
            return statistics;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public long countTotalBoxes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_box";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public long countOpenedBoxes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_box WHERE is_opened = true";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public Map<String, Long> countByGifticonType() throws SQLException {
        String sql = "SELECT future_gifticon_type, COUNT(*) FROM future_box GROUP BY future_gifticon_type";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            Map<String, Long> result = new HashMap<>();
            while (rs.next()) {
                String type = rs.getString(1);
                Long count = rs.getLong(2);
                result.put(type, count);
            }
            return result;
        } finally {
            close(null, pstmt, rs);
        }
    }

    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { log.error("ResultSet 닫기 실패", e); }
        }
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { log.error("Statement 닫기 실패", e); }
        }
        // Connection은 닫지 않음 - 스프링이 관리하도록 함
    }

}