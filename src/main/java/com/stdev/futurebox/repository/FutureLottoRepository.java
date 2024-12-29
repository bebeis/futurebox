package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureLotto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureLottoRepository {
    public void save(FutureLotto futureLotto) throws SQLException {
        String sql = "INSERT INTO future_lotto (box_id, lotto_number) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
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
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureLotto futureLotto = new FutureLotto();
                futureLotto.setId(rs.getLong("id"));
                futureLotto.setBoxId(rs.getLong("box_id"));
                futureLotto.setNumbers((int[]) rs.getArray("lotto_number").getArray());
                return futureLotto;
            } else {
                throw new NoSuchElementException("FutureLotto not found id=" + id);
            }
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
}
