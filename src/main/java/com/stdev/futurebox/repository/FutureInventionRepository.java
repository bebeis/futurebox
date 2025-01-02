package com.stdev.futurebox.repository;

import com.stdev.futurebox.connection.DBConnectionUtil;
import com.stdev.futurebox.domain.FutureInvention;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FutureInventionRepository {

    private final DataSource dataSource;

    public void save(FutureInvention futureInvention) throws SQLException {
        String sql = "INSERT INTO future_invention_types (name, description, image_url, detail_image_url) VALUES (?, ?, ?, ?) RETURNING id";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureInvention.getName());
            pstmt.setString(2, futureInvention.getDescription());
            pstmt.setString(3, futureInvention.getImageUrl());
            pstmt.setString(4, futureInvention.getDetailImageUrl());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureInvention.setId(rs.getLong("id"));
            } else {
                throw new SQLException("발명품 생성 실패: ID를 받지 못했습니다.");
            }
        } catch (SQLException e) {
            log.error("데이터베이스 오류", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public FutureInvention findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_invention_types WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureInvention futureInvention = new FutureInvention();
                futureInvention.setId(rs.getLong("id"));
                futureInvention.setName(rs.getString("name"));
                futureInvention.setDescription(rs.getString("description"));
                futureInvention.setImageUrl(rs.getString("image_url"));
                futureInvention.setDetailImageUrl(rs.getString("detail_image_url"));
                return futureInvention;
            } else {
                throw new NoSuchElementException("FutureInvention not found with id: " + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureInvention> findAll() throws SQLException {
        String sql = "SELECT * FROM future_invention_types";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            List<FutureInvention> futureInventions = new ArrayList<>();

            while (rs.next()) {
                FutureInvention futureInvention = new FutureInvention();
                futureInvention.setId(rs.getLong("id"));
                futureInvention.setName(rs.getString("name"));
                futureInvention.setDescription(rs.getString("description"));
                futureInvention.setImageUrl(rs.getString("image_url"));
                futureInvention.setDetailImageUrl(rs.getString("detail_image_url"));
                futureInventions.add(futureInvention);
            }
            return futureInventions;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, stmt, rs);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM future_invention_types WHERE id = ?";
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

    public void update(FutureInvention futureInvention) throws SQLException {
        String sql = "UPDATE future_invention_types SET name = ?, description = ?, image_url = ?, detail_image_url = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureInvention.getName());
            pstmt.setString(2, futureInvention.getDescription());
            pstmt.setString(3, futureInvention.getImageUrl());
            pstmt.setString(4, futureInvention.getDetailImageUrl());
            pstmt.setLong(5, futureInvention.getId());
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
