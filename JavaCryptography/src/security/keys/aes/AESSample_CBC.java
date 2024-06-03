package security.keys.aes;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import security.KeyUtil;

public class AESSample_CBC {
	public static void main(String[] args) throws Exception {
		// 建立一個 AES 的 Key (AES-128 bits, 16 bytes)
		final String KEY = "0123456789abcdef"; // 16個字
		// 建立 AES 密要規範
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		//--加密------------------------------------------------------------------
		// CBC 模式
		// 建立 IV
		// 自行定義 IV 內容
		//byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		// 透過 SecureRandom 定義 IV 內容
		
		byte[] iv = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);
		
		System.out.println("KEY: " + KEY);
		System.out.println("IV: " + Arrays.toString(iv));
		// 進行 CBC 資料加密
		byte[] encryptedCBC = KeyUtil.encryptWithAESKeyAndIV(aesKeySpec, "蔬菜蛋餅", iv);
		System.out.println("CBC 加密後: " + Base64.getEncoder().encodeToString(encryptedCBC));
		//--解密------------------------------------------------------------------
		String decryptedCBC = KeyUtil.decryptWithAESKeyAndIV(aesKeySpec, encryptedCBC, iv);
		System.out.println("CBC 解密後: " + decryptedCBC);
		
	}
}
