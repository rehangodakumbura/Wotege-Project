package com.wotege.hotel.service;

import com.wotege.hotel.dto.packages.PackageRequestDTO;
import com.wotege.hotel.dto.packages.PackageResponseDTO;
import com.wotege.hotel.entity.PackageEntity;
import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.exception.ResourceNotFoundException;
import com.wotege.hotel.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Override
    public PackageResponseDTO createPackage(PackageRequestDTO request) {
        if (!request.isValidDateRange()) {
            throw new BadRequestException("Valid to date cannot be before valid from date");
        }

        PackageEntity entity = PackageEntity.builder()
                .packageName(request.getPackageName())
                .category(request.getCategory())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .includedItems(request.getIncludedItems())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .status(request.getStatus())
                .totalBookings(0)
                .build();

        PackageEntity saved = packageRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<PackageResponseDTO> getAllPackages() {
        return packageRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PackageResponseDTO getPackageById(Long id) {
        PackageEntity entity = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        return mapToDTO(entity);
    }

    @Override
    public PackageResponseDTO updatePackage(Long id, PackageRequestDTO request) {
        PackageEntity entity = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        if (!request.isValidDateRange()) {
            throw new BadRequestException("Valid to date cannot be before valid from date");
        }

        entity.setPackageName(request.getPackageName());
        entity.setCategory(request.getCategory());
        entity.setPrice(request.getPrice());
        if (request.getImageUrl() != null) {
            entity.setImageUrl(request.getImageUrl());
        }
        entity.setIncludedItems(request.getIncludedItems());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setStatus(request.getStatus());

        PackageEntity saved = packageRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public void deletePackage(Long id) {
        PackageEntity entity = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        packageRepository.delete(entity);
    }

    @Override
    public PackageResponseDTO changeStatus(Long id, PackageStatus status) {
        PackageEntity entity = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        entity.setStatus(status);
        PackageEntity saved = packageRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<PackageResponseDTO> getPackagesByCategory(PackageCategory category) {
        return packageRepository.findByCategory(category)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PackageResponseDTO mapToDTO(PackageEntity entity) {
        return PackageResponseDTO.builder()
                .id(entity.getId())
                .packageName(entity.getPackageName())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .includedItems(entity.getIncludedItems())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .status(entity.getStatus())
                .totalBookings(entity.getTotalBookings())
                .build();
    }
}
