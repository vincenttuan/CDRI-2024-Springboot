package com.example.demo.oidc;

import java.io.IOException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//用戶端編號: 
//用戶端密鑰: 
//回調的 URI: https://localhost:8443/callback/google
//google 網頁應用程式 的用戶端 ID
//https://console.cloud.google.com/apis/credentials/oauthclient/

public class OIDCUtil {
	private static final String CLIENT_ID = ""; // 用戶端編號
	private static final String CLIENT_SECRET = ""; // 用戶端密鑰
	private static final String REDIRECT_URI = "https://localhost:8443/callback/google"; // 回調的 URI
	
	/**
     * 從HttpServletRequest中提取ID Token。
     * 此方法從"Authorization"標頭讀取資訊，此標頭中應包含類型為"Bearer"的ID Token。
     * 如果ID Token存在，方法會返回它；否則，返回null。
     *
     * @param req 用戶的HttpServletRequest，包含來自用戶的請求資訊和標頭。
     * @param resp 用於向客戶端發送響應的HttpServletResponse。
     * @return 如果存在，則返回ID Token字符串；如果缺少或格式不正確，則返回null。
     * @throws IOException 如果處理請求或設置響應時出現IO問題，則拋出此異常。
     */
    public static String extractIdTokenFromHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 從 'Authorization' 標頭獲取ID Token，通常是“Bearer <token>”的格式。
        String authHeader = req.getHeader("Authorization");
        String idToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 從標頭中提取ID Token，去除開頭的"Bearer "部分。
            idToken = authHeader.substring("Bearer ".length());
        } else {
            // 如果標頭無效或不存在，設置適當的錯誤狀態碼。
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("未獲授權: 缺少 ID Token");
            // 由於缺少必要的憑證，因此不再進行進一步處理。
            return null;
        }

        // 返回提取的ID Token
        return idToken;
    }
    
	/**
	 * 建立並獲取用於OAuth 2.0授權的Google授權URL。
	 * 此方法會創建一個包含指定參數的URL，用戶通過此URL進行Google賬戶的登錄和授權。
	 * 
	 * @param responseType OAuth 2.0響應類型，通常是"code"，表示會返回授權碼（authorization code）。
	 * @param scope        指定應用程式打算存取的Google API的範圍。這些範圍由空格分隔。
	 *                     請求多個範圍時，應用程式將獲得用戶數據訪問的聯合授權。
	 * @return 返回用戶授權的完整URL，用於重定向用戶到Google的登錄和授權頁面。
	 */
	public static String getAuthorizationUrl(String responseType, String scope) {
	    // 格式化URL，其中包含客戶端ID、重定向URI、響應類型（授權碼）和範圍
	    return String.format(
	        "https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s",
	        CLIENT_ID, REDIRECT_URI, responseType, scope
	    );
	}

	
	/**
	 * 根據授權碼從 Google 獲取 ID Token。
	 * 
	 * 此方法會向 Google 的令牌端點發送一個 POST 請求，包含從 OAuth 2.0 重定向獲得的授權碼。
	 * 如果請求成功，Google 會返回一個包含 ID Token 的響應。
	 * 
	 * @param authorizationCode 代表用戶授予應用程序訪問權限的授權碼。
	 * @return 一個代表用戶身份的 JWT 的字符串形式，即所謂的 ID Token。
	 * @throws Exception 如果在發送請求或解析響應時出現錯誤。
	 */
	public static String getIDToken(String authorizationCode) throws Exception {
	    // 構建向 Google Token endpoint 的 POST 請求
	    String response = Request.Post("https://oauth2.googleapis.com/token")
	        .bodyForm(Form.form()
	        .add("code", authorizationCode)
	        .add("client_id", CLIENT_ID)
	        .add("client_secret", CLIENT_SECRET)
	        .add("redirect_uri", REDIRECT_URI)
	        .add("grant_type", "authorization_code")
	        .build())
	        .execute().returnContent().asString();

	    // 解析響應JSON
	    JSONObject jsonResponse = new JSONObject(response);

	    // 從響應中獲取 ID Token（注意只能得到一次）
	    return jsonResponse.getString("id_token");
	}

	
	/**
	 * 解析 ID Token 並獲取其 JWT 負載（payload）。
	 * 
	 * 此方法會解析 JWT（JSON Web Token）格式的 ID Token，從中提取負載（payload）部分，
	 * 也就是包含用戶身份信息的 JSON 對象。這些信息可能包括用戶的唯一標識符、名稱、電子郵件地址等。
	 * 
	 * @param idToken 一個代表用戶身份的 JWT 的字符串形式，即所謂的 ID Token。
	 * @return 代表 JWT 負載部分的 JSON 字符串，其中包含了用戶的身份信息。
	 * @throws Exception 如果在解析 ID Token 或提取負載時出現錯誤。
	 */
	public static String extractJWTPayload(String idToken) throws Exception {
	    // 解析 ID Token 以獲取 JSON 對象
	    SignedJWT signedJWT = SignedJWT.parse(idToken);

	    // 獲取 JWT 的 payload 部分（這裡包含了實際的 JSON 數據）
	    JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

	    // 如果您需要以字符串形式打印整個 payload (也就是 JWT 的 JSON 部分)
	    String jsonPayload = jwtClaimsSet.toJSONObject().toString();

	    return jsonPayload;
	}
	
	/**
	 * 從 ID Token 中獲取聲明集並驗證它。
	 * 
	 * 本方法將解析和驗證從 Google 獲得的 ID Token。驗證過程包括檢查簽名、發行者、受眾以及檢查 Token 是否已過期。
	 * 只有在 ID Token 通過所有驗證步驟後，此方法才會返回聲明集，否則將拋出異常。
	 *
	 * @param idToken 一個 JWT 格式的 ID Token 字串，從 Google 獲得。
	 * @return JWTClaimsSet 包含 ID Token 的聲明集信息。
	 * @throws Exception 如果 ID Token 驗證失敗或解析出錯。
	 */
	public static JWTClaimsSet getClaimsSetAndVerifyIdToken(String idToken) throws Exception {
	    // 創建 JWT 處理器來解析和驗證 ID Token
	    ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();

	    // 設置 JWK 來源，這裡使用 Google 發布的公開金鑰
	    JWKSource keySource = new RemoteJWKSet(new URL("https://www.googleapis.com/oauth2/v3/certs"));
	    JWSKeySelector keySelector = new JWSVerificationKeySelector(JWSAlgorithm.RS256, keySource);
	    jwtProcessor.setJWSKeySelector(keySelector);

	    // 設置預期的受眾（aud）值，這應該與您的 OAuth 2.0 客戶端 ID 相符
	    String expectedAudience = CLIENT_ID; // "您的客戶端ID";

	    // 解析 ID Token 並進行驗證，返回聲明集
	    JWTClaimsSet claimsSet = jwtProcessor.process(idToken, null); // 使用 null 作為 context 參數，因為在此處不需要它

	    // 驗證 ID Token 的發行者（iss）、受眾（aud）和過期時間（exp）
	    String issuer = claimsSet.getIssuer();
	    List<String> audience = claimsSet.getAudience();
	    Date expirationTime = claimsSet.getExpirationTime();

	    // 檢查發行者和受眾是否有效
	    if (!"https://accounts.google.com".equals(issuer) || !audience.contains(expectedAudience)) {
	        throw new BadJWTException("發行者或受眾無效");
	    }

	    // 檢查 ID Token 是否已過期
	    if (new Date().after(expirationTime)) {
	        throw new BadJWTException("Token 已過期");
	    }
	    
	    // 如果一切正常，則返回聲明集
	    return claimsSet;
	}
	
	/**
	 * 向指定的報告服務發送帶有身份驗證信息的安全HTTP請求。
	 * 
	 * 這個方法會通過 'Authorization' 標頭，將 Google 登錄後獲得的 ID Token 作為 Bearer Token 加入到 HTTP 請求中。
	 * 這是一種標準的 OAuth 2.0 身份驗證方式，有助於保護敏感資料的安全性。此外，本方法還包含了一個自訂的信任策略，
	 * 允許系統接受自簽名SSL證書，這在與使用這類證書的內部服務或開發環境通訊時特別有用。
	 *
	 * @param idToken 用戶從 Google 登錄後獲得的 ID Token 字串，將用於身份驗證。
	 * @param clientResponse 用於向客戶端發送回應的 HttpServletResponse 物件。
	 * @param resourceURL 接收ID Token的服務的URL。
	 * @return 包含 statusCode, data, 和 error（如果有的話）的 Map 物件，這些資訊來自服務的回應。
	 */
	public static Map<String, String> sendAuthenticatedRequestToService(String idToken, String resourceURL) {
	    // 創建一個 map 來儲存和傳遞響應資訊
	    Map<String, String> responseDetails = new HashMap<>();
	    responseDetails.put("statusCode", null);
	    responseDetails.put("data", null);
	    responseDetails.put("error", null);
	    
		try {
	        // 創建一個信任所有證書的 X509TrustManager
	        X509TrustManager trustAllCerts = new X509TrustManager() {
	            public X509Certificate[] getAcceptedIssuers() {
	                return new X509Certificate[0];
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        };

	        // 設置 SSL 上下文以使用我們的 trustAllCerts
	        SSLContext sslContext = SSLContextBuilder.create()
	                .loadTrustMaterial(null, (certificate, authType) -> true) // 信任所有證書
	                .build();
	        sslContext.init(null, new TrustManager[]{trustAllCerts}, new java.security.SecureRandom());

	        // 創建 HttpClient，它接受自簽名證書並且關閉主機名驗證
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLContext(sslContext)
	                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                .build();

	        // 創建 HttpPost 請求並設置目標 URL
	        HttpPost httpPost = new HttpPost(resourceURL);

	        // 在 'Authorization' 標頭中放入 ID Token，遵循 Bearer Token 的格式
	        // 這樣做的好處是保護了敏感資料的安全，並且是 OAuth 2.0 標準建議的做法
	        httpPost.setHeader("Authorization", "Bearer " + idToken);

	        // 執行 HTTP 請求，獲取和處理服務的回應
	        CloseableHttpResponse serviceResponse = httpClient.execute(httpPost);
	        
	        try {
	            // 獲取響應的內容和狀態碼
	            HttpEntity entity = serviceResponse.getEntity();
	            int statusCode = serviceResponse.getStatusLine().getStatusCode();
	            responseDetails.put("statusCode", statusCode + "");
	            // 根據需要處理響應內容
	            // 如果狀態碼是 200，這表明報告服務成功接收並處理了 ID Token
	            if (statusCode == 200) {
	                String responseString = EntityUtils.toString(entity, "UTF-8");
	                // 將響應內容放到 map['data'] 中
	                responseDetails.put("data", responseString);
	            } else {
	                // 處理錯誤響應
	            	responseDetails.put("error", statusCode + " error");
	            }
	        } finally {
	        	serviceResponse.close();
	        }
	        
	    } catch (Exception e) {
	    	// 在出現異常時捕獲並記錄，例如網絡問題或 SSL 設置錯誤
	        responseDetails.put("error", e.toString());
	        e.printStackTrace();
	    }
		// 返回收集到的回應細節
	    return responseDetails;
	}
}
