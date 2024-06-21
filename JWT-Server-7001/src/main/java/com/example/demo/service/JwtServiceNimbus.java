package com.example.demo.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JwtServiceNimbus {
    private static final String KEY_FILE = "secret.key"; // 密鑰檔案名稱
    private static byte[] SECRET_KEY; // 密鑰

    static {
        try {
            File file = new File(KEY_FILE);
            // 檢查密鑰檔案是否存在
            if (file.exists()) {
                // 若密鑰檔案已存在，則從檔案中讀取密鑰
                SECRET_KEY = loadKeyFromFile();
            } else {
                // 若密鑰檔案不存在，則生成新的密鑰並保存至檔案
                generateAndSaveKey();
                // 重新從檔案中讀取密鑰，確保密鑰的正確性
                SECRET_KEY = loadKeyFromFile();
            }
        } catch (IOException e) {
            e.printStackTrace(); // 輸出異常信息
        }
    }
    
    // 取得 JWT Token 中 claims 的 username 值
	public Map<String,Object> getJWTClaims(String token) {
		try {
			// 解析 token 为 SignedJWT 对象
			SignedJWT signedJWT = SignedJWT.parse(token);
			// 取得 claims
			JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
			return claimsSet.getClaims();
		} catch (Exception e) {
			e.printStackTrace(); // 輸出異常信息
			return null;
		}
	}
    
    public boolean verifyToken(String token) {
        try {
            // 解析 token 为 SignedJWT 对象
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 創建簽名驗證器，使用相同的密鑰
            MACVerifier verifier = new MACVerifier(SECRET_KEY);

            // 验证 token 签名
            if (!signedJWT.verify(verifier)) {
                return false; // 签名验证失败
            }

            // 检查 token 是否过期
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            Date currentTime = new Date();
            if (expirationTime != null && currentTime.after(expirationTime)) {
                return false; // Token 已过期
            }

            return true; // Token 验证成功
        } catch (Exception e) {
            e.printStackTrace(); // 輸出異常信息
            return false;
        }
    }

    
    public String createToken(String subject, String username, long ttlMillis) throws JOSEException {
        // 創建 JWT Claims (聲明)
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        		.subject(subject) // 設置主題
        		.claim("username", username)
                .issueTime(new Date()) // 設置發行時間
                .expirationTime(new Date(System.currentTimeMillis() + ttlMillis)) // 設置過期時間
                .build();

        // 使用 HMAC 算法創建簽名器
        JWSSigner signer = new MACSigner(SECRET_KEY);

        // 創建 JWT 並簽名
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        // 將簽名後的 JWT 轉為字串格式並返回
        return signedJWT.serialize();
    }
    
    private static void generateAndSaveKey() throws IOException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 生成 256 位的密鑰
        secureRandom.nextBytes(keyBytes);
        // 寫入密鑰至檔案
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(keyBytes);
        }
    }

    private static byte[] loadKeyFromFile() throws IOException {
        // 從檔案中讀取密鑰
        return Files.readAllBytes(Paths.get(KEY_FILE));
    }
}
