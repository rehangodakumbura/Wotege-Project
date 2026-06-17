package com.wotege.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDTO {

    private Long id;
    private Long roleId;
    private String action;
    private String performedBy;
    private String description;
    private LocalDateTime createdAt;
}
