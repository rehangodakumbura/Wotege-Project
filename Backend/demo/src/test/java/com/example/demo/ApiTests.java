package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.example.demo.auth.LoginRequest;
import com.example.demo.auth.LoginResponse;
import com.example.demo.auth.UserAccount;
import com.example.demo.reservation.Reservation;
import com.example.demo.reservation.ReservationStatus;
import com.example.demo.role.Role;
import com.example.demo.room.Room;
import com.example.demo.room.RoomStatus;
import com.example.demo.room.RoomType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class ApiTests {

	@Autowired
	private TestRestTemplate rest;

	private String token;

	@BeforeEach
	void login() {
		LoginRequest request = new LoginRequest("test@example.com", "testpass");
		ResponseEntity<LoginResponse> response = rest.postForEntity(
			"/api/auth/login", request, LoginResponse.class);
		if (response.getBody() != null) {
			token = response.getBody().token();
		}
	}

	private HttpHeaders authHeaders() {
		HttpHeaders headers = new HttpHeaders();
		if (token != null) {
			headers.setBearerAuth(token);
		}
		return headers;
	}

	private <T> RequestEntity<T> authenticated(HttpMethod method, String path, T body) {
		return new RequestEntity<>(body, authHeaders(), method, URI.create(path));
	}

	@Test
	void testAuthLogin() {
		LoginRequest request = new LoginRequest("test@example.com", "testpass");
		ResponseEntity<LoginResponse> response = rest.postForEntity(
			"/api/auth/login", request, LoginResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().token()).isNotBlank();
		assertThat(response.getBody().username()).isEqualTo("test");
	}

	@Test
	void testAuthLoginInvalid() {
		LoginRequest request = new LoginRequest("test@example.com", "wrong");
		ResponseEntity<String> response = rest.postForEntity(
			"/api/auth/login", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void testAuthMe() {
		ResponseEntity<UserAccount> response = rest.exchange(
			authenticated(HttpMethod.GET, "/api/auth/me", null), UserAccount.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
	}

	@Test
	void testAuthMeWithoutTokenReturns401() {
		ResponseEntity<String> response = rest.getForEntity("/api/auth/me", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void testRoomTypesList() {
		ResponseEntity<List<RoomType>> response = rest.exchange(
			authenticated(HttpMethod.GET, "/api/room-types", null),
			new ParameterizedTypeReference<List<RoomType>>() {});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotEmpty();
	}

	@Test
	void testRoomTypesCreate() {
		RoomType rt = new RoomType();
		rt.setCode("TEST_TYPE");
		rt.setName("Test Room Type");
		rt.setBaseRate(10000);
		rt.setBeds(1);
		rt.setCapacity(2);
		rt.setAmenities(Set.of("Wi-Fi"));

		ResponseEntity<RoomType> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/room-types", rt), RoomType.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();
		assertThat(response.getBody().getCode()).isEqualTo("TEST_TYPE");
	}

	@Test
	void testRoomTypesUpdate() {
		List<RoomType> all = rest.exchange(authenticated(HttpMethod.GET, "/api/room-types", null),
			new ParameterizedTypeReference<List<RoomType>>() {}).getBody();
		RoomType existing = all.get(0);
		Long id = existing.getId();
		existing.setName("Updated Name");

		rest.exchange(authenticated(HttpMethod.PUT, "/api/room-types/" + id, existing), Void.class);
		List<RoomType> updatedList = rest.exchange(authenticated(HttpMethod.GET, "/api/room-types", null),
			new ParameterizedTypeReference<List<RoomType>>() {}).getBody();
		RoomType updated = updatedList.stream().filter(r -> r.getId().equals(id)).findFirst().orElseThrow();
		assertThat(updated.getName()).isEqualTo("Updated Name");
	}

	@Test
	void testRoomTypesDelete() {
		RoomType rt = new RoomType();
		rt.setCode("DELETE_ME");
		rt.setName("Delete Me");
		rt.setBaseRate(5000);
		rt.setBeds(1);
		rt.setCapacity(1);

		ResponseEntity<RoomType> created = rest.exchange(
			authenticated(HttpMethod.POST, "/api/room-types", rt), RoomType.class);

		ResponseEntity<Void> deleted = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/room-types/" + created.getBody().getId(), null), Void.class);
		assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void testRoomsList() {
		ResponseEntity<List<Room>> response = rest.exchange(
			authenticated(HttpMethod.GET, "/api/rooms", null),
			new ParameterizedTypeReference<List<Room>>() {});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotEmpty();
	}

	@Test
	void testRoomsCreate() {
		Room room = new Room();
		room.setRoomNumber("TEST-99");
		room.setFloor(3);
		room.setPrice(20000);
		room.setBeds(2);
		room.setStatus(RoomStatus.AVAILABLE);
		room.setAmenities(Set.of("TV", "AC"));

		ResponseEntity<Room> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/rooms", room), Room.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();
		assertThat(response.getBody().getRoomNumber()).isEqualTo("TEST-99");
	}

	@Test
	void testRoomsCreateWithRoomType() {
		List<RoomType> types = rest.exchange(authenticated(HttpMethod.GET, "/api/room-types", null),
			new ParameterizedTypeReference<List<RoomType>>() {}).getBody();
		Long typeId = types.get(0).getId();

		Room room = new Room();
		room.setRoomNumber("TEST-98");
		room.setFloor(3);
		room.setPrice(25000);
		room.setBeds(2);
		room.setStatus(RoomStatus.AVAILABLE);

		ResponseEntity<Room> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/rooms?roomTypeId=" + typeId, room), Room.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getRoomType()).isNotNull();
		assertThat(response.getBody().getRoomType().getId()).isEqualTo(typeId);
	}

	@Test
	void testRoomsUpdateStatus() {
		List<Room> all = rest.exchange(authenticated(HttpMethod.GET, "/api/rooms", null),
			new ParameterizedTypeReference<List<Room>>() {}).getBody();
		Room room = all.stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).findFirst().orElse(all.get(0));

		ResponseEntity<Room> updated = rest.exchange(
			authenticated(HttpMethod.PATCH, "/api/rooms/" + room.getId() + "/status?status=MAINTENANCE", null), Room.class);
		assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updated.getBody().getStatus()).isEqualTo(RoomStatus.MAINTENANCE);
	}

	@Test
	void testRoomsDelete() {
		Room room = new Room();
		room.setRoomNumber("DELETE-RM");
		room.setFloor(1);
		room.setPrice(10000);
		room.setBeds(1);
		ResponseEntity<Room> created = rest.exchange(
			authenticated(HttpMethod.POST, "/api/rooms", room), Room.class);

		ResponseEntity<Void> deleted = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/rooms/" + created.getBody().getId(), null), Void.class);
		assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void testReservationsList() {
		ResponseEntity<List<Reservation>> response = rest.exchange(
			authenticated(HttpMethod.GET, "/api/reservations", null),
			new ParameterizedTypeReference<List<Reservation>>() {});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotEmpty();
	}

	@Test
	void testReservationsCreate() {
		Reservation res = new Reservation();
		res.setBookingCode("TEST-RES-001");
		res.setGuestName("John Doe");
		res.setGuestEmail("john@example.com");
		res.setCheckInDate(LocalDate.now().plusDays(10));
		res.setCheckOutDate(LocalDate.now().plusDays(12));
		res.setAmount(new BigDecimal("50000"));

		ResponseEntity<Reservation> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/reservations", res), Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();
		assertThat(response.getBody().getBookingCode()).isEqualTo("TEST-RES-001");
	}

	@Test
	void testReservationsGenerateBookingCode() {
		Reservation res = new Reservation();
		res.setGuestName("Jane Doe");
		res.setCheckInDate(LocalDate.now().plusDays(20));
		res.setCheckOutDate(LocalDate.now().plusDays(22));
		res.setAmount(new BigDecimal("75000"));

		ResponseEntity<Reservation> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/reservations", res), Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getBookingCode()).startsWith("RES-");
	}

	@Test
	void testReservationsUpdateStatus() {
		List<Reservation> all = rest.exchange(authenticated(HttpMethod.GET, "/api/reservations", null),
			new ParameterizedTypeReference<List<Reservation>>() {}).getBody();
		Reservation res = all.get(0);

		ResponseEntity<Reservation> updated = rest.exchange(
			authenticated(HttpMethod.PATCH, "/api/reservations/" + res.getId() + "/status?status=CONFIRMED", null),
			Reservation.class);
		assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updated.getBody().getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
	}

	@Test
	void testReservationsDelete() {
		Reservation res = new Reservation();
		res.setBookingCode("DELETE-RES");
		res.setGuestName("Delete Me");
		res.setCheckInDate(LocalDate.now().plusDays(30));
		res.setCheckOutDate(LocalDate.now().plusDays(32));
		res.setAmount(new BigDecimal("30000"));
		ResponseEntity<Reservation> created = rest.exchange(
			authenticated(HttpMethod.POST, "/api/reservations", res), Reservation.class);

		ResponseEntity<Void> deleted = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/reservations/" + created.getBody().getId(), null), Void.class);
		assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void testRolesList() {
		ResponseEntity<List<Role>> response = rest.exchange(
			authenticated(HttpMethod.GET, "/api/roles", null),
			new ParameterizedTypeReference<List<Role>>() {});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotEmpty();
	}

	@Test
	void testRolesCreate() {
		Role role = new Role();
		role.setCode("TEST_ROLE");
		role.setName("Test Role");
		role.setDescription("A test role");
		role.setSystemRole(false);

		ResponseEntity<Role> response = rest.exchange(
			authenticated(HttpMethod.POST, "/api/roles", role), Role.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();
		assertThat(response.getBody().getCode()).isEqualTo("TEST_ROLE");
	}

	@Test
	void testRolesUpdate() {
		List<Role> all = rest.exchange(authenticated(HttpMethod.GET, "/api/roles", null),
			new ParameterizedTypeReference<List<Role>>() {}).getBody();
		Role role = all.stream().filter(r -> !r.isSystemRole()).findFirst().orElse(all.get(0));
		Long id = role.getId();
		role.setName("Updated Role Name");

		rest.exchange(authenticated(HttpMethod.PUT, "/api/roles/" + id, role), Void.class);
		List<Role> updatedList = rest.exchange(authenticated(HttpMethod.GET, "/api/roles", null),
			new ParameterizedTypeReference<List<Role>>() {}).getBody();
		Role updated = updatedList.stream().filter(r -> r.getId().equals(id)).findFirst().orElseThrow();
		assertThat(updated.getName()).isEqualTo("Updated Role Name");
	}

	@Test
	void testRolesDeleteNonSystem() {
		Role role = new Role();
		role.setCode("TO_DELETE");
		role.setName("To Delete");
		role.setSystemRole(false);
		ResponseEntity<Role> created = rest.exchange(
			authenticated(HttpMethod.POST, "/api/roles", role), Role.class);

		ResponseEntity<Void> deleted = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/roles/" + created.getBody().getId(), null), Void.class);
		assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void testRolesCannotDeleteSystemRole() {
		List<Role> all = rest.exchange(authenticated(HttpMethod.GET, "/api/roles", null),
			new ParameterizedTypeReference<List<Role>>() {}).getBody();
		Role systemRole = all.stream().filter(Role::isSystemRole).findFirst().orElseThrow();

		ResponseEntity<String> deleted = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/roles/" + systemRole.getId(), null), String.class);
		assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void testDeleteNonExistentReturns404() {
		ResponseEntity<String> response = rest.exchange(
			authenticated(HttpMethod.DELETE, "/api/rooms/99999", null), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
