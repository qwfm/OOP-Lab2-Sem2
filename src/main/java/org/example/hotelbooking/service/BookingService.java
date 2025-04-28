package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.BookingDTO;
import org.example.hotelbooking.dto.RequestDTO;
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
        RequestDTO confirmedRequest = requestService.confirm(dto.getRequestId());
        roomService.occupy(dto.getRoomId());
        Booking bookingEntity = bookingMapper.toEntity(dto);
        bookingEntity.setRequestId(dto.getRequestId());
        bookingEntity.setClientId(dto.getClientId());
        Booking saved = bookingRepository.save(bookingEntity);
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
