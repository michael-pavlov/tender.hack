package ru.gitpush.tender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gitpush.tender.model.Cte;

public interface CteRepository extends JpaRepository<Cte, Long> {
}
