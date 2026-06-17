package com.wotege.repository;

import com.wotege.entity.CustomerEntity;
import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    List<CustomerEntity> findByLoyaltyTier(LoyaltyTier tier);

    List<CustomerEntity> findByStatus(CustomerStatus status);

    List<CustomerEntity> findByIsVipTrue();

    Optional<CustomerEntity> findByCustomerCode(String customerCode);

    Optional<CustomerEntity> findByEmail(String email);

    @Query("SELECT c FROM CustomerEntity c WHERE " +
           "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CustomerEntity> searchByKeyword(@Param("keyword") String keyword);

    long countByIsVipTrueAndStatus(CustomerStatus status);
}
