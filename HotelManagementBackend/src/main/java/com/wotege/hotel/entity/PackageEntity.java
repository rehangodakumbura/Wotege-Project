package com.wotege.hotel.entity;

import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name = "offers_packages")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private PackageCategory category;

    @Column(name = "price", precision = 10, scale = 2)
    private Double price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "included_items", columnDefinition = "TEXT[]")
    private List<String> includedItems;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PackageStatus status;

    @Column(name = "total_bookings")
    @Builder.Default
    private Integer totalBookings = 0;
}
