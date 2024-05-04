package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveCustomerDto;
import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.ViewCustomerDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest-API контроллер для покупателей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Создать покупателя.
     *
     * @param saveCustomerDto Информация о новом покупателе.
     * @return Информация о созданном покупателе.
     */
    @PostMapping
    public ViewCustomerDto create(@RequestBody @Valid SaveCustomerDto saveCustomerDto) {
        return new ViewCustomerDto(customerService.createCustomer(saveCustomerDto));
    }
}
