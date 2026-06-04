package com.example.demo.room;

import com.example.demo.reservation.Reservation;

public class RoomView {

	private Room room;
	private Reservation currentReservation;

	public RoomView() {
	}

	public RoomView(Room room, Reservation currentReservation) {
		this.room = room;
		this.currentReservation = currentReservation;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Reservation getCurrentReservation() {
		return currentReservation;
	}

	public void setCurrentReservation(Reservation currentReservation) {
		this.currentReservation = currentReservation;
	}
}
