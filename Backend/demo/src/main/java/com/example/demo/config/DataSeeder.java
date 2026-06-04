package com.example.demo.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;

import com.example.demo.audit.AuditLog;
import com.example.demo.audit.AuditLogRepository;
import com.example.demo.auth.UserAccount;
import com.example.demo.auth.UserAccountRepository;
import com.example.demo.branch.Branch;
import com.example.demo.branch.BranchRepository;
import com.example.demo.branch.BranchStatus;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.customer.LoyaltyTier;
import com.example.demo.inventory.InventoryItem;
import com.example.demo.inventory.InventoryItemRepository;
import com.example.demo.inventory.InventoryMovement;
import com.example.demo.inventory.InventoryMovementRepository;
import com.example.demo.inventory.MovementType;
import com.example.demo.inventory.StockStatus;
import com.example.demo.menu.MenuCategory;
import com.example.demo.menu.MenuCategoryRepository;
import com.example.demo.menu.MenuItem;
import com.example.demo.menu.MenuItemRepository;
import com.example.demo.order.Order;
import com.example.demo.order.OrderItem;
import com.example.demo.order.OrderItemRepository;
import com.example.demo.order.OrderRepository;
import com.example.demo.order.OrderStatus;
import com.example.demo.order.OrderType;
import com.example.demo.payment.Payment;
import com.example.demo.payment.PaymentMethod;
import com.example.demo.payment.PaymentRepository;
import com.example.demo.payment.PaymentStatus;
import com.example.demo.reservation.BookingType;
import com.example.demo.reservation.Reservation;
import com.example.demo.reservation.ReservationRepository;
import com.example.demo.reservation.ReservationStatus;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.example.demo.room.Room;
import com.example.demo.room.RoomRepository;
import com.example.demo.room.RoomStatus;
import com.example.demo.room.RoomType;
import com.example.demo.room.RoomTypeRepository;
import com.example.demo.staff.Staff;
import com.example.demo.staff.StaffRepository;
import com.example.demo.staff.StaffStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

	private final String adminEmail;
	private final String adminPassword;
	private final MenuCategoryRepository menuCategoryRepository;
	private final MenuItemRepository menuItemRepository;

	public DataSeeder(
		@Value("${app.admin-email:rehan2003@gmail.com}") String adminEmail,
		@Value("${app.admin-password:rehan2003}") String adminPassword,
		MenuCategoryRepository menuCategoryRepository,
		MenuItemRepository menuItemRepository
	) {
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
		this.menuCategoryRepository = menuCategoryRepository;
		this.menuItemRepository = menuItemRepository;
	}

	@Bean
	CommandLineRunner seedDemoData(
		RoleRepository roleRepository,
		RoomTypeRepository roomTypeRepository,
		RoomRepository roomRepository,
		ReservationRepository reservationRepository,
		UserAccountRepository userAccountRepository,
		BranchRepository branchRepository,
		MenuCategoryRepository menuCategoryRepository,
		MenuItemRepository menuItemRepository,
		CustomerRepository customerRepository,
		OrderRepository orderRepository,
		OrderItemRepository orderItemRepository,
		PaymentRepository paymentRepository,
		AuditLogRepository auditLogRepository,
		InventoryItemRepository inventoryItemRepository,
		InventoryMovementRepository inventoryMovementRepository,
		StaffRepository staffRepository
	) {
		return args -> {
			Role superAdmin = roleRepository.findByCode("SUPER_ADMIN").orElseGet(() -> {
				Role role = new Role();
				role.setCode("SUPER_ADMIN");
				role.setName("Super Admin");
				role.setDescription("Full access to all system modules and settings.");
				role.setSystemRole(true);
				return roleRepository.save(role);
			});

			if (roleRepository.count() == 1) {
				Role hotelManager = new Role();
				hotelManager.setCode("HOTEL_MANAGER");
				hotelManager.setName("Hotel Manager");
				hotelManager.setDescription("Access to hotel operations, bookings, staff, and basic reports.");
				roleRepository.save(hotelManager);

				Role restaurantManager = new Role();
				restaurantManager.setCode("RESTAURANT_MANAGER");
				restaurantManager.setName("Restaurant Manager");
				restaurantManager.setDescription("Access to POS, menu management, and restaurant reports.");
				roleRepository.save(restaurantManager);

				Role receptionist = new Role();
				receptionist.setCode("RECEPTIONIST");
				receptionist.setName("Receptionist");
				receptionist.setDescription("Access to reservations, room status, and guest profiles.");
				roleRepository.save(receptionist);

				Role kitchenStaff = new Role();
				kitchenStaff.setCode("KITCHEN_STAFF");
				kitchenStaff.setName("Kitchen Staff");
				kitchenStaff.setDescription("Access to kitchen dashboard and ingredient inventory.");
				roleRepository.save(kitchenStaff);
			}

			RoomType standard = roomTypeRepository.findByCode("STANDARD").orElseGet(() -> {
				RoomType roomType = new RoomType();
				roomType.setCode("STANDARD");
				roomType.setName("Standard Room");
				roomType.setBaseRate(15000);
				roomType.setBeds(1);
				roomType.setCapacity(2);
				roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Fridge")));
				return roomTypeRepository.save(roomType);
			});

			RoomType deluxe = roomTypeRepository.findByCode("DELUXE").orElseGet(() -> {
				RoomType roomType = new RoomType();
				roomType.setCode("DELUXE");
				roomType.setName("Deluxe Suite");
				roomType.setBaseRate(35000);
				roomType.setBeds(2);
				roomType.setCapacity(4);
				roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony")));
				return roomTypeRepository.save(roomType);
			});

			RoomType presidential = roomTypeRepository.findByCode("PRESIDENTIAL").orElseGet(() -> {
				RoomType roomType = new RoomType();
				roomType.setCode("PRESIDENTIAL");
				roomType.setName("Presidential");
				roomType.setBaseRate(85000);
				roomType.setBeds(3);
				roomType.setCapacity(6);
				roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony", "Private Pool", "Jacuzzi")));
				return roomTypeRepository.save(roomType);
			});

			RoomType penthouse = roomTypeRepository.findByCode("PENTHOUSE").orElseGet(() -> {
				RoomType roomType = new RoomType();
				roomType.setCode("PENTHOUSE");
				roomType.setName("Penthouse");
				roomType.setBaseRate(150000);
				roomType.setBeds(4);
				roomType.setCapacity(8);
				roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony", "Jacuzzi", "Private Pool")));
				return roomTypeRepository.save(roomType);
			});

			ensureRoom(roomRepository, "101", 1, 35000, 2, RoomStatus.AVAILABLE, deluxe, java.util.List.of("Sea View", "Balcony"), null);
			ensureRoom(roomRepository, "102", 1, 35000, 2, RoomStatus.OCCUPIED, deluxe, java.util.List.of("Balcony"), "Alex M.");
			ensureRoom(roomRepository, "201", 2, 85000, 3, RoomStatus.CLEANING, presidential, java.util.List.of("Sea View", "Private Pool", "Balcony"), null);
			ensureRoom(roomRepository, "202", 2, 15000, 1, RoomStatus.AVAILABLE, standard, java.util.List.of(), null);
			ensureRoom(roomRepository, "203", 2, 15000, 1, RoomStatus.RESERVED, standard, java.util.List.of("Sea View"), "Sarah W.");
			ensureRoom(roomRepository, "301", 3, 150000, 4, RoomStatus.MAINTENANCE, penthouse, java.util.List.of("Sea View", "Balcony", "Jacuzzi"), null);

			if (roomRepository.count() == 0) {
				// legacy guard for very-first-run environments
				Room room101 = new Room();
				room101.setRoomNumber("101");
				room101.setFloor(1);
				room101.setPrice(35000);
				room101.setBeds(2);
				room101.setStatus(RoomStatus.AVAILABLE);
				room101.setRoomType(deluxe);
				room101.setAmenities(new LinkedHashSet<>(java.util.List.of("Sea View", "Balcony")));
				roomRepository.save(room101);

				Room room102 = new Room();
				room102.setRoomNumber("102");
				room102.setFloor(1);
				room102.setPrice(35000);
				room102.setBeds(2);
				room102.setStatus(RoomStatus.OCCUPIED);
				room102.setGuestName("Alex M.");
				room102.setRoomType(deluxe);
				room102.setAmenities(new LinkedHashSet<>(java.util.List.of("Balcony")));
				roomRepository.save(room102);

				Room room202 = new Room();
				room202.setRoomNumber("202");
				room202.setFloor(2);
				room202.setPrice(15000);
				room202.setBeds(1);
				room202.setStatus(RoomStatus.AVAILABLE);
				room202.setRoomType(standard);
				roomRepository.save(room202);

				Room room203 = new Room();
				room203.setRoomNumber("203");
				room203.setFloor(2);
				room203.setPrice(15000);
				room203.setBeds(1);
				room203.setStatus(RoomStatus.RESERVED);
				room203.setGuestName("Sarah W.");
				room203.setRoomType(standard);
				room203.setAmenities(new LinkedHashSet<>(java.util.List.of("Sea View")));
				roomRepository.save(room203);
			}

			if (reservationRepository.count() == 0) {
				Room room102 = roomRepository.findAll().stream().filter(room -> "102".equals(room.getRoomNumber())).findFirst().orElse(null);
				Room room203 = roomRepository.findAll().stream().filter(room -> "203".equals(room.getRoomNumber())).findFirst().orElse(null);

				Reservation reservation1 = new Reservation();
				reservation1.setBookingCode("RES-001");
				reservation1.setGuestName("Alex Morgan");
				reservation1.setGuestEmail("alex.morgan@example.com");
				reservation1.setGuestPhone("+971 55 987 6543");
				reservation1.setCheckInDate(LocalDate.now().minusDays(1));
				reservation1.setCheckOutDate(LocalDate.now().plusDays(1));
				reservation1.setCheckInTime(LocalTime.of(14, 0));
				reservation1.setCheckOutTime(LocalTime.of(12, 0));
				reservation1.setStatus(ReservationStatus.IN_HOUSE);
				reservation1.setBookingType(BookingType.STANDARD);
				reservation1.setAmount(new BigDecimal("105000"));
				reservation1.setRoom(room102);
				reservationRepository.save(reservation1);

				Reservation reservation2 = new Reservation();
				reservation2.setBookingCode("RES-002");
				reservation2.setGuestName("Sarah Williams");
				reservation2.setGuestEmail("sarah.w@example.com");
				reservation2.setGuestPhone("+971 50 123 4567");
				reservation2.setCheckInDate(LocalDate.now().plusDays(1));
				reservation2.setCheckOutDate(LocalDate.now().plusDays(3));
				reservation2.setCheckInTime(LocalTime.of(15, 0));
				reservation2.setCheckOutTime(LocalTime.of(11, 0));
				reservation2.setStatus(ReservationStatus.CONFIRMED);
				reservation2.setBookingType(BookingType.COUPLE);
				reservation2.setAmount(new BigDecimal("45000"));
				reservation2.setRoom(room203);
				reservationRepository.save(reservation2);
			} else {
				// Backfill times on existing seeded reservations and ensure room statuses are consistent
				reservationRepository.findAll().forEach(r -> {
					boolean dirty = false;
					if (r.getCheckInTime() == null && "RES-001".equals(r.getBookingCode())) {
						r.setCheckInTime(LocalTime.of(14, 0));
						r.setCheckOutTime(LocalTime.of(12, 0));
						dirty = true;
					}
					if (r.getCheckInTime() == null && "RES-002".equals(r.getBookingCode())) {
						r.setCheckInTime(LocalTime.of(15, 0));
						r.setCheckOutTime(LocalTime.of(11, 0));
						dirty = true;
					}
					if (dirty) {
						reservationRepository.save(r);
					}
				});
				reservationRepository.findAll().forEach(r -> {
					if (r.getRoom() == null) {
						return;
					}
					Room room = r.getRoom();
					switch (r.getStatus()) {
						case CONFIRMED, IN_HOUSE -> {
							if (room.getStatus() == RoomStatus.RESERVED) {
								room.setStatus(RoomStatus.OCCUPIED);
								roomRepository.save(room);
							}
						}
						case PENDING -> {
							if (room.getStatus() == RoomStatus.AVAILABLE) {
								room.setStatus(RoomStatus.RESERVED);
								room.setGuestName(r.getGuestName());
								roomRepository.save(room);
							}
						}
						default -> { }
					}
				});
			}

			if (branchRepository.count() == 0) {
				Branch dubaiMarina = new Branch();
				dubaiMarina.setName("Dubai Marina");
				dubaiMarina.setCode("DXB-MRN");
				dubaiMarina.setAddress("Dubai Marina Walk, Jumeirah Beach Residence");
				dubaiMarina.setCity("Dubai");
				dubaiMarina.setCountry("UAE");
				dubaiMarina.setPhone("+971 4 123 4567");
				dubaiMarina.setEmail("marina@wotege.ae");
				dubaiMarina.setActive(true);
				dubaiMarina.setStatus(BranchStatus.ACTIVE);
				branchRepository.save(dubaiMarina);

				Branch palmJumeirah = new Branch();
				palmJumeirah.setName("Palm Jumeirah");
				palmJumeirah.setCode("DXB-PLM");
				palmJumeirah.setAddress("Crescent Road, Palm Jumeirah");
				palmJumeirah.setCity("Dubai");
				palmJumeirah.setCountry("UAE");
				palmJumeirah.setPhone("+971 4 234 5678");
				palmJumeirah.setEmail("palm@wotege.ae");
				palmJumeirah.setActive(true);
				palmJumeirah.setStatus(BranchStatus.ACTIVE);
				branchRepository.save(palmJumeirah);

				Branch abuDhabi = new Branch();
				abuDhabi.setName("Abu Dhabi Corniche");
				abuDhabi.setCode("AUH-CRN");
				abuDhabi.setAddress("Corniche Road, Al Zahiyah");
				abuDhabi.setCity("Abu Dhabi");
				abuDhabi.setCountry("UAE");
				abuDhabi.setPhone("+971 2 345 6789");
				abuDhabi.setEmail("corniche@wotege.ae");
				abuDhabi.setActive(true);
				abuDhabi.setStatus(BranchStatus.ACTIVE);
				branchRepository.save(abuDhabi);

				Branch sharjah = new Branch();
				sharjah.setName("Sharjah Al Majaz");
				sharjah.setCode("SHJ-MJZ");
				sharjah.setAddress("Al Majaz Waterfront");
				sharjah.setCity("Sharjah");
				sharjah.setCountry("UAE");
				sharjah.setPhone("+971 6 456 7890");
				sharjah.setEmail("majaz@wotege.ae");
				sharjah.setActive(true);
				sharjah.setStatus(BranchStatus.ACTIVE);
				branchRepository.save(sharjah);

				Branch rasAlKhaimah = new Branch();
				rasAlKhaimah.setName("RAK Al Hamra");
				rasAlKhaimah.setCode("RAK-HMR");
				rasAlKhaimah.setAddress("Al Hamra Village");
				rasAlKhaimah.setCity("Ras Al Khaimah");
				rasAlKhaimah.setCountry("UAE");
				rasAlKhaimah.setPhone("+971 7 567 8901");
				rasAlKhaimah.setEmail("alhamra@wotege.ae");
				rasAlKhaimah.setActive(false);
				rasAlKhaimah.setStatus(BranchStatus.INACTIVE);
				branchRepository.save(rasAlKhaimah);
			}

			Branch branch = branchRepository.findByActiveTrue().stream().findFirst().orElse(null);

			if (menuCategoryRepository.count() == 0) {
				MenuCategory starters = createCategory("STA", "Starters", "Appetizers and small plates", 1);
				MenuCategory mains = createCategory("MNS", "Mains", "Main course dishes", 2);
				MenuCategory desserts = createCategory("DST", "Desserts", "Sweet treats and desserts", 3);
				MenuCategory beverages = createCategory("BVG", "Beverages", "Non-alcoholic drinks", 4);
				MenuCategory alcohol = createCategory("ALC", "Alcohol", "Wines, spirits, and cocktails", 5);

				if (menuItemRepository.count() == 0) {
					// Starters
					saveMenuItem("STA-001", "Lobster Bisque", "Creamy lobster soup with cognac",
						"https://images.unsplash.com/photo-1548943487-a2e4143fa723?q=80&w=300&auto=format&fit=crop",
						2800, 1100, starters, true, 12);
					saveMenuItem("STA-002", "Tuna Tartare", "Fresh yellowfin tuna with avocado",
						"https://images.unsplash.com/photo-1559847844-5315695dadae?q=80&w=300&auto=format&fit=crop",
						3500, 1400, starters, true, 10);
					saveMenuItem("STA-003", "Burrata & Heirloom Tomato", "Creamy burrata with basil oil",
						"https://images.unsplash.com/photo-1608897013039-887f21d8c804?q=80&w=300&auto=format&fit=crop",
						3200, 1200, starters, true, 8);

					// Mains
					saveMenuItem("MNS-001", "Wagyu Beef Steak", "A5 Japanese wagyu with truffle jus",
						"https://images.unsplash.com/photo-1546241072-48010ad168d5?q=80&w=300&auto=format&fit=crop",
						8500, 3500, mains, true, 25);
					saveMenuItem("MNS-002", "Truffle Risotto", "Arborio rice with black truffle",
						"https://images.unsplash.com/photo-1626379616459-b2ce1d9decbc?q=80&w=300&auto=format&fit=crop",
						4200, 1600, mains, true, 20);
					saveMenuItem("MNS-003", "Gold-leaf Sushi Roll", "Premium sushi with edible gold leaf",
						"https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=300&auto=format&fit=crop",
						5500, 2200, mains, true, 15);
					saveMenuItem("MNS-004", "Pan-Seared Sea Bass", "Mediterranean sea bass with lemon beurre blanc",
						"https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?q=80&w=300&auto=format&fit=crop",
						4800, 1800, mains, true, 18);
					saveMenuItem("MNS-005", "Herb-Crusted Lamb Rack", "New Zealand lamb with rosemary and mint",
						"https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=300&auto=format&fit=crop",
						6200, 2400, mains, true, 25);

					// Desserts
					saveMenuItem("DST-001", "Matcha Tiramisu", "Japanese twist on classic tiramisu",
						"https://images.unsplash.com/photo-1571115177098-24ec42ed204d?q=80&w=300&auto=format&fit=crop",
						1800, 600, desserts, true, 5);
					saveMenuItem("DST-002", "Molten Chocolate Lava", "Dark chocolate fondant with vanilla ice cream",
						"https://images.unsplash.com/photo-1606313564200-e75d5e30476c?q=80&w=300&auto=format&fit=crop",
						2200, 750, desserts, true, 12);
					saveMenuItem("DST-003", "Crème Brûlée", "Classic vanilla bean custard",
						"https://images.unsplash.com/photo-1470124182917-cc6e71b22ecc?q=80&w=300&auto=format&fit=crop",
						1500, 500, desserts, true, 5);

					// Beverages
					saveMenuItem("BVG-001", "Signature Iced Tea", "House-brewed with peach and mint",
						"https://images.unsplash.com/photo-1556679343-c7306c1976bc?q=80&w=300&auto=format&fit=crop",
						950, 250, beverages, true, 3);
					saveMenuItem("BVG-002", "Fresh Coconut Water", "Young Thai coconut",
						"https://images.unsplash.com/photo-1525385133512-2f3bdd039054?q=80&w=300&auto=format&fit=crop",
						1100, 350, beverages, true, 2);
					saveMenuItem("BVG-003", "Espresso Martini", "Freshly brewed espresso shaken with vodka",
						"https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?q=80&w=300&auto=format&fit=crop",
						1800, 600, beverages, true, 5);

					// Alcohol
					saveMenuItem("ALC-001", "Dom Perignon 2012", "Prestige cuvée champagne",
						"https://images.unsplash.com/photo-1590740924976-5a415ffcc934?q=80&w=300&auto=format&fit=crop",
						29000, 12000, alcohol, true, 2);
					saveMenuItem("ALC-002", "Château Margaux 2015", "First growth Bordeaux",
						"https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?q=80&w=300&auto=format&fit=crop",
						32000, 14000, alcohol, true, 2);
					saveMenuItem("ALC-003", "Single Malt Whisky", "18 year aged Scottish single malt",
						"https://images.unsplash.com/photo-1527281400683-1aae777175f8?q=80&w=300&auto=format&fit=crop",
						4500, 1800, alcohol, true, 2);
				}
			}

			if (customerRepository.count() == 0) {
				Customer john = new Customer();
				john.setCode("CUST-001");
				john.setName("John Smith");
				john.setEmail("john.smith@email.com");
				john.setPhone("+971 55 123 4567");
				john.setIdCardNumber("ID-784-2024");
				john.setAddress("Villa 12, Palm Jumeirah, Dubai");
				john.setNationality("British");
				john.setDateOfBirth(LocalDate.of(1985, 6, 15));
				john.setMemberSince(LocalDate.now().minusMonths(6));
				john.setLoyaltyTier(LoyaltyTier.GOLD);
				john.setTotalVisits(8);
				john.setTotalSpent(new BigDecimal("345000"));
				john.setCreatedAt(LocalDateTime.now().minusMonths(6));
				john.setUpdatedAt(LocalDateTime.now());
				customerRepository.save(john);

				Customer emma = new Customer();
				emma.setCode("CUST-002");
				emma.setName("Emma Watson");
				emma.setEmail("emma.w@example.com");
				emma.setPhone("+971 50 987 6543");
				emma.setIdCardNumber("ID-891-2024");
				emma.setAddress("Apartment 304, Marina Towers, Dubai");
				emma.setNationality("French");
				emma.setDateOfBirth(LocalDate.of(1992, 11, 3));
				emma.setMemberSince(LocalDate.now().minusMonths(2));
				emma.setLoyaltyTier(LoyaltyTier.SILVER);
				emma.setTotalVisits(3);
				emma.setTotalSpent(new BigDecimal("87500"));
				emma.setCreatedAt(LocalDateTime.now().minusMonths(2));
				emma.setUpdatedAt(LocalDateTime.now());
				customerRepository.save(emma);
			}

			Customer john = customerRepository.findAll().stream().filter(c -> "CUST-001".equals(c.getCode())).findFirst().orElse(null);
			Customer emma = customerRepository.findAll().stream().filter(c -> "CUST-002".equals(c.getCode())).findFirst().orElse(null);

			MenuItem grill = menuItemRepository.findAll().stream().filter(m -> "SIG-001".equals(m.getCode())).findFirst().orElse(null);
			MenuItem lobster = menuItemRepository.findAll().stream().filter(m -> "SIG-002".equals(m.getCode())).findFirst().orElse(null);
			MenuItem lamb = menuItemRepository.findAll().stream().filter(m -> "SIG-003".equals(m.getCode())).findFirst().orElse(null);
			MenuItem seaBass = menuItemRepository.findAll().stream().filter(m -> "SIG-004".equals(m.getCode())).findFirst().orElse(null);

			if (orderRepository.count() == 0) {
				Order order1 = new Order();
				order1.setOrderCode("ORD-001");
				order1.setCustomer(john);
				order1.setCashierName("Ahmed");
				order1.setTableNumber("T7");
				order1.setType(OrderType.DINE_IN);
				order1.setStatus(OrderStatus.COMPLETED);
				order1.setSubtotal(new BigDecimal("47000"));
				order1.setTax(new BigDecimal("4700"));
				order1.setDiscount(new BigDecimal("0"));
				order1.setTotalAmount(new BigDecimal("51700"));
				order1.setOrderedAt(LocalDateTime.now().minusHours(3));
				order1.setCompletedAt(LocalDateTime.now().minusHours(2));
				orderRepository.save(order1);

				OrderItem oi1 = new OrderItem();
				oi1.setOrder(order1);
				oi1.setMenuItem(grill);
				oi1.setQuantity(1);
				oi1.setUnitPrice(new BigDecimal("18500"));
				oi1.setSubtotal(new BigDecimal("18500"));
				orderItemRepository.save(oi1);

				OrderItem oi2 = new OrderItem();
				oi2.setOrder(order1);
				oi2.setMenuItem(lobster);
				oi2.setQuantity(1);
				oi2.setUnitPrice(new BigDecimal("28500"));
				oi2.setSubtotal(new BigDecimal("28500"));
				orderItemRepository.save(oi2);

				Order order2 = new Order();
				order2.setOrderCode("ORD-002");
				order2.setCustomer(emma);
				order2.setCashierName("Fatima");
				order2.setTableNumber("T3");
				order2.setType(OrderType.DINE_IN);
				order2.setStatus(OrderStatus.PREPARING);
				order2.setSubtotal(new BigDecimal("22500"));
				order2.setTax(new BigDecimal("2250"));
				order2.setDiscount(new BigDecimal("1125"));
				order2.setTotalAmount(new BigDecimal("23625"));
				order2.setOrderedAt(LocalDateTime.now().minusMinutes(30));
				orderRepository.save(order2);

				OrderItem oi3 = new OrderItem();
				oi3.setOrder(order2);
				oi3.setMenuItem(lamb);
				oi3.setQuantity(1);
				oi3.setUnitPrice(new BigDecimal("22500"));
				oi3.setSubtotal(new BigDecimal("22500"));
				orderItemRepository.save(oi3);

				Order order3 = new Order();
				order3.setOrderCode("ORD-003");
				order3.setCustomer(null);
				order3.setCashierName("Ahmed");
				order3.setTableNumber("T12");
				order3.setType(OrderType.TAKEAWAY);
				order3.setStatus(OrderStatus.PENDING);
				order3.setSubtotal(new BigDecimal("19500"));
				order3.setTax(new BigDecimal("1950"));
				order3.setDiscount(new BigDecimal("0"));
				order3.setTotalAmount(new BigDecimal("21450"));
				order3.setOrderedAt(LocalDateTime.now().minusMinutes(10));
				orderRepository.save(order3);

				OrderItem oi4 = new OrderItem();
				oi4.setOrder(order3);
				oi4.setMenuItem(seaBass);
				oi4.setQuantity(1);
				oi4.setUnitPrice(new BigDecimal("19500"));
				oi4.setSubtotal(new BigDecimal("19500"));
				orderItemRepository.save(oi4);
			}

			if (paymentRepository.count() == 0) {
				Order ord1 = orderRepository.findAll().stream().filter(o -> "ORD-001".equals(o.getOrderCode())).findFirst().orElse(null);

				Payment payment1 = new Payment();
				payment1.setPaymentCode("PAY-001");
				payment1.setOrder(ord1);
				payment1.setAmount(new BigDecimal("51700"));
				payment1.setMethod(PaymentMethod.CARD);
				payment1.setStatus(PaymentStatus.COMPLETED);
				payment1.setReferenceNumber("TXN-REF-78901");
				payment1.setNotes("Main payment for table T7");
				payment1.setPaidAt(LocalDateTime.now().minusHours(2));
				paymentRepository.save(payment1);
			}

			if (auditLogRepository.count() == 0) {
				AuditLog log1 = new AuditLog();
				log1.setAction("New Room Booking");
				log1.setEntityType("Reservation");
				log1.setEntityId(1L);
				log1.setDescription("Suite 402 - 3 Nights");
				log1.setDetails("+LKR 120,000");
				log1.setUsername("Receptionist Sara");
				log1.setTimestamp(LocalDateTime.now().minusMinutes(1));
				log1.setBranchCode("DXB-MRN");
				auditLogRepository.save(log1);

				AuditLog log2 = new AuditLog();
				log2.setAction("Restaurant POS");
				log2.setEntityType("Order");
				log2.setEntityId(1L);
				log2.setDescription("Table 14 settled bill");
				log2.setDetails("+LKR 14,500");
				log2.setUsername("Cashier Ahmed");
				log2.setTimestamp(LocalDateTime.now().minusMinutes(2));
				log2.setBranchCode("DXB-MRN");
				auditLogRepository.save(log2);

				AuditLog log3 = new AuditLog();
				log3.setAction("Room Service");
				log3.setEntityType("Order");
				log3.setEntityId(2L);
				log3.setDescription("Order #482 to Room 205");
				log3.setDetails("+LKR 4,500");
				log3.setUsername("Room Service");
				log3.setTimestamp(LocalDateTime.now().minusMinutes(15));
				log3.setBranchCode("DXB-MRN");
				auditLogRepository.save(log3);

				AuditLog log4 = new AuditLog();
				log4.setAction("Check-out");
				log4.setEntityType("Reservation");
				log4.setEntityId(2L);
				log4.setDescription("Room 304 - Mr. Smith");
				log4.setUsername("Receptionist Sara");
				log4.setTimestamp(LocalDateTime.now().minusHours(1));
				log4.setBranchCode("DXB-MRN");
				auditLogRepository.save(log4);

				AuditLog log5 = new AuditLog();
				log5.setAction("Stock Alert");
				log5.setEntityType("InventoryItem");
				log5.setEntityId(1L);
				log5.setDescription("Premium Champagne low");
				log5.setUsername("Chef Marco");
				log5.setTimestamp(LocalDateTime.now().minusHours(2));
				log5.setBranchCode("DXB-MRN");
				auditLogRepository.save(log5);
			}

			if (inventoryItemRepository.count() == 0) {
				InventoryItem salmon = new InventoryItem();
				salmon.setCode("INV-001");
				salmon.setName("Atlantic Salmon");
				salmon.setDescription("Fresh Norwegian Atlantic Salmon");
				salmon.setCategory("Seafood");
				salmon.setUnit("kg");
				salmon.setQuantity(new BigDecimal("25"));
				salmon.setMinQuantity(new BigDecimal("5"));
				salmon.setUnitCost(new BigDecimal("85"));
				salmon.setStatus(StockStatus.IN_STOCK);
				salmon.setSupplierInfo("Fresh Catch LLC");
				salmon.setUpdatedAt(LocalDateTime.now());
				inventoryItemRepository.save(salmon);

				InventoryItem ribeye = new InventoryItem();
				ribeye.setCode("INV-002");
				ribeye.setName("USDA Ribeye Steak");
				ribeye.setDescription("Prime grade USDA Ribeye");
				ribeye.setCategory("Meat");
				ribeye.setUnit("kg");
				ribeye.setQuantity(new BigDecimal("3"));
				ribeye.setMinQuantity(new BigDecimal("10"));
				ribeye.setUnitCost(new BigDecimal("160"));
				ribeye.setStatus(StockStatus.REORDER);
				ribeye.setSupplierInfo("Premium Meats Co.");
				ribeye.setUpdatedAt(LocalDateTime.now());
				inventoryItemRepository.save(ribeye);

				InventoryItem oliveOil = new InventoryItem();
				oliveOil.setCode("INV-003");
				oliveOil.setName("Extra Virgin Olive Oil");
				oliveOil.setDescription("Italian extra virgin olive oil 5L");
				oliveOil.setCategory("Pantry");
				oliveOil.setUnit("liters");
				oliveOil.setQuantity(new BigDecimal("2"));
				oliveOil.setMinQuantity(new BigDecimal("4"));
				oliveOil.setUnitCost(new BigDecimal("45"));
				oliveOil.setStatus(StockStatus.LOW_STOCK);
				oliveOil.setSupplierInfo("Mediterranean Imports");
				oliveOil.setUpdatedAt(LocalDateTime.now());
				inventoryItemRepository.save(oliveOil);

				InventoryItem wine = new InventoryItem();
				wine.setCode("INV-004");
				wine.setName("Cabernet Sauvignon");
				wine.setDescription("Chilean Cabernet Sauvignon 750ml");
				wine.setCategory("Beverages");
				wine.setUnit("bottles");
				wine.setQuantity(new BigDecimal("48"));
				wine.setMinQuantity(new BigDecimal("12"));
				wine.setUnitCost(new BigDecimal("65"));
				wine.setStatus(StockStatus.IN_STOCK);
				wine.setSupplierInfo("Global Wines Ltd.");
				wine.setUpdatedAt(LocalDateTime.now());
				inventoryItemRepository.save(wine);

				if (inventoryMovementRepository.count() == 0) {
					InventoryMovement m1 = new InventoryMovement();
					m1.setItem(salmon);
					m1.setQuantity(new BigDecimal("15"));
					m1.setType(MovementType.IN);
					m1.setReference("PO-2024-001");
					m1.setNotes("Weekly seafood delivery");
					m1.setCreatedAt(LocalDateTime.now().minusDays(1));
					inventoryMovementRepository.save(m1);

					InventoryMovement m2 = new InventoryMovement();
					m2.setItem(ribeye);
					m2.setQuantity(new BigDecimal("2"));
					m2.setType(MovementType.OUT);
					m2.setReference("ORD-001");
					m2.setNotes("Used for weekend specials");
					m2.setCreatedAt(LocalDateTime.now().minusDays(2));
					inventoryMovementRepository.save(m2);

					InventoryMovement m3 = new InventoryMovement();
					m3.setItem(oliveOil);
					m3.setQuantity(new BigDecimal("3"));
					m3.setType(MovementType.IN);
					m3.setReference("PO-2024-002");
					m3.setNotes("Restocked pantry");
					m3.setCreatedAt(LocalDateTime.now().minusDays(3));
					inventoryMovementRepository.save(m3);
				}
			}

			if (staffRepository.count() == 0) {
				Staff chef = new Staff();
				chef.setStaffCode("STF-001");
				chef.setFirstName("Marco");
				chef.setLastName("Rossi");
				chef.setEmail("marco.rossi@wotege.ae");
				chef.setPhone("+971 54 111 2222");
				chef.setPosition("Executive Chef");
				chef.setDepartment("Kitchen");
				chef.setHireDate(LocalDate.of(2023, 3, 1));
				chef.setSalary(new BigDecimal("25000"));
				chef.setStatus(StaffStatus.ACTIVE);
				chef.setBranch(branch);
				staffRepository.save(chef);

				Staff sara = new Staff();
				sara.setStaffCode("STF-002");
				sara.setFirstName("Sara");
				sara.setLastName("Ahmed");
				sara.setEmail("sara.ahmed@wotege.ae");
				sara.setPhone("+971 55 333 4444");
				sara.setPosition("Senior Receptionist");
				sara.setDepartment("Front Desk");
				sara.setHireDate(LocalDate.of(2024, 1, 15));
				sara.setSalary(new BigDecimal("8500"));
				sara.setStatus(StaffStatus.ACTIVE);
				sara.setBranch(branch);
				staffRepository.save(sara);
			}

			if (userAccountRepository.findByEmail(adminEmail).isEmpty()) {
				String username = adminEmail.contains("@") ? adminEmail.substring(0, adminEmail.indexOf("@")) : "admin";
				UserAccount admin = new UserAccount();
				admin.setUsername(username);
				admin.setPassword(adminPassword);
				admin.setFullName("WOTEGE Administrator");
				admin.setEmail(adminEmail);
				admin.setRole(superAdmin);
				admin.setActive(true);
				userAccountRepository.save(admin);
			}
		};
	}

	private MenuCategory createCategory(String code, String name, String description, int displayOrder) {
		MenuCategory category = new MenuCategory();
		category.setCode(code);
		category.setName(name);
		category.setDescription(description);
		category.setDisplayOrder(displayOrder);
		category.setActive(true);
		return menuCategoryRepository.save(category);
	}

	private void saveMenuItem(String code, String name, String description, String imageUrl,
			Number price, Number costPrice, MenuCategory category, boolean available, int prepTime) {
		MenuItem item = new MenuItem();
		item.setCode(code);
		item.setName(name);
		item.setDescription(description);
		item.setImageUrl(imageUrl);
		item.setPrice(new BigDecimal(price.toString()));
		item.setCostPrice(new BigDecimal(costPrice.toString()));
		item.setCategory(category);
		item.setAvailable(available);
		item.setSignature(false);
		item.setPreparationTime(prepTime);
		item.setCreatedAt(LocalDateTime.now());
		item.setUpdatedAt(LocalDateTime.now());
		menuItemRepository.save(item);
	}

	private void ensureRoom(RoomRepository repo, String roomNumber, int floor, int price, int beds,
			RoomStatus status, RoomType type, java.util.List<String> amenities, String guestName) {
		if (repo.findAll().stream().anyMatch(r -> roomNumber.equals(r.getRoomNumber()))) {
			return;
		}
		Room room = new Room();
		room.setRoomNumber(roomNumber);
		room.setFloor(floor);
		room.setPrice(price);
		room.setBeds(beds);
		room.setStatus(status);
		room.setRoomType(type);
		room.setAmenities(new LinkedHashSet<>(amenities));
		room.setGuestName(guestName);
		repo.save(room);
	}
}
