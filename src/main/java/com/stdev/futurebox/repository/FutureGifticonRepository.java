package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureGifticon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FutureGifticonRepository {
    public void save(FutureGifticon futureGifticon) throws SQLException {
        String sql = "INSERT INTO future_gifticon_types (name, description, image_url, detail_image_url) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureGifticon.getName());
            pstmt.setString(2, futureGifticon.getDescription());
            pstmt.setString(3, futureGifticon.getImageUrl());
            pstmt.setString(4, futureGifticon.getDetailImageUrl());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureGifticon.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureGifticon failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureGifticon findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_gifticon_types WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureGifticon futureGifticon = new FutureGifticon();
                futureGifticon.setId(rs.getLong("id"));
                futureGifticon.setName(rs.getString("name"));
                futureGifticon.setDescription(rs.getString("description"));
                futureGifticon.setImageUrl(rs.getString("image_url"));
                futureGifticon.setDetailImageUrl(rs.getString("detail_image_url"));
                return futureGifticon;
            } else {
                throw new NoSuchElementException("No gifticon found with id " + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureGifticon> findAll() throws SQLException {
        String sql = "SELECT * FROM future_gifticon_types";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            List<FutureGifticon> futureGifticons = new ArrayList<>();
            while (rs.next()) {
                FutureGifticon futureGifticon = new FutureGifticon();
                futureGifticon.setId(rs.getLong("id"));
                futureGifticon.setName(rs.getString("name"));
                futureGifticon.setDescription(rs.getString("description"));
                futureGifticon.setImageUrl(rs.getString("image_url"));
                futureGifticon.setDetailImageUrl(rs.getString("detail_image_url"));
                futureGifticons.add(futureGifticon);
            }

            return futureGifticons;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, stmt, rs);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM future_gifticon_types WHERE id = ?";
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

    public void update(FutureGifticon futureGifticon) throws SQLException {
        String sql = "UPDATE future_gifticon_types SET name = ?, description = ?, image_url = ?, detail_image_url = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureGifticon.getName());
            pstmt.setString(2, futureGifticon.getDescription());
            pstmt.setString(3, futureGifticon.getImageUrl());
            pstmt.setString(4, futureGifticon.getDetailImageUrl());
            pstmt.setLong(5, futureGifticon.getId());
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
