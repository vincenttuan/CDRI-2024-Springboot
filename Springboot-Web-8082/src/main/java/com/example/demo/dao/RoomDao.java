package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.po.MeetingRoom;

public interface RoomDao {
	List<MeetingRoom> findAllRooms();
	Optional<MeetingRoom> getRoom(Integer roomId);
	Integer addRoom(MeetingRoom room);
}
