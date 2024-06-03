package security.hash;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

import security.KeyUtil;

// 加鹽 Hash
public class SimpleAddSaltHash {

	public static void main(String[] args) throws Exception {
		// 1.設定一個密碼
		String password = "8888";
		
		// 2.隨機生成一個鹽(Salt)
		byte[] salt = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt); // 填充隨機值
		System.out.printf("鹽: %s%n", Arrays.toString(salt));
		System.out.printf("鹽(Hex): %s%n", KeyUtil.bytesToHex(salt));
		
		// 3.獲取 SHA-256 消息摘要物件來幫助我們生成密碼的哈希
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		// 4.加鹽
		messageDigest.update(salt);
		
		// 5.將密碼轉換為 byte[] 然後生成哈希
		byte[] hashedBytes = messageDigest.digest(password.getBytes());
		
		// 6.將 byte[] 轉 Hex
		String hashedHexString = KeyUtil.bytesToHex(hashedBytes);
		System.out.printf("原始密碼: %s%n", password);
		System.out.printf("加鹽後的哈希密碼: %s%n", hashedHexString);
		
		//-----------------------------------------------------------------------
		// 7.模擬使用者輸入密碼進行驗證
		Scanner sc = new Scanner(System.in);
		System.out.print("請輸入密碼: ");
		String inputPassword = sc.nextLine();
		
		// 8.生成使用者輸入密碼的哈希值
		messageDigest.reset(); // 重制
		messageDigest.update(salt); // 加鹽
		byte[] inputHashedBytes = messageDigest.digest(inputPassword.getBytes());
		String inputHashedHexString = KeyUtil.bytesToHex(inputHashedBytes);
		
		// 9.驗證密碼
		if(inputHashedHexString.equals(hashedHexString)) {
			System.out.println("驗證成功");
		} else {
			System.out.println("驗證失敗");
		}
		
	}

}
