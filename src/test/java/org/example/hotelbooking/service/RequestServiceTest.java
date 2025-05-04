package org.example.hotelbooking.service;

import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.entity.Request;
import org.example.hotelbooking.mappers.RequestMapper;
import org.example.hotelbooking.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    RequestRepository repo;
    @Mock
    RequestMapper mapper;
    @InjectMocks
    RequestService service;

    @Test
    void confirm_existingRequest_statusUpdated() {
        Request req = Request.builder().id(5L).status("PENDING").build();
        when(repo.findById(5L)).thenReturn(Optional.of(req));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(req)).thenReturn(new RequestDTO(5L, null, null, null, "CONFIRMED", null, null));

        RequestDTO dto = service.confirm(5L);

        assertEquals("CONFIRMED", dto.getStatus());
        verify(repo).save(req);
    }

    @Test
    void reject_existingRequest_logsAndReturns() {
        Request req = Request.builder().id(7L).status("PENDING").build();
        when(repo.findById(7L)).thenReturn(Optional.of(req));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(req)).thenReturn(new RequestDTO(7L, null, null, null, "REJECTED", null, null));

        RequestDTO dto = service.reject(7L);

        assertEquals("REJECTED", dto.getStatus());
    }

}
