package org.example.hotelbooking.repository;

import org.example.hotelbooking.entity.Booking;
import org.example.hotelbooking.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    public List<Booking> findByClientId(Long clientId);
}
