package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.model.Customer;

/**
 * Интерфейс для сервиса управления покупателями.
 */
public interface CustomerService {
    Customer getCustomerById(Long customerId);
}
