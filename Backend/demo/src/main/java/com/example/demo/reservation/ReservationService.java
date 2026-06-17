package com.example.demo.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.config.NotificationService;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.guest.Guest;
import com.example.demo.guest.GuestRepository;
import com.example.demo.room.Room;
import com.example.demo.room.RoomRepository;
import com.example.demo.room.RoomStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final GuestRepository guestRepository;
    private final NotificationService notificationService;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            CustomerRepository customerRepository,
            GuestRepository guestRepository,
            NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.guestRepository = guestRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByFilters(ReservationStatus status, String search) {
        if (status != null && (search == null || search.isBlank())) {
            return reservationRepository.findByStatus(status);
        }
        if ((status == null) && search != null && !search.isBlank()) {
            return reservationRepository.findBySearch(search);
        }
        if (status != null && search != null && !search.isBlank()) {
            return reservationRepository.findByStatus(status).stream()
                    .filter(r -> r.getGuestName().toLowerCase().contains(search.toLowerCase())
                            || r.getBookingCode().toLowerCase().contains(search.toLowerCase())
                            || (r.getBookingId() != null && r.getBookingId().toLowerCase().contains(search.toLowerCase())))
                    .toList();
        }
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public Reservation create(Reservation payload, Long roomId, Long guestId, Long customerId) {
        Reservation reservation = new Reservation();
        apply(payload, reservation);

        String bookingId = generateBookingId();
        reservation.setBookingId(bookingId);
        reservation.setBookingCode(bookingId);
        reservation.setStatus(ReservationStatus.PENDING);

        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
            checkAvailability(roomId, reservation.getCheckInDate(), reservation.getCheckOutDate());
            reservation.setRoom(room);
        }

        if (guestId != null) {
            Guest guest = guestRepository.findById(guestId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
            reservation.setGuest(guest);
        }

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
            reservation.setCustomer(customer);
        }

        Reservation saved = reservationRepository.save(reservation);

        if (Boolean.TRUE.equals(payload.getSendInvoiceEmail())) {
            sendInvoiceEmail(saved);
        }

        Map<String, Object> notification = new LinkedHashMap<>();
        notification.put("type", "RESERVATION_CREATED");
        notification.put("reservationId", saved.getId());
        notification.put("bookingId", saved.getBookingId());
        notification.put("guestName", saved.getGuestName());
        notificationService.broadcastReservationUpdate(notification);

        return saved;
    }

    public Reservation update(Long id, Reservation payload, Long roomId, Long guestId, Long customerId) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        apply(payload, existing);

        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
            if (!room.getId().equals(existing.getRoom() != null ? existing.getRoom().getId() : null)) {
                checkAvailability(roomId, existing.getCheckInDate(), existing.getCheckOutDate());
            }
            existing.setRoom(room);
        }

        if (guestId != null) {
            Guest guest = guestRepository.findById(guestId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
            existing.setGuest(guest);
        }

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
            existing.setCustomer(customer);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(existing);

        Map<String, Object> notification = new LinkedHashMap<>();
        notification.put("type", "RESERVATION_UPDATED");
        notification.put("reservationId", saved.getId());
        notification.put("bookingId", saved.getBookingId());
        notificationService.broadcastReservationUpdate(notification);

        return saved;
    }

    public Reservation updateStatus(Long id, ReservationStatus status) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        existing.setStatus(status);
        existing.setUpdatedAt(LocalDateTime.now());

        if (existing.getRoom() != null) {
            switch (status) {
                case CONFIRMED, IN_HOUSE -> existing.getRoom().setStatus(RoomStatus.OCCUPIED);
                case COMPLETED -> existing.getRoom().setStatus(RoomStatus.AVAILABLE);
                case CANCELLED -> existing.getRoom().setStatus(RoomStatus.AVAILABLE);
                default -> {}
            }
        }

        Reservation saved = reservationRepository.save(existing);

        Map<String, Object> notification = new LinkedHashMap<>();
        notification.put("type", "RESERVATION_STATUS_CHANGE");
        notification.put("reservationId", saved.getId());
        notification.put("bookingId", saved.getBookingId());
        notification.put("status", status);
        notificationService.broadcastReservationUpdate(notification);

        return saved;
    }

    public void delete(Long id) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (existing.getRoom() != null && existing.getStatus() != ReservationStatus.CANCELLED
                && existing.getStatus() != ReservationStatus.COMPLETED) {
            existing.getRoom().setStatus(RoomStatus.AVAILABLE);
        }

        reservationRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStats() {
        LocalDate today = LocalDate.now();

        long todayCheckIns = reservationRepository.findCheckInsToday(today).stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .count();

        long todayCheckOuts = reservationRepository.findCheckOutsToday(today).stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .count();

        long upcomingEvents = reservationRepository.findUpcomingEvents(today).size();

        long availableRooms = roomRepository.findByStatus(RoomStatus.AVAILABLE).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("todayCheckIns", todayCheckIns);
        stats.put("todayCheckOuts", todayCheckOuts);
        stats.put("upcomingEvents", upcomingEvents);
        stats.put("availableRooms", availableRooms);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCalendar(String view, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate weekStart = date.with(java.time.DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Room> rooms = roomRepository.findAll();

        List<Map<String, Object>> roomEntries = rooms.stream().map(room -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("roomNumber", room.getRoomNumber());
            entry.put("roomType", room.getRoomType() != null ? room.getRoomType().getName() : "Unknown");

            if (room.getStatus() == RoomStatus.MAINTENANCE) {
                entry.put("maintenance", true);
                entry.put("bookings", List.of());
            } else {
                List<Reservation> activeReservations = reservationRepository.findAll()
                        .stream()
                        .filter(r -> room.getId().equals(r.getRoom() != null ? r.getRoom().getId() : null))
                        .filter(r -> r.getCheckOutDate().isAfter(weekStart) && r.getCheckInDate().isBefore(weekEnd.plusDays(1)))
                        .filter(r -> r.getStatus() != ReservationStatus.CANCELLED && r.getStatus() != ReservationStatus.COMPLETED)
                        .collect(Collectors.toList());

                List<Map<String, Object>> bookings = activeReservations.stream().map(r -> {
                    Map<String, Object> booking = new LinkedHashMap<>();
                    booking.put("guestName", r.getGuestName());
                    booking.put("checkIn", r.getCheckInDate().toString());
                    booking.put("checkOut", r.getCheckOutDate().toString());
                    booking.put("bookingId", r.getBookingId());
                    booking.put("status", mapStatusForCalendar(r));
                    return booking;
                }).collect(Collectors.toList());

                entry.put("maintenance", false);
                entry.put("bookings", bookings);
            }

            return entry;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rooms", roomEntries);
        result.put("weekStart", weekStart.toString());
        result.put("weekEnd", weekEnd.toString());
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getEstimate(Long roomId, LocalDate checkIn, LocalDate checkOut, DurationType durationType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days < 1) days = 1;

        int basePrice = room.getPrice() != null ? room.getPrice() : 0;
        BigDecimal total;

        if (durationType == DurationType.PER_HOUR) {
            total = BigDecimal.valueOf(basePrice / 24.0 * days * 24);
        } else {
            total = BigDecimal.valueOf(basePrice).multiply(BigDecimal.valueOf(days));
        }

        Map<String, Object> estimate = new HashMap<>();
        estimate.put("totalEstimate", total);
        estimate.put("currency", "LKR");
        estimate.put("nights", days);
        estimate.put("ratePerNight", basePrice);
        return estimate;
    }

    public Reservation cancel(Long id) {
        return updateStatus(id, ReservationStatus.CANCELLED);
    }

    private void checkAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflicts = reservationRepository.findConflicting(roomId, checkIn, checkOut);
        if (!conflicts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Room is already booked for the selected dates");
        }
    }

    private String generateBookingId() {
        Optional<String> maxId = reservationRepository.findMaxBookingId();
        if (maxId.isPresent()) {
            String current = maxId.get();
            int num = Integer.parseInt(current.substring(4));
            return String.format("RES-%03d", num + 1);
        }
        return "RES-001";
    }

    private String mapStatusForCalendar(Reservation r) {
        return switch (r.getStatus()) {
            case CONFIRMED -> "confirmed";
            case IN_HOUSE -> "in_house";
            case PENDING -> "pending";
            default -> "checking_out";
        };
    }

    private void apply(Reservation payload, Reservation target) {
        target.setGuestName(payload.getGuestName());
        target.setGuestEmail(payload.getGuestEmail());
        target.setGuestPhone(payload.getGuestPhone());
        if (payload.getCheckInDate() != null) target.setCheckInDate(payload.getCheckInDate());
        if (payload.getCheckOutDate() != null) target.setCheckOutDate(payload.getCheckOutDate());
        if (payload.getStatus() != null) target.setStatus(payload.getStatus());
        if (payload.getBookingType() != null) target.setBookingType(payload.getBookingType());
        if (payload.getDurationType() != null) target.setDurationType(payload.getDurationType());
        if (payload.getAmount() != null) target.setAmount(payload.getAmount());
        if (payload.getSpecialRequests() != null) target.setSpecialRequests(payload.getSpecialRequests());
        if (payload.getSendInvoiceEmail() != null) target.setSendInvoiceEmail(payload.getSendInvoiceEmail());
        if (payload.getNotes() != null) target.setNotes(payload.getNotes());
    }

    private void sendInvoiceEmail(Reservation reservation) {
        String guestEmail = reservation.getGuestEmail();
        if (guestEmail == null || guestEmail.isBlank()) return;

        try {
            System.out.println("=== INVOICE EMAIL ===");
            System.out.println("To: " + guestEmail);
            System.out.println("Subject: Invoice for Booking " + reservation.getBookingId());
            System.out.println("Body: Thank you for your reservation. Your booking " +
                    reservation.getBookingId() + " is confirmed.");
            System.out.println("Amount: LKR " + reservation.getAmount());
            System.out.println("=== END ===");
        } catch (Exception e) {
            System.err.println("Failed to send invoice email: " + e.getMessage());
        }
    }
}
