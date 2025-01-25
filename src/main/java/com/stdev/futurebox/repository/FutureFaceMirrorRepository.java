package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureFaceMirror;
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
public class FutureFaceMirrorRepository {

    private final DataSource dataSource;

    public void save(FutureFaceMirror futureFaceMirror) throws SQLException {
        String sql = "INSERT INTO future_face_mirror (box_id, year, image_url) values (?, ?, ?) RETURNING id";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureFaceMirror.getBoxId());
            pstmt.setInt(2, futureFaceMirror.getYear());
            pstmt.setString(3, futureFaceMirror.getImageUrl());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                futureFaceMirror.setId(rs.getLong("id"));
            } else {
                throw new SQLException("FutureFaceMirror 생성 실패");
            }
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureFaceMirror findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_face_mirror WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToMirror(rs);
            }
            throw new NoSuchElementException("해당 ID의 FutureFaceMirror가 없습니다: " + id);
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureFaceMirror findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_face_mirror WHERE box_id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToMirror(rs);
            }
            return null;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public void update(FutureFaceMirror futureFaceMirror) throws SQLException {
        String sql = "UPDATE future_face_mirror SET box_id = ?, year = ?, image_url = ? WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureFaceMirror.getBoxId());
            pstmt.setInt(2, futureFaceMirror.getYear());
            pstmt.setString(3, futureFaceMirror.getImageUrl());
            pstmt.setLong(4, futureFaceMirror.getId());
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_face_mirror WHERE id = ?";
        
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
        String sql = "DELETE FROM future_face_mirror WHERE box_id = ?";
        
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

    private FutureFaceMirror mapToMirror(ResultSet rs) throws SQLException {
        FutureFaceMirror mirror = new FutureFaceMirror();
        mirror.setId(rs.getLong("id"));
        mirror.setBoxId(rs.getLong("box_id"));
        mirror.setYear(rs.getInt("year"));
        mirror.setImageUrl(rs.getString("image_url"));
        return mirror;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("ResultSet 닫기 실패", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("Statement 닫기 실패", e);
            }
        }
        // Connection은 닫지 않음 - 스프링이 관리하도록 함
    }
}
