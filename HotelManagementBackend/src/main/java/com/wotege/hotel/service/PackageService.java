package com.wotege.hotel.service;

import com.wotege.hotel.dto.packages.PackageRequestDTO;
import com.wotege.hotel.dto.packages.PackageResponseDTO;
import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;

import java.util.List;

public interface PackageService {

    PackageResponseDTO createPackage(PackageRequestDTO request);

    List<PackageResponseDTO> getAllPackages();

    PackageResponseDTO getPackageById(Long id);

    PackageResponseDTO updatePackage(Long id, PackageRequestDTO request);

    void deletePackage(Long id);

    PackageResponseDTO changeStatus(Long id, PackageStatus status);

    List<PackageResponseDTO> getPackagesByCategory(PackageCategory category);
}
