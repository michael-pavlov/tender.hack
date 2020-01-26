package ru.gitpush.tender.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gitpush.tender.model.Order;
import ru.gitpush.tender.model.OrderStatus;
import ru.gitpush.tender.repository.OrderRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> findByCteId(Long cteId) {
        return orderRepository.findByCteId(cteId);
    }

    public List<Order> findIfCooperativeIsNotNullByType(String type, OrderStatus status, Long cteIs) {
        return orderRepository.findIfCooperativeIsNotNull(type, status, cteIs);
    }

    public List<Order> findByCte_CteTypeAndStatusAnfMoreGeneration(String cte_type, OrderStatus status, Integer generation) {
        return orderRepository.findByCte_CteTypeAndStatusAnfMoreGeneration(cte_type, status, generation);
    }

    public List<Order> findByCte_CteTypeAnfLessGeneration(String cte_type, Integer generation) {
        return orderRepository.findByCte_CteTypeAnfLessGeneration(cte_type, generation);
    }
}
