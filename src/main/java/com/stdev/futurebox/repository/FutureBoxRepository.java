package com.stdev.futurebox.repository;

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
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.datasource.DataSourceUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FutureBoxRepository {

    private final DataSource dataSource;

    public void save(FutureBox box) throws SQLException {
        String sql = "INSERT INTO future_box (receiver, sender, is_opened, future_gifticon_type, " +
                    "future_value_meter_included, created_at, uuid) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, box.getReceiver());
            pstmt.setString(2, box.getSender());
            pstmt.setBoolean(3, box.getIsOpened());
            pstmt.setObject(4, box.getFutureGifticonType());
            pstmt.setBoolean(5, box.getFutureValueMeterIncluded());
            pstmt.setTimestamp(6, box.getCreatedTime());
            pstmt.setObject(7, box.getUuid());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                box.setId(rs.getLong("id"));
            } else {
                throw new SQLException("FutureBox 생성 실패");
            }
        } finally {
            close(null, pstmt, rs);
        }
    }

    public List<FutureBox> findAll(String orderBy, String direction) throws SQLException {
        String sql = "SELECT * FROM future_box ORDER BY " + orderBy + " " + direction;
        List<FutureBox> futureBoxes = new ArrayList<>();
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                FutureBox futureBox = mapToBox(rs);
                futureBoxes.add(futureBox);
            }
            return futureBoxes;
        } finally {
            close(null, pstmt, rs);
        }
    }

    public FutureBox findById(Long id) throws SQLException {
        String sql = "SELECT * FROM future_box WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToBox(rs);
            }
            throw new NoSuchElementException("해당 ID의 FutureBox가 없습니다: " + id);
        } finally {
            close(null, pstmt, rs);
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
                return mapToBox(rs);
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
                FutureBox futureBox = mapToBox(rs);
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
                FutureBox futureBox = mapToBox(rs);
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

    public List<FutureBox> findByKeyword(String searchType, String keyword, Boolean isOpened) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM future_box WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND ").append(searchType).append(" LIKE ?");
            params.add("%" + keyword + "%");
        }

        if (isOpened != null) {
            sql.append(" AND is_opened = ?");
            params.add(isOpened);
        }

        List<FutureBox> results = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FutureBox futureBox = mapToBox(rs);
                    results.add(futureBox);
                }
            }
        }
        return results;
    }

    public void deleteById(Long id) throws SQLException {
        deleteLogsByBoxId(id);
        
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

    public void update(FutureBox box) throws SQLException {
        String sql = "UPDATE future_box SET receiver = ?, sender = ?, is_opened = ?, " +
                    "future_gifticon_type = ?, future_value_meter_included = ?, " +
                    "created_at = ? WHERE id = ?";
        
        Connection con = DataSourceUtils.getConnection(dataSource);
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, box.getReceiver());
            pstmt.setString(2, box.getSender());
            pstmt.setBoolean(3, box.getIsOpened());
            pstmt.setObject(4, box.getFutureGifticonType());
            pstmt.setBoolean(5, box.getFutureValueMeterIncluded());
            pstmt.setTimestamp(6, box.getCreatedTime());
            pstmt.setLong(7, box.getId());
            pstmt.executeUpdate();
        } finally {
            close(null, pstmt, null);
        }
    }

    public void deleteLogsByBoxId(Long boxId) throws SQLException {
        String sql = "DELETE FROM future_box_logs WHERE box_id = ?";
        
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

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_box";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        }
    }

    public long countByOpenTrue() throws SQLException {
        String sql = "SELECT COUNT(*) FROM future_box WHERE is_opened = true";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        }
    }

    private FutureBox mapToBox(ResultSet rs) throws SQLException {
        FutureBox box = new FutureBox();
        box.setId(rs.getLong("id"));
        box.setReceiver(rs.getString("receiver"));
        box.setSender(rs.getString("sender"));
        box.setIsOpened(rs.getBoolean("is_opened"));
        box.setFutureGifticonType((Integer) rs.getObject("future_gifticon_type"));
        box.setFutureValueMeterIncluded(rs.getBoolean("future_value_meter_included"));
        box.setCreatedTime(rs.getTimestamp("created_at"));
        box.setUuid((UUID) rs.getObject("uuid"));
        return box;
    }

    private Connection getConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        return connection;
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
    }
}


