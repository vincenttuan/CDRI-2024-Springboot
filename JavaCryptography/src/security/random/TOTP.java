package security.random;

import java.util.Base64;

import security.KeyUtil;

/*
 * 
 * TOTP (Time-based One-Time Password) 是一種一次性密碼（OTP）算法，它的特點是根據當前的時間值生成。
 * 由於TOTP是基於時間生成的密碼，因此當客戶端和伺服器的時鐘保持同步時，它能夠有效運作。
 * TOTP生成的密碼具有固定的短期有效期，過了該時段後，該密碼將不再有效且不被接受。
 * OTP (One-Time Password) 是一種更廣泛的術語，描述了只能使用一次的密碼。
 * TOTP 是 OTP 的一種，但還有其他如 HOTP（基於計數的一次性密碼）。
 * 差別：
 * OTP: 一個泛用術語，描述了只能使用一次的密碼。
 * TOTP: 是 OTP 的一種，其特點是根據當前時間生成密碼。
*/
public class TOTP {
	public static void main(String[] args) throws Exception {
		String secure = Base64.getEncoder().encodeToString("MyKey".getBytes()); // 當作金鑰
		long timeInterval = System.currentTimeMillis() / 1000L / 30L; // 30 秒
		// 使用 HMACSHA256 最為加密算法
		String totp = KeyUtil.generateTOTP(secure, timeInterval, "HMACSHA256");
		System.out.printf("我的 TOTP 密碼: %s%n", totp);
		
	}
}
