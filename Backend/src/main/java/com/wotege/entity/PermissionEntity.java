package com.wotege.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(name = "module_name", length = 100)
    private String moduleName;

    @Column(name = "can_view")
    @Builder.Default
    private Boolean canView = false;

    @Column(name = "can_create")
    @Builder.Default
    private Boolean canCreate = false;

    @Column(name = "can_edit")
    @Builder.Default
    private Boolean canEdit = false;

    @Column(name = "can_delete")
    @Builder.Default
    private Boolean canDelete = false;
}
