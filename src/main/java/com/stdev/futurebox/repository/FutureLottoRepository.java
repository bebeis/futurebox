package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureLotto;
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
public class FutureLottoRepository {

    private final DataSource dataSource;

    public void save(FutureLotto futureLotto) throws SQLException {
        String sql = "INSERT INTO future_lotto (box_id, numbers) VALUES (?, ?) RETURNING id";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureLotto.getBoxId());
            pstmt.setObject(2, futureLotto.getNumbers());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureLotto.setId(rs.getLong("id"));
            } else {
                throw new SQLException("Creating FutureLotto failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public FutureLotto findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_lotto WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureLotto futureLotto = new FutureLotto();
                futureLotto.setId(rs.getLong("id"));
                futureLotto.setBoxId(rs.getLong("box_id"));
                
                Object[] objArray = (Object[]) rs.getArray("numbers").getArray();
                int[] intArray = new int[objArray.length];
                for (int i = 0; i < objArray.length; i++) {
                    intArray[i] = ((Integer) objArray[i]).intValue();
                }
                futureLotto.setNumbers(intArray);
                
                return futureLotto;
            } else {
                throw new NoSuchElementException("FutureLotto not found id=" + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public FutureLotto findByBoxId(Long boxId) throws SQLException {
        String sql = "SELECT * FROM future_lotto WHERE box_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, boxId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureLotto futureLotto = new FutureLotto();
                futureLotto.setId(rs.getLong("id"));
                futureLotto.setBoxId(rs.getLong("box_id"));
                
                Object[] objArray = (Object[]) rs.getArray("numbers").getArray();
                int[] intArray = new int[objArray.length];
                for (int i = 0; i < objArray.length; i++) {
                    intArray[i] = ((Integer) objArray[i]).intValue();
                }
                futureLotto.setNumbers(intArray);
                
                return futureLotto;
            }
            return null;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM future_lotto WHERE id = ?";
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

    public void update(FutureLotto futureLotto) throws SQLException {
        String sql = "UPDATE future_lotto SET box_id = ?, numbers = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, futureLotto.getBoxId());
            pstmt.setObject(2, futureLotto.getNumbers());
            pstmt.setLong(3, futureLotto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void deleteByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_lotto WHERE box_id = ?";
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
