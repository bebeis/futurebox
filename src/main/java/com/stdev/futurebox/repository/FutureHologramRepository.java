package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureHologram;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.datasource.DataSourceUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FutureHologramRepository {

    private final DataSource dataSource;

    public FutureHologram save(FutureHologram hologram) throws SQLException {
        String sql = "INSERT INTO future_hologram (box_id, image_url) VALUES (?, ?) RETURNING id";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, hologram.getBoxId());
            pstmt.setString(2, hologram.getImageUrl());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                hologram.setId(rs.getLong("id"));
                return hologram;
            } else {
                throw new SQLException("FutureHologram 생성 실패");
            }
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureHologram findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_hologram WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToHologram(rs);
            }
            throw new NoSuchElementException("해당 ID의 FutureHologram이 없습니다: " + id);
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureHologram findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_hologram WHERE box_id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToHologram(rs);
            }
            return null;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void update(FutureHologram hologram) throws SQLException {
        String sql = "UPDATE future_hologram SET image_url = ? WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, hologram.getImageUrl());
            pstmt.setLong(2, hologram.getId());
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_hologram WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    public void deleteByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_hologram WHERE box_id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    private FutureHologram mapToHologram(ResultSet rs) throws SQLException {
        FutureHologram hologram = new FutureHologram();
        hologram.setId(rs.getLong("id"));
        hologram.setBoxId(rs.getLong("box_id"));
        hologram.setImageUrl(rs.getString("image_url"));
        return hologram;
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

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_hologram";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        }
    }
}
