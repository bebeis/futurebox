package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureHologram;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class FutureHologramRepository {
    public void save(FutureHologram futureHologram) throws SQLException {
        String sql = "INSERT INTO future_hologram (box_id, message, image_url) VALUES (?, ?, ?) Returning id";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureHologram.getBoxId());
            pstmt.setString(2, futureHologram.getMessage());
            pstmt.setString(3, futureHologram.getImageUrl());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureHologram.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureHologram failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureHologram findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_hologram WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureHologram futureHologram = new FutureHologram();
                futureHologram.setId(rs.getLong("id"));
                futureHologram.setBoxId(rs.getLong("box_id"));
                futureHologram.setMessage(rs.getString("message"));
                futureHologram.setImageUrl(rs.getString("image_url"));
                return futureHologram;
            } else {
                throw new NoSuchElementException("FutureHologram not found id=" + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureHologram findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_hologram WHERE box_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureHologram futureHologram = new FutureHologram();
                futureHologram.setId(rs.getLong("id"));
                futureHologram.setBoxId(rs.getLong("box_id"));
                futureHologram.setMessage(rs.getString("message"));
                futureHologram.setImageUrl(rs.getString("image_url"));
                return futureHologram;
            }
            return null;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_hologram WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void deleteByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_hologram WHERE box_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void update(FutureHologram futureHologram) throws SQLException {
        String sql = "UPDATE future_hologram SET box_id = ?, message = ?, image_url = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureHologram.getBoxId());
            pstmt.setString(2, futureHologram.getMessage());
            pstmt.setString(3, futureHologram.getImageUrl());
            pstmt.setLong(4, futureHologram.getId());
            pstmt.executeUpdate();
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
