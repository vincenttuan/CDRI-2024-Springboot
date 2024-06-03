package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.dto.BookingMeetingRoomDto;
import com.example.demo.model.po.BookingMeetingRoom;

public interface BookingDao extends RoomDao {
	
	// 預定會議室
	Integer addBooking(BookingMeetingRoom bookingMeetingRoom);
	
	// 取消預定
	Integer cancelBooking(Integer bookingId);
	
	// 查看所有預定
	List<BookingMeetingRoomDto> findAllBookings();
	
	// 修改預約人
	Integer updateBookingUserId(Integer bookingId, Integer userId);
}
