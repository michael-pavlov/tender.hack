package ru.gitpush.tender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gitpush.tender.model.Order;
import ru.gitpush.tender.model.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCteId(Long id);

    @Query(value = "select ord from Order ord where ord.cooperativeId is not null and ord.cte.cteType = ?1 and ord.status = ?2 and ord.cte.id <> ?3")
    List<Order> findIfCooperativeIsNotNull(String cte_type, OrderStatus status, Long cteIs);

    @Query(value = "select ord from Order ord where ord.cte.cteType = ?1 and ord.status = ?2 " +
            "and ord.cte.generation > ?3")
    List<Order> findByCte_CteTypeAndStatusAnfMoreGeneration(String cte_type, OrderStatus status, Integer generation);

    @Query(value = "select ord from Order ord where ord.cte.cteType = ?1  " +
            "and ord.cte.generation < ?2")
    List<Order> findByCte_CteTypeAnfLessGeneration(String cte_type, Integer generation);
}
