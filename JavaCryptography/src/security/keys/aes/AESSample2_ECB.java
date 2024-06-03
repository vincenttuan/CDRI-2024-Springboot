package security.keys.aes;

import java.util.Base64;
import java.util.Scanner;

import javax.crypto.spec.SecretKeySpec;

import security.KeyUtil;

public class AESSample2_ECB {
	
	/*
	 * username  userpassword
	 * -----------------------
	 * admin     viPRerPf6RVeQuHPD/732w==  1234
	 * john      iX27WFyeFoKryxJgNykxdA==  5678
	 * mary      Dr7Bn+4jjA9laP1Tf47cQw==  1111 
	 * haker     viPRerPf6RVeQuHPD/732w==  1234
	 * 
	 * */
	
	private static String userName = "admin"; 
	private static String userPassword = "viPRerPf6RVeQuHPD/732w=="; // 1234
	
	public static void main(String[] args) throws Exception {
		// 建立一個 AES 的 Key (AES-128 bits, 16 bytes)
		final String KEY = "0123456789abcdef"; // 16個字
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("請輸入帳號:");
		String inputUserName = scanner.next();
		System.out.print("請輸入密碼:");
		String inputUserPassword = scanner.next();
		
		if(!inputUserName.equals(userName)) {
			System.out.println("帳號輸入錯誤");
			return;
		}
		
		// 將 inputUserPassword 轉 AES 再轉 Base64 ==> 進行與 userPassword 比較
		byte[] passwordEnctryptedECB = KeyUtil.encryptWithAESKey(aesKeySpec, inputUserPassword);
		String passwordEnctryptedBase64 = Base64.getEncoder()
												.encodeToString(passwordEnctryptedECB);
		if(!passwordEnctryptedBase64.equals(userPassword)) {
			System.out.println("密碼輸入錯誤");
			return;
		}
		
		System.out.println("登入成功");
		
	}

}
