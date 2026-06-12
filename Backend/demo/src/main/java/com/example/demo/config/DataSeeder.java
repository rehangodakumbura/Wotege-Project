package com.example.demo.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;

import com.example.demo.auth.UserAccount;
import com.example.demo.auth.UserAccountRepository;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

	@Bean
	CommandLineRunner seedDemoData(
		RoleRepository roleRepository,
		RoomTypeRepository roomTypeRepository,
		RoomRepository roomRepository,
		ReservationRepository reservationRepository,
		UserAccountRepository userAccountRepository,
		PasswordEncoder passwordEncoder
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

			if (roomRepository.count() == 0) {
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
				reservation1.setGuestName("Richard Thompson");
				reservation1.setGuestEmail("r.thompson@corp.uk");
				reservation1.setGuestPhone("+44 7911 123456");
				reservation1.setCheckInDate(LocalDate.now().plusDays(1));
				reservation1.setCheckOutDate(LocalDate.now().plusDays(4));
				reservation1.setStatus(ReservationStatus.CONFIRMED);
				reservation1.setBookingType(BookingType.STANDARD);
				reservation1.setAmount(new BigDecimal("450000"));
				reservation1.setRoom(room102);
				reservationRepository.save(reservation1);

				Reservation reservation2 = new Reservation();
				reservation2.setBookingCode("RES-002");
				reservation2.setGuestName("Sofia Al-Maktoum");
				reservation2.setGuestEmail("sofia.al@example.com");
				reservation2.setGuestPhone("+971 50 123 4567");
				reservation2.setCheckInDate(LocalDate.now().plusDays(2));
				reservation2.setCheckOutDate(LocalDate.now().plusDays(5));
				reservation2.setStatus(ReservationStatus.PENDING);
				reservation2.setBookingType(BookingType.COUPLE);
				reservation2.setAmount(new BigDecimal("120000"));
				reservation2.setRoom(room203);
				reservationRepository.save(reservation2);
			}

		if (userAccountRepository.count() == 0) {
			UserAccount admin = new UserAccount();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setFullName("WOTEGE Administrator");
			admin.setEmail("admin@wotege.com");
			admin.setRole(superAdmin);
			admin.setActive(true);
			userAccountRepository.save(admin);
		}
		};
	}
}
