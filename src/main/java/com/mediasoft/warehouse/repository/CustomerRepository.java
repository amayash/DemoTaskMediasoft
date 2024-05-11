package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для {@link Customer}.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
