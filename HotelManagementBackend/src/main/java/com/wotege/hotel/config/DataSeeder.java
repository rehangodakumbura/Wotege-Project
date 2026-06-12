package com.wotege.hotel.config;

import com.wotege.hotel.entity.Room;
import com.wotege.hotel.entity.User;
import com.wotege.hotel.repository.RoomRepository;
import com.wotege.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (roomRepository.count() == 0) {
            seedRooms();
        }
    }

    private void seedUsers() {
        userRepository.save(User.builder()
                .username("admin")
                .email("admin@hotel.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("manager")
                .email("manager@hotel.com")
                .password(passwordEncoder.encode("manager123"))
                .role(User.Role.MANAGER)
                .build());

        userRepository.save(User.builder()
                .username("reception")
                .email("reception@hotel.com")
                .password(passwordEncoder.encode("reception123"))
                .role(User.Role.RECEPTIONIST)
                .build());
    }

    private void seedRooms() {
        List<Room> rooms = Arrays.asList(
                createRoom("101", "Deluxe Suite", 250.0, 2, true, true),
                createRoom("102", "Deluxe Suite", 250.0, 2, true, false),
                createRoom("103", "Deluxe Suite", 250.0, 2, false, true),
                createRoom("104", "Deluxe Suite", 250.0, 2, false, false),
                createRoom("105", "Deluxe Suite", 250.0, 2, true, true),
                createRoom("201", "Presidential", 500.0, 3, true, true),
                createRoom("202", "Presidential", 500.0, 3, true, true),
                createRoom("203", "Standard", 150.0, 1, false, false),
                createRoom("204", "Standard", 150.0, 2, false, false),
                createRoom("205", "Standard", 150.0, 1, false, true),
                createRoom("301", "Penthouse", 800.0, 4, true, true),
                createRoom("302", "Penthouse", 800.0, 4, true, true)
        );
        roomRepository.saveAll(rooms);
    }

    private Room createRoom(String roomNumber, String roomType, double price,
                            int bedCount, boolean seaView, boolean balcony) {
        return Room.builder()
                .roomNumber(roomNumber)
                .roomType(roomType)
                .description(roomType + " - Room " + roomNumber)
                .pricePerNight(price)
                .bedCount(bedCount)
                .hasSeaView(seaView)
                .hasBalcony(balcony)
                .status(Room.RoomStatus.AVAILABLE)
                .build();
    }
}
