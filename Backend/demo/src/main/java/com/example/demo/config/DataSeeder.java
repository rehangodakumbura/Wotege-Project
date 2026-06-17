package com.example.demo.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;

import com.example.demo.auth.UserAccount;
import com.example.demo.auth.UserAccountRepository;
import com.example.demo.guest.Guest;
import com.example.demo.guest.GuestRepository;
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
            GuestRepository guestRepository,
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

            RoomType suite = roomTypeRepository.findByCode("SUITE").orElseGet(() -> {
                RoomType roomType = new RoomType();
                roomType.setCode("SUITE");
                roomType.setName("Suite");
                roomType.setBaseRate(60000);
                roomType.setBeds(2);
                roomType.setCapacity(4);
                roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony", "Jacuzzi")));
                return roomTypeRepository.save(roomType);
            });

            RoomType penthouse = roomTypeRepository.findByCode("PENTHOUSE").orElseGet(() -> {
                RoomType roomType = new RoomType();
                roomType.setCode("PENTHOUSE");
                roomType.setName("Penthouse");
                roomType.setBaseRate(145000);
                roomType.setBeds(3);
                roomType.setCapacity(6);
                roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony", "Jacuzzi", "Butler Service")));
                return roomTypeRepository.save(roomType);
            });

            RoomType presidential = roomTypeRepository.findByCode("PRESIDENTIAL_SUITE").orElseGet(() -> {
                RoomType roomType = new RoomType();
                roomType.setCode("PRESIDENTIAL_SUITE");
                roomType.setName("Presidential Suite");
                roomType.setBaseRate(150000);
                roomType.setBeds(4);
                roomType.setCapacity(8);
                roomType.setAmenities(new LinkedHashSet<>(java.util.List.of("Free Wi-Fi", "AC", "TV", "Mini Bar", "Sea View", "Balcony", "Jacuzzi", "Butler Service", "Private Pool")));
                return roomTypeRepository.save(roomType);
            });

            if (roomRepository.count() <= 5) {
                reservationRepository.deleteAll();
                roomRepository.deleteAll();

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

                Room room204 = new Room();
                room204.setRoomNumber("204");
                room204.setFloor(2);
                room204.setPrice(15000);
                room204.setBeds(1);
                room204.setStatus(RoomStatus.MAINTENANCE);
                room204.setRoomType(standard);
                roomRepository.save(room204);

                Room room224 = new Room();
                room224.setRoomNumber("224");
                room224.setFloor(2);
                room224.setPrice(15000);
                room224.setBeds(1);
                room224.setStatus(RoomStatus.AVAILABLE);
                room224.setRoomType(standard);
                roomRepository.save(room224);

                Room suite602 = new Room();
                suite602.setRoomNumber("602");
                suite602.setFloor(6);
                suite602.setPrice(60000);
                suite602.setBeds(2);
                suite602.setStatus(RoomStatus.OCCUPIED);
                suite602.setGuestName("Sofia Al-Maktoum");
                suite602.setRoomType(suite);
                suite602.setAmenities(new LinkedHashSet<>(java.util.List.of("Sea View", "Balcony", "Jacuzzi")));
                roomRepository.save(suite602);

                Room penthouseRoom = new Room();
                penthouseRoom.setRoomNumber("PH-01");
                penthouseRoom.setFloor(10);
                penthouseRoom.setPrice(145000);
                penthouseRoom.setBeds(3);
                penthouseRoom.setStatus(RoomStatus.RESERVED);
                penthouseRoom.setGuestName("Karin Lagerfeld");
                penthouseRoom.setRoomType(penthouse);
                penthouseRoom.setAmenities(new LinkedHashSet<>(java.util.List.of("Sea View", "Balcony", "Jacuzzi", "Butler Service")));
                roomRepository.save(penthouseRoom);

                Room presRoom = new Room();
                presRoom.setRoomNumber("PH-02");
                presRoom.setFloor(10);
                presRoom.setPrice(150000);
                presRoom.setBeds(4);
                presRoom.setStatus(RoomStatus.OCCUPIED);
                presRoom.setGuestName("Richard Thompson");
                presRoom.setRoomType(presidential);
                presRoom.setAmenities(new LinkedHashSet<>(java.util.List.of("Sea View", "Balcony", "Private Pool", "Butler Service")));
                roomRepository.save(presRoom);
            }

            if (guestRepository.count() == 0) {
                Guest g1 = new Guest();
                g1.setFullName("Richard Thompson");
                g1.setEmail("r.thompson@corp.uk");
                g1.setPhone("+44 7911 123456");
                g1.setNationality("British");
                g1.setGender(Guest.Gender.MALE);
                guestRepository.save(g1);

                Guest g2 = new Guest();
                g2.setFullName("Sofia Al-Maktoum");
                g2.setEmail("sofia.al@example.com");
                g2.setPhone("+971 50 123 4567");
                g2.setNationality("Emirati");
                g2.setGender(Guest.Gender.FEMALE);
                guestRepository.save(g2);

                Guest g3 = new Guest();
                g3.setFullName("Karin Lagerfeld");
                g3.setEmail("karin.l@fashion.de");
                g3.setPhone("+49 170 987654");
                g3.setNationality("German");
                g3.setGender(Guest.Gender.FEMALE);
                guestRepository.save(g3);

                Guest g4 = new Guest();
                g4.setFullName("Michael Chen");
                g4.setEmail("m.chen@tech.sg");
                g4.setPhone("+65 9123 4567");
                g4.setNationality("Singaporean");
                g4.setGender(Guest.Gender.MALE);
                guestRepository.save(g4);
            }

            if (reservationRepository.count() < 4) {
                Room room102 = roomRepository.findAll().stream().filter(r -> "102".equals(r.getRoomNumber())).findFirst().orElse(null);
                Room room203 = roomRepository.findAll().stream().filter(r -> "203".equals(r.getRoomNumber())).findFirst().orElse(null);
                Room suite602 = roomRepository.findAll().stream().filter(r -> "602".equals(r.getRoomNumber())).findFirst().orElse(null);
                Room penthouseRoom = roomRepository.findAll().stream().filter(r -> "PH-01".equals(r.getRoomNumber())).findFirst().orElse(null);
                Room presRoom = roomRepository.findAll().stream().filter(r -> "PH-02".equals(r.getRoomNumber())).findFirst().orElse(null);
                Room room224 = roomRepository.findAll().stream().filter(r -> "224".equals(r.getRoomNumber())).findFirst().orElse(null);

                Reservation res1 = new Reservation();
                res1.setBookingId("RES-001");
                res1.setBookingCode("RES-001");
                res1.setGuestName("Richard Thompson");
                res1.setGuestEmail("r.thompson@corp.uk");
                res1.setGuestPhone("+44 7911 123456");
                res1.setCheckInDate(LocalDate.of(2026, 10, 15));
                res1.setCheckOutDate(LocalDate.of(2026, 10, 18));
                res1.setStatus(ReservationStatus.CONFIRMED);
                res1.setBookingType(BookingType.STANDARD);
                res1.setAmount(new BigDecimal("450000"));
                res1.setRoom(presRoom);
                res1.setNotes("VIP guest - presidential suite");
                reservationRepository.save(res1);

                Reservation res2 = new Reservation();
                res2.setBookingId("RES-002");
                res2.setBookingCode("RES-002");
                res2.setGuestName("Sofia Al-Maktoum");
                res2.setGuestEmail("sofia.al@example.com");
                res2.setGuestPhone("+971 50 123 4567");
                res2.setCheckInDate(LocalDate.of(2026, 10, 14));
                res2.setCheckOutDate(LocalDate.of(2026, 10, 16));
                res2.setStatus(ReservationStatus.IN_HOUSE);
                res2.setBookingType(BookingType.COUPLE);
                res2.setAmount(new BigDecimal("120000"));
                res2.setRoom(suite602);
                reservationRepository.save(res2);

                Reservation res3 = new Reservation();
                res3.setBookingId("RES-003");
                res3.setBookingCode("RES-003");
                res3.setGuestName("Karin Lagerfeld");
                res3.setGuestEmail("karin.l@fashion.de");
                res3.setGuestPhone("+49 170 987654");
                res3.setCheckInDate(LocalDate.of(2026, 10, 18));
                res3.setCheckOutDate(LocalDate.of(2026, 10, 22));
                res3.setStatus(ReservationStatus.PENDING);
                res3.setBookingType(BookingType.STANDARD);
                res3.setAmount(new BigDecimal("580000"));
                res3.setRoom(penthouseRoom);
                reservationRepository.save(res3);

                Reservation res4 = new Reservation();
                res4.setBookingId("RES-004");
                res4.setBookingCode("RES-004");
                res4.setGuestName("Michael Chen");
                res4.setGuestEmail("m.chen@tech.sg");
                res4.setGuestPhone("+65 9123 4567");
                res4.setCheckInDate(LocalDate.of(2026, 10, 12));
                res4.setCheckOutDate(LocalDate.of(2026, 10, 14));
                res4.setStatus(ReservationStatus.COMPLETED);
                res4.setBookingType(BookingType.STANDARD);
                res4.setAmount(new BigDecimal("30000"));
                res4.setRoom(room224);
                reservationRepository.save(res4);
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
