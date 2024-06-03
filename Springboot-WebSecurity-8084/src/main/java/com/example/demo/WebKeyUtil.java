package com.example.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebKeyUtil {
	
	// 預設的用戶名和密碼，用於 HTTP Basic 和 Digest 認證
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "1234";
    
    // 預設的用戶名和密碼，用於 HTTP FROM 認證
    private static final String VALID_FORM_USERNAME = "user";
    private static final String VALID_FORM_PASSWORD = "Rn/mkRKRO9NwgZC80a7ItA=="; // 請自行填寫
   
    // 用於生成 nonce 的預設值
    private static final String NONCE_SECRET = "somesecret"; // 請自行填寫
    
    /**
     * 驗證表單所提供的 username, password 是否正確。
     * 
     * @return 如果認證成功則返回 true，否則返回 false。
     */
    public static boolean checkFormLogin(String username, String password) {
    	return VALID_FORM_USERNAME.equals(username) && VALID_FORM_PASSWORD.equals(password);
    }
    
    /**
     * 驗證提供的認證信息是否正確。
     * 
     * @param authHeader 從 HTTP 請求中提取的 Authorization 標頭的值。
     * @return 如果認證成功則返回 true，否則返回 false。
     */
    public static boolean isValidBasicAuth(String authHeader) {
        StringTokenizer tokenizer = new StringTokenizer(authHeader);
        if (tokenizer.hasMoreTokens()) {
            String basic = tokenizer.nextToken();

            if (basic.equalsIgnoreCase("Basic")) {
                try {
                    String credentials = new String(Base64.getDecoder().decode(tokenizer.nextToken()), "UTF-8");
                    StringTokenizer userPass = new StringTokenizer(credentials, ":");
                    String username = userPass.nextToken();
                    String password = userPass.nextToken();
                    
                    // 驗證用戶名和密碼是否與我們模擬的數據庫中的匹配
                    return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * 生成HTTP Basic認證的挑戰。
     * 
     * @param realm 用於定義受保護資源的領域名稱。
     * @return 返回用於WWW-Authenticate頭的HTTP Basic認證挑戰。
     */
    public static String generateBasicChallenge(String realm) {
        return "Basic realm=\"" + realm + "\"";
    }
    
    /**
     * 將輸入的字串轉換為MD5摘要。
     * 
     * @param input 需要被轉換為MD5的字串。
     * @return 返回MD5摘要。
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 將byte陣列轉換為十六進制字串。
     * 
     * @param bytes 需要被轉換的byte陣列。
     * @return 返回十六進制字串。
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 驗證Digest認證的有效性。
     * 
     * @param authHeader 從HTTP請求中獲取的'Authorization'頭部。
     * @param httpMethod HTTP請求方法（例如GET、POST等）。
     * @param uri 請求的URI。
     * @param REALM 領域名稱（唯讀）。
     * @return 如果摘要驗證成功則返回true，否則返回false。
     */
    public static boolean isValidDigest(String authHeader, String httpMethod, String uri, final String REALM) {
    	// 解析 authHeader 來提取需要的值
        String usernameFromHeader = null, response = null, nonce = null, nc = null, cnonce = null, qop = null;
        
        String[] authParts = authHeader.substring(7).split(", ");
        for (String part : authParts) {
            String[] keyValue = part.split("=");
            switch (keyValue[0]) {
                case "username":
                    usernameFromHeader = keyValue[1].replaceAll("\"", "");
                    break;
                case "response":
                    response = keyValue[1].replaceAll("\"", "");
                    break;
                case "nonce":
                    nonce = keyValue[1].replaceAll("\"", "");
                    break;
                case "nc":
                    nc = keyValue[1];
                    break;
                case "cnonce":
                    cnonce = keyValue[1].replaceAll("\"", "");
                    break;
                case "qop":
                    qop = keyValue[1];
                    break;
                case "uri":
                    uri = keyValue[1].replaceAll("\"", "");
                    break;
            }
        }

        if (!VALID_USERNAME.equals(usernameFromHeader)) {
            return false; // 用戶名無效
        }

        String ha1 = md5(VALID_USERNAME + ":" + REALM + ":" + VALID_PASSWORD);
        String ha2 = md5(httpMethod + ":" + uri);
        String computedResponse = md5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2);

        return computedResponse.equals(response);
    }

    /**
     * 生成一個摘要驗證的挑戰。
     * 
     * @param realm 用於定義受保護資源的領域名稱。
     * @return 返回用於WWW-Authenticate頭部的摘要驗證挑戰。
     */
    public static String generateDigestChallenge(String realm) {
        long nonceValue = new Random().nextLong();
        String nonce = md5(NONCE_SECRET + nonceValue);
        return "Digest realm=\"" + realm + "\", qop=\"auth\", nonce=\"" + nonce + "\"";
    }
    
    /**
     * 提供從 context.xml 或 web.xml 中獲取金鑰的功能。
     * 
     * 從 context.xml 的 <Environment name="secretKey"> 中取得金鑰。
     * 或 web.xml 的 <env-entry> 中取得金鑰。
     *
     * @return 從配置中獲取的金鑰對應的 String
     * @throws RuntimeException 當無法從上下文中獲取JWT的金鑰時拋出
     */
    public static String getSecretKeyStringFromContext() {
        try {
            // 1. 建立InitialContext物件，它代表JNDI查找的起始點。
            Context initialContext = new InitialContext();
            
            // 2. 對"java:/comp/env"進行查找。此JNDI上下文用於Java EE中查找應用程序的環境條目和資源引用。
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            
            // 3. 取得 context.xml 或 web.xml中名為 "secretKey" 的資源的值。
            // 對應到 web.xml：<env-entry-name>secretKey</env-entry-name>
            //       context.xml：<Environment name="secretKey" ...
            String secretKeyBase64 = (String) envContext.lookup("secretKey");

            return secretKeyBase64;
        } catch (NamingException e) {
            throw new RuntimeException("從上下文中獲取JWT金鑰時出錯", e);
        }
    }
    
    /**
     * 提供從 context.xml 或 web.xml 中獲取金鑰的功能。
     * 
     * 從 context.xml 的 <Environment name="secretKey"> 中取得金鑰。
     * 或 web.xml 的 <env-entry> 中取得金鑰。
     *
     * @return 從配置中獲取的金鑰對應的 byte 數組
     * @throws RuntimeException 當無法從上下文中獲取JWT的金鑰時拋出
     */
    public static byte[] getSecretKeyFromContext() {
    	// 從配置中獲取的金鑰對應的 String
        String secretKeyBase64 = getSecretKeyStringFromContext();
        // 解碼Base64格式的金鑰，並返回其byte數組形式。
        return Base64.getDecoder().decode(secretKeyBase64);
    }
    
    /**
     * 生成指定長度的隨機密鑰，並將其 Base64 編碼。
     *
     * @param byteLength 欲生成的密鑰的字節長度。例如，若要生成 256 位的密鑰，字節長度應為 32。
     * @return 返回 Base64 編碼的密鑰。
     *
     * 使用方法:
     * - SecureRandom 用於生成加密安全的隨機數。
     * - 這個方法首先使用 SecureRandom 生成指定長度的隨機密鑰。
     * - 然後將生成的密鑰進行 Base64 編碼，這樣可以將它安全地存儲或轉輸，同時避免任何不打印或特殊的字符。
     */
    public static String generateSecret(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();   // 創建一個加密安全的隨機數生成器
        byte[] key = new byte[byteLength];                // 分配用於保存密鑰的空間
        secureRandom.nextBytes(key);                      // 生成隨機密鑰
        return Base64.getEncoder().encodeToString(key);   // 將密鑰 Base64 編碼並返回
    }
    
    /**
     * 根據提供的字串密鑰進行加密的方法.
     *
     * @param input         需要加密的字符串.
     * @param stringKey     字串格式的密鑰.
     * @return String       返回加密後的字符串（Base64編碼）.
     * @throws Exception    當加密過程中出現問題時拋出異常.
     */
    public static String encrypt(String input, String stringKey) throws Exception {
        // 轉換字串密鑰為SecretKey對象
        byte[] decodedKey = Base64.getDecoder().decode(stringKey);
        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // 使用AES算法
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(input.getBytes());

        // 轉換成Base64格式的字符串
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    /**
     * 從指定的XML文件中讀取名為'secretKey'的環境變量的值。
     *
     * @param filePath XML文件的完整路徑。
     * @return secretKey 的值，如果未找到則為 null。
     * @throws Exception 當讀取或解析XML時發生錯誤。
     */
    public static String getSecretKeyFromContextXml(String filePath) throws Exception {
        // 創建DocumentBuilderFactory對象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // 使用工廠創建DocumentBuilder對象
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // 解析XML，獲得Document對象，即DOM樹
        Document document = builder.parse(filePath);
        
        // 獲取所有名為"Environment"的節點
        NodeList nodeList = document.getElementsByTagName("Environment");
        
        // 遍歷所有"Environment"節點
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 獲取每個節點
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // 轉換為Element，便於訪問其屬性
                Element element = (Element) node;
                
                // 檢查是否有name屬性，且值為"secretKey"
                if (element.hasAttribute("name") && "secretKey".equals(element.getAttribute("name"))) {
                    // 返回'secretKey'的值
                    return element.getAttribute("value");
                }
            }
        }
        
        // 如果沒有找到名為'secretKey'的環境變量，返回null
        return null;
    }
    
    /**
     * 要從十六進制格式的雜湊字串轉回 byte[]
     * 
     * @return 返回 byte[]。
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
}
