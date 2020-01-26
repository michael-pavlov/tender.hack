package ru.gitpush.tender.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gitpush.tender.model.Offer;
import ru.gitpush.tender.repository.OfferRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public List<Offer> findByCteId(Long id) {
        return offerRepository.findByCte_Id(id);
    }

    public List<Offer> findByCte_CteTypeAnfLessGeneration(String cte_type, Integer generation) {
        return offerRepository.findByCte_CteTypeAnfLessGeneration(cte_type, generation);
    }
}
