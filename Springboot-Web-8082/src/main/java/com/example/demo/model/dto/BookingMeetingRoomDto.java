package com.example.demo.model.dto;

import java.sql.Timestamp;

import com.example.demo.model.po.MeetingRoom;
import com.example.demo.model.po.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingMeetingRoomDto {
	// 一般欄位資料
	private Integer bookingId;
	private Integer roomId;
	private Integer userId;
	private String bookingDate;
	private Timestamp createDate;
	
	// 關聯欄位(多對一)
	private MeetingRoom meetingRoom;
	private User user;
}
