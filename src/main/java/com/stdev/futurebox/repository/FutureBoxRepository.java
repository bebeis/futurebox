package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class FutureBoxRepository {

    public FutureBox save(FutureBox futureBox) throws SQLException {
        String sql =
                "INSERT INTO future_box (uuid, receiver, sender, is_opened, future_movie_type, future_gifticon_type, future_invention_type, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, futureBox.getUuid());
            pstmt.setString(2, futureBox.getReceiver());
            pstmt.setString(3, futureBox.getSender());
            pstmt.setBoolean(4, futureBox.getOpen());
            pstmt.setObject(5, futureBox.getFutureMovieType());
            pstmt.setObject(6, futureBox.getFutureGifticonType());
            pstmt.setObject(7, futureBox.getFutureInventionType());
            pstmt.setTimestamp(8, futureBox.getCreatedTime());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureBox.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureBox failed, no ID obtained.");
            }
            return futureBox;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
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

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    public FutureBox findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_box WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                FutureBox futureBox = new FutureBox();
                futureBox.setId(rs.getLong("id"));
                futureBox.setUuid(java.util.UUID.fromString(rs.getString("uuid")));
                futureBox.setReceiver(rs.getString("receiver"));
                futureBox.setSender(rs.getString("sender"));
                futureBox.setOpen(rs.getBoolean("is_opened"));
                futureBox.setFutureMovieType(rs.getInt("future_movie_type"));
                futureBox.setFutureGifticonType(rs.getInt("future_gifticon_type"));
                futureBox.setFutureInventionType(rs.getInt("future_invention_type"));
                futureBox.setCreatedTime(rs.getTimestamp("created_at"));
                return futureBox;
            } else {
                throw new NoSuchElementException("No such future box with id: " + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public FutureBox findByUuid(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM future_box WHERE uuid = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, uuid);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                FutureBox futureBox = new FutureBox();
                futureBox.setId(rs.getLong("id"));
                futureBox.setUuid(java.util.UUID.fromString(rs.getString("uuid")));
                futureBox.setReceiver(rs.getString("receiver"));
                futureBox.setSender(rs.getString("sender"));
                futureBox.setOpen(rs.getBoolean("is_opened"));
                futureBox.setFutureMovieType(rs.getInt("future_movie_type"));
                futureBox.setFutureGifticonType(rs.getInt("future_gifticon_type"));
                futureBox.setFutureInventionType(rs.getInt("future_invention_type"));
                futureBox.setCreatedTime(rs.getTimestamp("created_at"));
                return futureBox;
            } else {
                throw new NoSuchElementException("No such future box with uuid: " + uuid);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureBox> findByReceiver(String receiver) throws SQLException {
        String sql = "SELECT * FROM future_box WHERE receiver = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, receiver);

            rs = pstmt.executeQuery();
            List<FutureBox> futureBoxes = new ArrayList<>();
            while (rs.next()) {
                FutureBox futureBox = new FutureBox();
                futureBox.setId(rs.getLong("id"));
                futureBox.setUuid(java.util.UUID.fromString(rs.getString("uuid")));
                futureBox.setReceiver(rs.getString("receiver"));
                futureBox.setSender(rs.getString("sender"));
                futureBox.setOpen(rs.getBoolean("is_opened"));
                futureBox.setFutureMovieType(rs.getInt("future_movie_type"));
                futureBox.setFutureGifticonType(rs.getInt("future_gifticon_type"));
                futureBox.setFutureInventionType(rs.getInt("future_invention_type"));
                futureBox.setCreatedTime(rs.getTimestamp("created_at"));
                futureBoxes.add(futureBox);
            }
            return futureBoxes;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureBox> findBySender(String sender) throws SQLException {
        String sql = "SELECT * FROM future_box WHERE sender = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, sender);

            rs = pstmt.executeQuery();
            List<FutureBox> futureBoxes = new ArrayList<>();
            while (rs.next()) {
                FutureBox futureBox = new FutureBox();
                futureBox.setId(rs.getLong("id"));
                futureBox.setUuid(java.util.UUID.fromString(rs.getString("uuid")));
                futureBox.setReceiver(rs.getString("receiver"));
                futureBox.setSender(rs.getString("sender"));
                futureBox.setOpen(rs.getBoolean("is_opened"));
                futureBox.setFutureMovieType(rs.getInt("future_movie_type"));
                futureBox.setFutureGifticonType(rs.getInt("future_gifticon_type"));
                futureBox.setFutureInventionType(rs.getInt("future_invention_type"));
                futureBox.setCreatedTime(rs.getTimestamp("created_at"));
                futureBoxes.add(futureBox);
            }
            return futureBoxes;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureBox> findAll(String sortField, String sortDirection) throws SQLException {
        String sql = "SELECT * FROM future_box ORDER BY " + 
                    (sortField != null ? sortField : "created_at") + 
                    " " + (sortDirection != null ? sortDirection : "DESC");
        Connection con = null;
        PreparedStatement ptsmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ptsmt = con.prepareStatement(sql);
            rs = ptsmt.executeQuery();

            List<FutureBox> futureBoxes = new ArrayList<>();
            while (rs.next()) {
                FutureBox futureBox = new FutureBox();
                futureBox.setId(rs.getLong("id"));
                futureBox.setUuid(java.util.UUID.fromString(rs.getString("uuid")));
                futureBox.setReceiver(rs.getString("receiver"));
                futureBox.setSender(rs.getString("sender"));
                futureBox.setOpen(rs.getBoolean("is_opened"));
                futureBox.setFutureMovieType(rs.getInt("future_movie_type"));
                futureBox.setFutureGifticonType(rs.getInt("future_gifticon_type"));
                futureBox.setFutureInventionType(rs.getInt("future_invention_type"));
                futureBox.setCreatedTime(rs.getTimestamp("created_at"));
                futureBoxes.add(futureBox);
            }
            return futureBoxes;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, ptsmt, rs);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_box WHERE id = ?";
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

    public void update(FutureBox futureBox) throws SQLException {
        String sql = "UPDATE future_box SET uuid = ?, receiver = ?, sender = ?, is_opened = ?, future_movie_type = ?, future_gifticon_type = ?, future_invention_type = ?, created_at = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, futureBox.getUuid());
            pstmt.setString(2, futureBox.getReceiver());
            pstmt.setString(3, futureBox.getSender());
            pstmt.setBoolean(4, futureBox.getOpen());
            pstmt.setInt(5, futureBox.getFutureMovieType());
            pstmt.setInt(6, futureBox.getFutureGifticonType());
            pstmt.setInt(7, futureBox.getFutureInventionType());
            pstmt.setTimestamp(8, futureBox.getCreatedTime());
            pstmt.setLong(9, futureBox.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }
}

