package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureTarot;
import java.sql.Array;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FutureTarotRepository {
    
    private final DataSource dataSource;

    public FutureTarot save(FutureTarot tarot) throws SQLException {
        String sql = "INSERT INTO future_tarot (box_id, card_indexes, description) VALUES (?, ?, ?) RETURNING id";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, tarot.getBoxId());
            pstmt.setArray(2, con.createArrayOf("integer", tarot.getIndexes()));
            pstmt.setString(3, tarot.getDescription());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                tarot.setId(rs.getLong("id"));
                return tarot;
            } else {
                throw new SQLException("FutureTarot 생성 실패");
            }
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureTarot findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_tarot WHERE box_id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToTarot(rs);
            }
            return null;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void deleteByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_tarot WHERE box_id = ?";
        
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

    public FutureTarot findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_tarot WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToTarot(rs);
            }
            throw new NoSuchElementException("해당 ID의 FutureTarot가 없습니다: " + id);
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_tarot WHERE id = ?";
        
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

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_tarot";
        
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

    public void update(FutureTarot tarot) throws SQLException {
        String sql = "UPDATE future_tarot SET box_id = ?, card_indexes = ?, description = ? WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, tarot.getBoxId());
            pstmt.setArray(2, con.createArrayOf("integer", tarot.getIndexes()));
            pstmt.setString(3, tarot.getDescription());
            pstmt.setLong(4, tarot.getId());
            
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    private FutureTarot mapToTarot(ResultSet rs) throws SQLException {
        FutureTarot tarot = new FutureTarot();
        tarot.setId(rs.getLong("id"));
        tarot.setBoxId(rs.getLong("box_id"));
        
        Array indexesArray = rs.getArray("card_indexes");
        if (indexesArray != null) {
            Integer[] indexes = (Integer[]) indexesArray.getArray();
            tarot.setIndexes(indexes);
        }
        
        tarot.setDescription(rs.getString("description"));
        return tarot;
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { log.error("ResultSet 닫기 실패", e); }
        }
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { log.error("Statement 닫기 실패", e); }
        }
        // Connection은 닫지 않음 - 스프링이 관리하도록 함
    }
} 