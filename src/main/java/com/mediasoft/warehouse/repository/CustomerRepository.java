package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для {@link Customer}.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Получить информацию о том, существует ли покупатель с указанным логином.
     *
     * @param login Логин для проверки.
     * @return Флаг, указывающий существует ли покупатель с указанным логином.
     */
    boolean existsByLogin(String login);
}
