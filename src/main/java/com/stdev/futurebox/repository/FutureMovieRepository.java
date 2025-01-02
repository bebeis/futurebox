package com.stdev.futurebox.repository;

import com.stdev.futurebox.domain.FutureMovie;
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
public class FutureMovieRepository {

    private final DataSource dataSource;

    public void save(FutureMovie futureMovie) throws SQLException {
        String sql = "INSERT INTO future_movie_types (name, description, image_url, detail_image_url) VALUES (?, ?, ?, ?) RETURNING id";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureMovie.getName());
            pstmt.setString(2, futureMovie.getDescription());
            pstmt.setString(3, futureMovie.getImageUrl());
            pstmt.setString(4, futureMovie.getDetailImageUrl());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                futureMovie.setId(rs.getLong("id"));
            } else {
                throw new SQLException("영화 생성 실패: ID를 받지 못했습니다.");
            }
        } catch (SQLException e) {
            log.error("데이터베이스 오류", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public FutureMovie findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_movie_types WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                FutureMovie futureMovie = new FutureMovie();
                futureMovie.setId(rs.getLong("id"));
                futureMovie.setName(rs.getString("name"));
                futureMovie.setDescription(rs.getString("description"));
                futureMovie.setImageUrl(rs.getString("image_url"));
                futureMovie.setDetailImageUrl(rs.getString("detail_image_url"));
                return futureMovie;
            } else {
                throw new NoSuchElementException("FutureMovie with id " + id + " not found");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<FutureMovie> findAll() throws SQLException {
        String sql = "SELECT * FROM future_movie_types";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            List<FutureMovie> futureMovies = new ArrayList<>();
            while (rs.next()) {
                FutureMovie futureMovie = new FutureMovie();
                futureMovie.setId(rs.getLong("id"));
                futureMovie.setName(rs.getString("name"));
                futureMovie.setDescription(rs.getString("description"));
                futureMovie.setImageUrl(rs.getString("image_url"));
                futureMovie.setDetailImageUrl(rs.getString("detail_image_url"));
                futureMovies.add(futureMovie);
            }

            return futureMovies;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, stmt, rs);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM future_movie_types WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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

    public void update(FutureMovie futureMovie) throws SQLException {
        String sql = "UPDATE future_movie_types SET name = ?, description = ?, image_url = ?, detail_image_url = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, futureMovie.getName());
            pstmt.setString(2, futureMovie.getDescription());
            pstmt.setString(3, futureMovie.getImageUrl());
            pstmt.setString(4, futureMovie.getDetailImageUrl());
            pstmt.setLong(5, futureMovie.getId());
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
