package com.wotege.service.impl;

import com.wotege.dto.CustomerRequestDTO;
import com.wotege.dto.CustomerResponseDTO;
import com.wotege.dto.DashboardStatisticsDTO;
import com.wotege.entity.CustomerEntity;
import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import com.wotege.exception.DuplicateResourceException;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.CustomerRepository;
import com.wotege.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (customerRepository.findByCustomerCode(requestDTO.getCustomerCode()).isPresent()) {
            throw new DuplicateResourceException("Customer code already exists: " + requestDTO.getCustomerCode());
        }
        if (customerRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }

        CustomerEntity entity = mapToEntity(requestDTO);
        entity = customerRepository.save(entity);
        return mapToResponseDTO(entity);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!entity.getCustomerCode().equals(requestDTO.getCustomerCode())) {
            if (customerRepository.findByCustomerCode(requestDTO.getCustomerCode()).isPresent()) {
                throw new DuplicateResourceException("Customer code already exists: " + requestDTO.getCustomerCode());
            }
        }
        if (!entity.getEmail().equals(requestDTO.getEmail())) {
            if (customerRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
            }
        }

        entity.setCustomerCode(requestDTO.getCustomerCode());
        entity.setFullName(requestDTO.getFullName());
        entity.setEmail(requestDTO.getEmail());
        entity.setPhoneNumber(requestDTO.getPhoneNumber());
        entity.setLoyaltyTier(requestDTO.getLoyaltyTier());
        entity.setTotalSpent(requestDTO.getTotalSpent());
        entity.setVisitCount(requestDTO.getVisitCount());
        entity.setIsVip(requestDTO.getIsVip());
        entity.setLastVisitDate(requestDTO.getLastVisitDate());
        entity.setStatus(requestDTO.getStatus());

        entity = customerRepository.save(entity);
        return mapToResponseDTO(entity);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(entity);
    }

    @Override
    @Transactional
    public CustomerResponseDTO changeStatus(Long id, CustomerStatus status) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        entity.setStatus(status);
        entity = customerRepository.save(entity);
        return mapToResponseDTO(entity);
    }

    @Override
    public List<CustomerResponseDTO> getCustomersByTier(LoyaltyTier tier) {
        return customerRepository.findByLoyaltyTier(tier)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponseDTO> getVipCustomers() {
        return customerRepository.findByIsVipTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        return customerRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DashboardStatisticsDTO getDashboardStatistics() {
        long totalCustomers = customerRepository.count();
        long activeVipCustomers = customerRepository.countByIsVipTrueAndStatus(CustomerStatus.ACTIVE);

        List<CustomerEntity> all = customerRepository.findAll();
        double averageLtv = all.stream()
                .mapToDouble(c -> c.getTotalSpent() != null ? c.getTotalSpent() : 0.0)
                .average()
                .orElse(0.0);

        return DashboardStatisticsDTO.builder()
                .totalCustomers(totalCustomers)
                .activeVipCustomers(activeVipCustomers)
                .averageLtv(averageLtv)
                .build();
    }

    private CustomerEntity mapToEntity(CustomerRequestDTO dto) {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerCode(dto.getCustomerCode());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setLoyaltyTier(dto.getLoyaltyTier());
        entity.setTotalSpent(dto.getTotalSpent() != null ? dto.getTotalSpent() : 0.0);
        entity.setVisitCount(dto.getVisitCount() != null ? dto.getVisitCount() : 0);
        entity.setIsVip(dto.getIsVip() != null ? dto.getIsVip() : false);
        entity.setLastVisitDate(dto.getLastVisitDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private CustomerResponseDTO mapToResponseDTO(CustomerEntity entity) {
        return CustomerResponseDTO.builder()
                .id(entity.getId())
                .customerCode(entity.getCustomerCode())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .loyaltyTier(entity.getLoyaltyTier())
                .totalSpent(entity.getTotalSpent())
                .visitCount(entity.getVisitCount())
                .isVip(entity.getIsVip())
                .lastVisitDate(entity.getLastVisitDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
