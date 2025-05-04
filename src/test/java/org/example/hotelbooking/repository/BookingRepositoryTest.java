package org.example.hotelbooking.repository;

import org.example.hotelbooking.entity.Booking;
import org.example.hotelbooking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.liquibase.enabled=false"
        }
)
@AutoConfigureTestDatabase
class BookingRepositoryTest {
    @Autowired
    BookingRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void findByClientId_returnsCorrectList() {
        Booking b1 = Booking.builder()
                .clientId(1L)
                .requestId(10L)
                .roomId(8L)
                .totalPrice(new BigDecimal("100.00"))
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(1))
                .build();

        em.persist(b1);
        em.flush();

        List<Booking> result = repo.findByClientId(1L);

        assertThat(result)
                .hasSize(1)
                .containsExactly(b1);
    }
}
