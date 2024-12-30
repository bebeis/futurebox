package com.stdev.futurebox.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.stdev.futurebox.domain.FutureBox;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class FutureBoxRepositoryTest {

    FutureBoxRepository repository = new FutureBoxRepository();

    @Test
    void save() throws SQLException {
        FutureBox futureBox = new FutureBox(UUID.randomUUID(), "receiver", "sender", false, 1, 2, 3, Timestamp.valueOf(
                LocalDateTime.now()));

        // save 메서드를 호출하여 데이터베이스에 저장하고, 생성된 ID를 설정
        FutureBox savedFutureBox = repository.save(futureBox);

        // ID가 정상적으로 설정되었는지 검증
        assertThat(savedFutureBox.getId()).isNotNull();

        // findById 메서드를 사용하여 저장된 데이터를 조회
        FutureBox findFutureBox = repository.findById(savedFutureBox.getId());

        // 저장된 객체와 조회된 객체가 동일한지 검증
        assertThat(findFutureBox).isEqualTo(savedFutureBox);
    }


}