package com.wotege.hotel.dto.packages;

import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageResponseDTO {

    private Long id;
    private String packageName;
    private PackageCategory category;
    private Double price;
    private String imageUrl;
    private List<String> includedItems;
    private LocalDate validFrom;
    private LocalDate validTo;
    private PackageStatus status;
    private Integer totalBookings;
}
