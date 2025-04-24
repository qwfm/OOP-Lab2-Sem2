package org.example.hotelbooking.repository;

import org.example.hotelbooking.entity.Request;
import org.example.hotelbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByClientId(Long clientId );
}