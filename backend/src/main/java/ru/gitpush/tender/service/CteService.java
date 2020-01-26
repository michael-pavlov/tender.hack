package ru.gitpush.tender.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gitpush.tender.model.Cte;
import ru.gitpush.tender.repository.CteRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CteService {
    final CteRepository cteRepository;

    public Cte findById(Long id) {
        return cteRepository.findById(id).orElse(null);
    }
}
