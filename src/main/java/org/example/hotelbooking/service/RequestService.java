package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.entity.Request;
import org.example.hotelbooking.mappers.RequestMapper;
import org.example.hotelbooking.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestDTO create(RequestDTO dto) {
        Request entity = requestMapper.toEntity(dto);
        entity.setStatus("PENDING");
        Request saved = requestRepository.save(entity);
        return requestMapper.toDto(saved);
    }

    public List<RequestDTO> getByClient(Long clientId) {
        return requestRepository.findByClientId(clientId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    public List<RequestDTO> getAll() {
        return requestRepository.findAll().stream()
                .map(requestMapper::toDto)
                .toList();
    }

    public RequestDTO confirm(Long id) {
        Request req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        req.setStatus("CONFIRMED");
        return requestMapper.toDto(requestRepository.save(req));
    }
}
