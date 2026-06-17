package com.wotege.hotel.repository;

import com.wotege.hotel.entity.PackageEntity;
import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    List<PackageEntity> findByCategory(PackageCategory category);

    List<PackageEntity> findByStatus(PackageStatus status);
}
