package com.example.demo.model.po;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingMeetingRoom {
	private Integer bookingId;
	private Integer roomId;
	private Integer userId;
	private String bookingDate;
	private Timestamp createDate;
}
