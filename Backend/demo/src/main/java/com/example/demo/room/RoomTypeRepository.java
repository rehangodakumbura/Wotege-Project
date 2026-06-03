package com.example.demo.room;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

	Optional<RoomType> findByCode(String code);
}