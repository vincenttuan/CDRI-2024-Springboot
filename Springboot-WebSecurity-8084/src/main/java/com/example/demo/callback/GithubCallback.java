package com.example.demo.callback;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.oauth2.OAuth2Util;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/callback/github")
public class GithubCallback {
	
	@GetMapping
	//@ResponseBody
	public String getToken(@RequestParam("code") String code, HttpSession session) throws Exception {
		// github 會回調一個 code 給我們
		
		// 已有授權碼(code)之後，可以跟 Github 來得到 token (訪問令牌)
		// 有了 token 就可以得到客戶的公開資訊例如: userInfo
		// 1. 根據 code 的得到 token
		String token = OAuth2Util.getGitHubAccessToken(code);
		System.out.println("token: " + token);
		// 2. 透過 token 裡面的 access_token 來取得用戶資訊
		String accessToken = OAuth2Util.parseAccessToken(token);
		System.out.println("accessToken: " + accessToken);
		
		if(accessToken != null) {
			// 取得該用戶在 Github 上的公開資料
			String userInfo = OAuth2Util.getUserInfoFromGitHub(accessToken);
			System.out.println("userInfo: " + userInfo);
			// 取得使用者名稱
			JSONObject userInfoObject = new JSONObject(userInfo);
			System.out.println("login: " + userInfoObject.getString("login"));
			System.out.println("id: " + userInfoObject.getInt("id"));
			System.out.println("email: " + userInfoObject.getString("email"));
			System.out.println("name: " + userInfoObject.getString("name"));
			System.out.println("bio: " + userInfoObject.getString("bio"));
			
			// 1. 將登入資料放到 session 中 ...
			// 查看 login: vincenttuan 是否有存在於會員帳戶資料表中 ?
			// 若沒有則自動幫該使用者進行資料紀錄的新增
			session.setAttribute("loginStatue", true);
			
		} else {
			System.out.println("accessToken is null");
		}
		
		// 2. 重導到登入成功頁面（或指定網頁）
		return "redirect:/form_auth/report";
	}
	
}
