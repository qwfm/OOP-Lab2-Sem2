package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RequestMapper     requestMapper;

    public RequestDTO create(RequestDTO dto) {
        System.out.println(">> create(dto)=" + dto);
        Request entity = requestMapper.toEntity(dto);
        entity.setStatus("PENDING");
        Request saved = requestRepository.save(entity);
        RequestDTO out = requestMapper.toDto(saved);
        System.out.println("<< create returns " + out);
        return out;
    }

    public List<RequestDTO> getByClient(Long clientId) {
        System.out.println(">> getByClient(" + clientId + ")");
        List<RequestDTO> list = requestRepository.findByClientId(clientId).stream()
                .map(requestMapper::toDto)
                .toList();
        System.out.println("<< getByClient returns " + list.size() + " items");
        return list;
    }

    public List<RequestDTO> getAll() {
        System.out.println(">> getAll()");
        List<RequestDTO> list = requestRepository.findAll().stream()
                .map(requestMapper::toDto)
                .toList();
        System.out.println("<< getAll returns " + list.size() + " items");
        return list;
    }

    public RequestDTO confirm(Long id) {
        System.out.println(">> confirm(" + id + ")");
        Request req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        System.out.println("   found request: " + req);
        req.setStatus("CONFIRMED");
        Request saved = requestRepository.save(req);
        System.out.println("   saved request: " + saved);
        RequestDTO dto = requestMapper.toDto(saved);
        System.out.println("<< confirm returns " + dto);
        return dto;
    }

    public RequestDTO reject(Long id) {
        Request req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        System.out.println("   found request: " + req);
        req.setStatus("REJECTED");
        Request saved = requestRepository.save(req);
        System.out.println("   saved request: " + saved);
        RequestDTO dto = requestMapper.toDto(saved);
        System.out.println("<< reject returns " + dto);
        return dto;
    }
}

