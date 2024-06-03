<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<!-- Tomcat 10.x JSTL -->    
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!-- Spring Form 表單標籤 -->
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<!--
	+---------+-------------+
	| 預約表單   |
	| xxxxx   |
	+---------+    回
	| 取消預約   |    應
	| xxxxx   |    結
	+---------+    果
	| 查詢預約   |
	+---------+
	| 新增會議室 |
	+------- -+-------------+
 -->
<html>
	<head>
		<meta charset="UTF-8">
		<title>Booking MeetingRoom</title>
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" />
	</head>
	<body style="padding: 15px">
		<table>
			<tr>
				<td valign="top" width="25%">
					<!-- 預約表單 -->
					<sp:form modelAttribute="bookingMeetingRoom" 
							 method="post" 
							 action="/booking" 
							 class="pure-form"
							 target="resultFrame">
						<fieldset>
							<legend>預約表單</legend>
							會議室:
								<sp:select path="roomId" items="${ rooms }" itemValue="roomId" itemLabel="roomName" /><p>
							預約人:
								<sp:select path="userId" items="${ users }" itemValue="id" itemLabel="name" /><p>
							預約日:
								<sp:input type="date" path="bookingDate" required="required"/><p>
							<button type="submit" class="pure-button pure-button-primary">送出</button>	
						</fieldset>
					</sp:form>
				</td>
				<td valign="top" width="25%">	
					<!-- 取消預約 -->
					<form class="pure-form" method="post" action="/booking" target="resultFrame">
						<fieldset>
							<legend>取消預約</legend>
							預約Id: <input type="number" id="bookingId" name="bookingId" required="required" /><p>
							<input type="hidden" id="_method" name="_method" value="DELETE" />
							<button type="submit" class="pure-button pure-button-primary">送出</button>
						</fieldset>
					</form>
				</td>
				<td valign="top" width="25%">	
					<!-- 查詢預約 -->
					<form class="pure-form" method="get" action="/booking/findAll" target="resultFrame">
						<fieldset>
							<legend>查詢預約</legend>
							<button type="submit" class="pure-button pure-button-primary">查詢</button>
						</fieldset>
					</form>
				</td>
				<td valign="top" width="25%">	
					<!-- 新增會議室 -->
					<!-- 
					<form class="pure-form" method="post" action="/booking/room" target="resultFrame">
						<fieldset>
							<legend>新增會議室(傳統表單)</legend>
							會議室Id: <input type="number" id="roomId" name="roomId" required="required" /><p />
							會議室名稱: <input type="text" id="roomName" name="roomName" required="required" /><p />
							會議室人數: <input type="number" id="roomSize" name="roomSize" required="required" /><p />
							<button type="submit" class="pure-button pure-button-primary">送出</button>
						</fieldset>
					</form>
					 -->
					<sp:form class="pure-form" method="post" action="/booking/room" target="resultFrame" 
							 modelAttribute="meetingRoom">
						<fieldset>
							<legend>新增會議室(SP表單)</legend>
							會議室Id: <sp:input type="number" path="roomId" required="required" /><p />
							會議室名稱: <sp:input type="text" path="roomName"  name="roomName" required="required" /><p />
							會議室人數: <sp:input type="number" path="roomSize" required="required" /><p />
							<button type="submit" class="pure-button pure-button-primary">送出</button>
						</fieldset>
					</sp:form>
				</td>
			</tr>	
			<tr>	
				<!-- 回應結果 -->
				<td valign="top" colspan="4">
					<iframe name="resultFrame" style="border: 0px" width="1200px" height="1000px" src="/booking/findAll"></iframe>
				</td>
			</tr>
		</table>
		<%=application.getServerInfo() %>
		
	</body>
</html>







