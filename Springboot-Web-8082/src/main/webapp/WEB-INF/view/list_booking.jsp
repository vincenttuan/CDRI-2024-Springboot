<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<!-- Tomcat 10.x JSTL -->    
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>    
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>List Booking</title>
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" />
		<script type="text/javascript" src="/js/booking.js"></script>
	</head>
	<body style="padding: 0px">
		<!-- 
		bookingId=1, roomId=101, userId=1, bookingDate=2024-05-20, createDate=2024-05-20 12:08:17.0, 
		meetingRoom=MeetingRoom(roomId=101, roomName=101(S), roomSize=10), 
		user=User(id=1, name=John)
		-->
		<div class="pure-form">
			<fieldset>
				<legend>預約列表</legend>
				<table class="pure-table pure-table-bordered" width="100%">
					<thead>
						<tr>
							<th>預約Id</th><th>房間Id</th><th>預約人Id</th><th>預約日期</th>
							<th>建立時間</th><th>房間名稱</th><th>房間最大使用人數</th><th>預約人名稱</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ bookingDtos }" var="booking">
							<tr>
								<td>${ booking.bookingId }</td>
								<td>${ booking.roomId }</td>
								<td>${ booking.userId }</td>
								<td>
									<span class="date-text" data-booking-id="${ booking.bookingId }">
										${ booking.bookingDate }
									</span>
									<input class="date-input" type="date" 
										   data-booking-id="${ booking.bookingId }"
										   value="${ booking.bookingDate }" style="display: none;" />
									
								</td>
								<td>${ booking.createDate }</td>
								<td>${ booking.meetingRoom.roomName }</td>
								<td>${ booking.meetingRoom.roomSize }</td>
								<td>${ booking.user.name }</td>
							</tr>
						</c:forEach>
					</tbody>
					
				</table>
			</fieldset>
		</div>
		
	</body>
</html>










