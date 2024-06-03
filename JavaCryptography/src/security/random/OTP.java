package security.random;

import java.security.SecureRandom;

/**
 * OTP (One-Time Password) 生成器範例
 *
 * 情境應用：
 * 假設一家銀行想要提供給其客戶一個安全的網上交易平台。
 * 為了確保交易的安全性，銀行決定在客戶進行交易時，發送一個只能使用一次的密碼 (OTP) 到客戶的手機。
 * 客戶在完成交易時需要輸入此OTP。這增加了交易的安全性，因為即使攻擊者得到了客戶的登錄資訊，也無法完成交易，除非他們同時具有該OTP。
 *
 * 這個範例展示了如何使用Java的SecureRandom類生成這樣的OTP。
 */
public class OTP {
	
	// 生成一個六位數的 OTP
	public static String generateOTP() {
		SecureRandom secureRandom = new SecureRandom();
		int number = secureRandom.nextInt(1000000);
		return String.format("%06d", number);
	}
	
	public static void main(String[] args) {
		String otp = generateOTP();
		System.out.printf("您的 OTP: %s%n", otp);
	}
	
	
}



