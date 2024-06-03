package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoom {
	private Integer roomId;
	private String roomName;
	private Integer roomSize;
	
}
