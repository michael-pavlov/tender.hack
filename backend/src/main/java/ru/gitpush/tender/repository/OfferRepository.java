package ru.gitpush.tender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gitpush.tender.model.Offer;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByCte_Id(Long cteId);

    @Query(value = "select ord from Offer ord where ord.cte.cteType = ?1  " +
            "and ord.cte.generation < ?2")
    List<Offer> findByCte_CteTypeAnfLessGeneration(String cte_type, Integer generation);
}
