package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.BookingDTO;
import org.example.hotelbooking.entity.Booking;
import org.example.hotelbooking.mappers.BookingMapper;
import org.example.hotelbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final RoomService roomService;
    private final RequestService requestService;

    public BookingDTO create(BookingDTO dto) {
        requestService.confirm(dto.getId());
        roomService.occupy(dto.getRoomId());
        Booking entity = bookingMapper.toEntity(dto);
        Booking saved = bookingRepository.save(entity);
        return bookingMapper.toDto(saved);
    }

    public List<BookingDTO> getByClient(Long clientId) {
        return bookingRepository.findByClientId(clientId).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    public List<BookingDTO> getAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    public void cancel(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        roomService.free(b.getRoomId());
        bookingRepository.deleteById(id);
    }
}
