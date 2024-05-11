package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.error.exception.CustomerNotFoundException;
import com.mediasoft.warehouse.model.Customer;
import com.mediasoft.warehouse.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для управления покупателями.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    /**
     * Получить покупателя по идентификатору.
     *
     * @param customerId Идентификатор покупателя.
     * @return Покупатель с указанным идентификатором.
     * @throws CustomerNotFoundException, если покупатель не найден.
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
