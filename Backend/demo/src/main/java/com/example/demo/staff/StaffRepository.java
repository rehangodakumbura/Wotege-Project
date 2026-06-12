package com.example.demo.staff;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStatus(StaffStatus status);

    List<Staff> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Staff s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Staff> search(@Param("search") String search);

    long countByStatus(StaffStatus status);
}
