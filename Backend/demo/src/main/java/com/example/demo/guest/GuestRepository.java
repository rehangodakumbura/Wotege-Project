package com.example.demo.guest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("SELECT g FROM Guest g WHERE LOWER(g.fullName) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR g.phone LIKE CONCAT('%', :q, '%') " +
           "OR LOWER(g.email) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Guest> search(@Param("q") String q);
}
