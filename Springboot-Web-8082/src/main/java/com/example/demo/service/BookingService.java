package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BookingDao;
import com.example.demo.model.dto.BookingMeetingRoomDto;
import com.example.demo.model.po.BookingMeetingRoom;
import com.example.demo.model.po.MeetingRoom;

@Service
public class BookingService {
	
	@Autowired
	private BookingDao bookingDao;
	
	public List<MeetingRoom> findAllRooms() {
		return bookingDao.findAllRooms();
	}

	public Optional<MeetingRoom> getRoom(Integer roomId) {
		return bookingDao.getRoom(roomId);
	}

	public Integer addRoom(Integer roomId, String roomName, Integer roomSize) {
		MeetingRoom room = new MeetingRoom(roomId, roomName, roomSize);
		return bookingDao.addRoom(room);
	}

	public Integer addBooking(Integer roomId, Integer userId, String bookingDate) {
		BookingMeetingRoom booking = new BookingMeetingRoom();
		booking.setRoomId(roomId);
		booking.setUserId(userId);
		booking.setBookingDate(bookingDate);
		return bookingDao.addBooking(booking);
	}

	public Integer cancelBooking(Integer bookingId) {
		return bookingDao.cancelBooking(bookingId);
	}

	public List<BookingMeetingRoomDto> findAllBookings() {
		return bookingDao.findAllBookings();
	}

	public Integer updateBookingUserId(Integer bookingId, Integer userId) {
		return bookingDao.updateBookingUserId(bookingId, userId);
	}

}
