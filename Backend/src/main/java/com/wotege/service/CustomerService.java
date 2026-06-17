package com.wotege.service;

import com.wotege.dto.CustomerRequestDTO;
import com.wotege.dto.CustomerResponseDTO;
import com.wotege.dto.DashboardStatisticsDTO;
import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(Long id);

    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);

    void deleteCustomer(Long id);

    CustomerResponseDTO changeStatus(Long id, CustomerStatus status);

    List<CustomerResponseDTO> getCustomersByTier(LoyaltyTier tier);

    List<CustomerResponseDTO> getVipCustomers();

    List<CustomerResponseDTO> searchCustomers(String keyword);

    DashboardStatisticsDTO getDashboardStatistics();
}
