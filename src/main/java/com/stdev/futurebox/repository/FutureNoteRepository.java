package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureNote;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class FutureNoteRepository {

    private final DataSource dataSource;

    public FutureNote save(FutureNote futureNote) throws SQLException {
        String sql = "INSERT INTO future_note (box_id, message) VALUES (?, ?) RETURNING id";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureNote.getBoxId());
            pstmt.setString(2, futureNote.getMessage());
            log.info("futureNote = {}", futureNote);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureNote.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureBox failed, no ID obtained.");
            }
            return futureNote;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureNote findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_note WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureNote futureNote = new FutureNote();
                futureNote.setId(rs.getLong("id"));
                futureNote.setBoxId(rs.getLong("box_id"));
                futureNote.setMessage(rs.getString("message"));
                return futureNote;
            } else {
                throw new NoSuchElementException("FutureNote not found id=" + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public FutureNote findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_note WHERE box_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureNote futureNote = new FutureNote();
                futureNote.setId(rs.getLong("id"));
                futureNote.setBoxId(rs.getLong("box_id"));
                futureNote.setMessage(rs.getString("message"));
                return futureNote;
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
        String sql = "DELETE FROM future_note WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
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
        String sql = "DELETE FROM future_note WHERE box_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
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

    public void update(FutureNote futureNote) throws SQLException {
        String sql = "UPDATE future_note SET box_id = ?, message = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureNote.getBoxId());
            pstmt.setString(2, futureNote.getMessage());
            pstmt.setLong(3, futureNote.getId());
            pstmt.executeUpdate();
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
}
