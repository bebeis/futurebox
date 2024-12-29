package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureFaceMirror;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureFaceMirrorRepository {

    public void save(FutureFaceMirror futureFaceMirror) throws SQLException {
        String sql = "INSERT INTO future_face_mirror (box_id, year, image_url) values (?, ?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureFaceMirror.getBoxId());
            pstmt.setInt(2, futureFaceMirror.getYear());
            pstmt.setString(3, futureFaceMirror.getImageUrl());
            rs = pstmt.executeQuery();

            if (rs.next())  {
                futureFaceMirror.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureFaceMirror failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureFaceMirror findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_face_mirror WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureFaceMirror futureFaceMirror = new FutureFaceMirror();
                futureFaceMirror.setId(rs.getLong("id"));
                futureFaceMirror.setBoxId(rs.getLong("box_id"));
                futureFaceMirror.setYear(rs.getInt("year"));
                futureFaceMirror.setImageUrl(rs.getString("image_url"));
                return futureFaceMirror;
            } else {
                throw new NoSuchElementException("FutureFaceMirror not found id=" + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
