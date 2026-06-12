package com.example.demo.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Transactional(readOnly = true)
    public List<Customer> search(String query) {
        return customerRepository.search(query);
    }

    public Customer create(Customer payload) {
        if (payload.getEmail() != null) {
            customerRepository.findByEmail(payload.getEmail())
                    .ifPresent(c -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer with this email already exists");
                    });
        }
        Customer customer = new Customer();
        apply(payload, customer);
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer payload) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        apply(payload, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(existing);
    }

    public void delete(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        customerRepository.delete(existing);
    }

    public Customer incrementVisit(Long id, int nights, java.math.BigDecimal amount) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        customer.setTotalVisits(customer.getTotalVisits() + 1);
        customer.setTotalNights(customer.getTotalNights() + nights);
        customer.setTotalSpent(customer.getTotalSpent().add(amount));
        customer.setUpdatedAt(LocalDateTime.now());

        updateLoyaltyTier(customer);

        return customerRepository.save(customer);
    }

    private void updateLoyaltyTier(Customer customer) {
        int visits = customer.getTotalVisits();
        if (visits >= 20) {
            customer.setLoyaltyTier(LoyaltyTier.DIAMOND);
        } else if (visits >= 10) {
            customer.setLoyaltyTier(LoyaltyTier.PLATINUM);
        } else if (visits >= 5) {
            customer.setLoyaltyTier(LoyaltyTier.GOLD);
        } else if (visits >= 3) {
            customer.setLoyaltyTier(LoyaltyTier.SILVER);
        }
    }

    private void apply(Customer payload, Customer target) {
        target.setFirstName(payload.getFirstName());
        target.setLastName(payload.getLastName());
        target.setEmail(payload.getEmail());
        target.setPhone(payload.getPhone());
        target.setAddress(payload.getAddress());
        target.setDateOfBirth(payload.getDateOfBirth());
        target.setIdProofType(payload.getIdProofType());
        target.setIdProofNumber(payload.getIdProofNumber());
        if (payload.getLoyaltyTier() != null) {
            target.setLoyaltyTier(payload.getLoyaltyTier());
        }
    }
}
