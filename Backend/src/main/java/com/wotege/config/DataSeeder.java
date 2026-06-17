package com.wotege.config;

import com.wotege.entity.Category;
import com.wotege.entity.MenuItem;
import com.wotege.entity.PermissionEntity;
import com.wotege.entity.RoleEntity;
import com.wotege.enums.Department;
import com.wotege.enums.RoleStatus;
import com.wotege.enums.RoleType;
import com.wotege.repository.CategoryRepository;
import com.wotege.repository.MenuItemRepository;
import com.wotege.repository.PermissionRepository;
import com.wotege.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private static final List<String> DEFAULT_MODULES = List.of(
            "Dashboard Analytics",
            "Reservation Module",
            "Room Management",
            "Restaurant POS",
            "Menu Management",
            "Customer Database",
            "Staff Management",
            "Inventory System",
            "Reports & Insights",
            "System Settings"
    );

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        seedMenuData();
        seedRoles();
    }

    private void seedMenuData() {
        if (categoryRepository.count() > 0) {
            log.info("Menu data already seeded, skipping...");
            return;
        }

        log.info("Seeding menu data...");

        List<Category> categories = categoryRepository.saveAll(List.of(
                createCategory("Starters"),
                createCategory("Mains"),
                createCategory("Desserts"),
                createCategory("Beverages"),
                createCategory("Alcohol"),
                createCategory("Specials")
        ));

        Category starters = categories.get(0);
        Category mains = categories.get(1);
        Category desserts = categories.get(2);
        Category beverages = categories.get(3);
        Category alcohol = categories.get(4);
        Category specials = categories.get(5);

        menuItemRepository.saveAll(List.of(
                createMenuItem("Wagyu Beef Steak", 8500.0, true, 124, mains, "https://images.unsplash.com/photo-1546241072-48010ad168d5?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Truffle Risotto", 4200.0, true, 89, mains, "https://images.unsplash.com/photo-1626379616459-b2ce1d9decbc?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Lobster Bisque", 2800.0, false, 112, starters, "https://images.unsplash.com/photo-1548943487-a2e4143fa723?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Gold-leaf Sushi Roll", 5500.0, true, 56, mains, "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Dom Perignon 2012", 29000.0, true, 20, alcohol, "https://images.unsplash.com/photo-1590740924976-5a415ffcc934?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Matcha Tiramisu", 1800.0, true, 201, desserts, "https://images.unsplash.com/photo-1571115177098-24ec42ed204d?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Crispy Calamari", 1500.0, true, 67, starters, "https://images.unsplash.com/photo-1599487488170-d11ec9c172f0?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Caesar Salad", 1200.0, true, 45, starters, "https://images.unsplash.com/photo-1546793665-c74683f339c1?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Grilled Salmon", 6500.0, true, 78, mains, "https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Chocolate Lava Cake", 2200.0, true, 156, desserts, "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Fresh Juice", 800.0, true, 234, beverages, "https://images.unsplash.com/photo-1622597467836-f3285f2131b8?q=80&w=300&auto=format&fit=crop"),
                createMenuItem("Espresso Martini", 2500.0, true, 89, alcohol, "https://images.unsplash.com/photo-1587235442320-5c1de0e5f998?q=80&w=300&auto=format&fit=crop")
        ));

        log.info("Database seeded successfully with sample data");
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            log.info("Roles already seeded, skipping...");
            return;
        }

        log.info("Seeding default system roles...");

        List<RoleEntity> roles = roleRepository.saveAll(List.of(
                createRole("R-001", "Super Admin", "Full access to all system modules and settings.",
                        Department.GENERAL_ADMINISTRATION, RoleType.SYSTEM_ROLE, RoleStatus.ACTIVE),
                createRole("R-002", "Hotel Manager", "Manages hotel operations, reservations, and room assignments.",
                        Department.HOTEL_OPERATIONS, RoleType.SYSTEM_ROLE, RoleStatus.ACTIVE),
                createRole("R-003", "Restaurant Manager", "Oversees restaurant POS, menu management, and kitchen operations.",
                        Department.RESTAURANT_OPERATIONS, RoleType.SYSTEM_ROLE, RoleStatus.ACTIVE),
                createRole("R-004", "Receptionist", "Handles front desk, guest check-in/out, and reservations.",
                        Department.FRONT_DESK, RoleType.SYSTEM_ROLE, RoleStatus.ACTIVE),
                createRole("R-005", "Kitchen Staff", "Manages kitchen operations, inventory, and food preparation.",
                        Department.KITCHEN, RoleType.SYSTEM_ROLE, RoleStatus.ACTIVE)
        ));

        for (RoleEntity role : roles) {
            List<PermissionEntity> permissions = createPermissionsForRole(role);
            permissionRepository.saveAll(permissions);
        }

        log.info("Default system roles seeded successfully");
    }

    private List<PermissionEntity> createPermissionsForRole(RoleEntity role) {
        List<Boolean[]> permissionMatrix = getPermissionMatrix(role.getRoleName());
        List<PermissionEntity> permissions = new ArrayList<>();
        for (int i = 0; i < DEFAULT_MODULES.size(); i++) {
            Boolean[] perms = permissionMatrix.get(i);
            permissions.add(PermissionEntity.builder()
                    .role(role)
                    .moduleName(DEFAULT_MODULES.get(i))
                    .canView(perms[0])
                    .canCreate(perms[1])
                    .canEdit(perms[2])
                    .canDelete(perms[3])
                    .build());
        }
        return permissions;
    }

    private List<Boolean[]> getPermissionMatrix(String roleName) {
        // [VIEW, CREATE, EDIT, DELETE]
        return switch (roleName) {
            case "Super Admin" -> List.of(
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true}
            );
            case "Hotel Manager" -> List.of(
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, false, false},
                    new Boolean[]{true, false, false, false},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, false, false, false}
            );
            case "Restaurant Manager" -> List.of(
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, false, false, false}
            );
            case "Receptionist" -> List.of(
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{true, true, true, true},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false}
            );
            case "Kitchen Staff" -> List.of(
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{true, true, true, false},
                    new Boolean[]{true, true, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{true, false, false, false},
                    new Boolean[]{false, false, false, false},
                    new Boolean[]{false, false, false, false}
            );
            default -> DEFAULT_MODULES.stream()
                    .map(m -> new Boolean[]{false, false, false, false})
                    .toList();
        };
    }

    private RoleEntity createRole(String roleCode, String roleName, String description,
                                   Department department, RoleType roleType, RoleStatus status) {
        RoleEntity role = new RoleEntity();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setDepartment(department);
        role.setRoleType(roleType);
        role.setStatus(status);
        role.setUserCount(0);
        return role;
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    private MenuItem createMenuItem(String name, double price, boolean available, int orderCount,
                                     Category category, String imageUrl) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setPrice(price);
        item.setAvailable(available);
        item.setOrderCount(orderCount);
        item.setCategory(category);
        item.setImageUrl(imageUrl);
        return item;
    }
}
