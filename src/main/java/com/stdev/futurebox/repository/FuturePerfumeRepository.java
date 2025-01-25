package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FuturePerfume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FuturePerfumeRepository {
    
    private final DataSource dataSource;

    public FuturePerfume save(FuturePerfume perfume) throws SQLException {
        String sql = "INSERT INTO future_perfume (box_id, name, description, keywords, shape_type, color, outline_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, perfume.getBoxId());
            pstmt.setString(2, perfume.getName());
            pstmt.setString(3, perfume.getDescription());
            pstmt.setArray(4, con.createArrayOf("varchar", perfume.getKeywords()));
            pstmt.setInt(5, perfume.getShapeType());
            pstmt.setInt(6, perfume.getColor());
            pstmt.setInt(7, perfume.getOutlineType());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                perfume.setId(rs.getLong("id"));
                return perfume;
            } else {
                throw new SQLException("FuturePerfume 생성 실패");
            }
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FuturePerfume findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_perfume WHERE box_id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToPerfume(rs);
            }
            return null;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void deleteByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_perfume WHERE box_id = ?";
        
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

    public FuturePerfume findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_perfume WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToPerfume(rs);
            }
            throw new NoSuchElementException("해당 ID의 FuturePerfume이 없습니다: " + id);
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_perfume WHERE id = ?";
        
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

    private FuturePerfume mapToPerfume(ResultSet rs) throws SQLException {
        FuturePerfume perfume = new FuturePerfume();
        perfume.setId(rs.getLong("id"));
        perfume.setBoxId(rs.getLong("box_id"));
        perfume.setName(rs.getString("name"));
        perfume.setDescription(rs.getString("description"));
        Array keywordArray = rs.getArray("keywords");
        if (keywordArray != null) {
            perfume.setKeywords((String[]) keywordArray.getArray());
        }
        perfume.setShapeType(rs.getInt("shape_type"));
        perfume.setColor(rs.getInt("color"));
        perfume.setOutlineType(rs.getInt("outline_type"));
        return perfume;
    }

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_perfume";
        
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

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
} 