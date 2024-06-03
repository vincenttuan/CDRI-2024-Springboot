<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.UUID"
    pageEncoding="UTF-8"%>

<% 
	// 生成 CSRF 令牌
	String csrfToken = UUID.randomUUID().toString();
	// 將 CSRF 令牌保存在 session 中
	session.setAttribute("csrfToken", csrfToken);
%>    
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" />
		<title>Login</title>
		<script type="text/javascript">
			// 當網頁載入(進入本頁)時：清除密碼
			window.addEventListener("load", function(event) {
				//console.log('load event = ' + event);
				document.getElementById("password").value = ""; // 密碼欄位置空
			});
			
			// 當網頁卸載(離開本頁)時：清除密碼
			window.addEventListener("beforeunload", function(event) {
				//console.log('beforeunload event = ' + event);
				document.getElementById("password").value = ""; // 密碼欄位置空
			});
			
		</script>
	</head>
	<body style="padding: 20px">
		
		<form method="post" action="/form_auth/report" class="pure-form">
			<!-- 關閉提示：autocomplete="off" -->
			Username：<input type="text" id="username" name="username" autocomplete="off" /> Ex: user<p />
			
			<!-- 防止瀏覽器保存密碼：autocomplete="new-password" -->
			Password：<input type="password" id="password" name="password" autocomplete="new-password" /> Ex: 1234<p />
			
			<!-- 加入 csrf 令牌隱藏欄位 -->
			<input type="hidden" name="csrfToken" value="<%=csrfToken %>">
			
			<input type="submit" value="Login" class="pure-button pure-button-primary" /><p />
			
			<!-- OAuth 2.0 第三方登入 -->
			<a href="/oauth2/github/login">使用 Github 登入 (OAuth2)</a><p />
			
			<!-- OIDC 第三方登入 -->
			<a href="/oidc/google/login">使用 Google 登入 (OIDC)</a><p />
		</form>
		
	</body>
</html>