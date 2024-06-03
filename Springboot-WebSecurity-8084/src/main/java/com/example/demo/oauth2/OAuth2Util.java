package com.example.demo.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OAuth2Util {
	// GitHub OAuth 2.0 的客戶端 ID 和密鑰。這些應該在 GitHub 設置中生成。
	// 我們的應用程序的回調 URL(REDIRECT_URI)，這應該與在 GitHub 設置中的回調 URL 相匹配。
	
	public final static String CLIENT_ID = ""; // 你的 GitHub CLIENT ID
    private final static String CLIENT_SECRET = "";	// 你的 GitHub CLIENT SECRET
    public final static String REDIRECT_URI = "https://10.100.53.58:8443/callback/github"; // 你的回調位置
	
    
    //  GitHub 的 OAuth 2.0 授權端點 (Authorization Endpoint)
    //  用戶同意，GitHub 會將他們重定向回應用程序指定的 redirect_uri，並附帶一個授權碼（code）作為參數。
    public final static String AUTH_URL = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI;
	
    // 這是 GitHub 的 OAuth 2.0 令牌端點 (Token Endpoint)。
    // 一旦應用程序從 GitHub 獲得了授權碼，它將呼叫這個端點以交換該授權碼為一個訪問令牌 (access token)。
    // 這個訪問令牌將允許應用程序訪問用戶的 GitHub 資源，直到令牌過期或被撤銷。
    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
	
	/**
     * 根據提供的 GitHub 授權碼來獲取訪問令牌 (access token)。
     * 
     * @param code 從 GitHub 授權後返回的授權碼。
     * @return 返回從 GitHub 獲取的訪問令牌。該令牌用於訪問受保護的 GitHub API 資源。
     * @throws IOException 如果在發送 HTTP 請求或接收 HTTP 回應時出現問題，則會拋出此異常。
     */
    public static String getGitHubAccessToken(String code) throws IOException {
        // 準備請求參數
        String params = "client_id=" + CLIENT_ID + 
                        "&client_secret=" + CLIENT_SECRET + 
                        "&code=" + code + 
                        "&redirect_uri=" + REDIRECT_URI;
        return sendPostRequest(ACCESS_TOKEN_URL, params);
    }

    
    /**
     * 使用 POST 方法發送請求到給定的 URL，並回傳回應內容。
     *
     * @param targetURL 要發送請求的 URL。
     * @param urlParameters POST 方法的參數。
     * @return 伺服器的回應內容。
     */
    private static String sendPostRequest(String targetURL, String urlParameters) throws IOException {
        URL url = new URL(targetURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            os.write(urlParameters.getBytes());
            os.flush();
        }
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * 解析 access_token 從 GitHub 的 OAuth 回應。
     *
     * @param response 從 GitHub OAuth 服務獲得的回應內容。
     * @return 解析出來的 access_token，如果不存在，則返回 null。
     */
    public static String parseAccessToken(String response) {
        String[] pairs = response.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue[0].equals("access_token")) {
                return keyValue[1];
            }
        }
        return null;
    }
    
    /**
     * 使用提供的訪問令牌調用 GitHub API 以獲取用戶資訊。
     * 
     * @param accessToken OAuth 2.0 訪問令牌
     * @return GitHub 用戶的 JSON 表示形式
     * @throws IOException 如果在訪問 API 時發生問題
     */
    public static String getUserInfoFromGitHub(String accessToken) throws IOException {
        // GitHub API 的 URL 用於獲取用戶資訊
        String apiUrl = "https://api.github.com/user";
        URL url = new URL(apiUrl);
        HttpURLConnection apiConn = (HttpURLConnection) url.openConnection();

        // 設置請求方法和添加授權頭部
        apiConn.setRequestMethod("GET");
        apiConn.setRequestProperty("Authorization", "Bearer " + accessToken);

        // 讀取響應
        BufferedReader apiIn = new BufferedReader(new InputStreamReader(apiConn.getInputStream(), "UTF-8"));
        String apiLine;
        StringBuffer apiResponse = new StringBuffer();
        while ((apiLine = apiIn.readLine()) != null) {
        	apiResponse.append(apiLine);
        }
        apiIn.close();
        
        return apiResponse.toString();
    }
}
