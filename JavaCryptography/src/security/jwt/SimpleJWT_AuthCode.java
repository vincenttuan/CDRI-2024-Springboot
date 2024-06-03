package security.jwt;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbusds.jwt.JWTClaimsSet;

import net.glxn.qrgen.QRCode;
import security.KeyUtil;

// 建立一個高鐵票的 JWT + AuthCode(授權碼)
public class SimpleJWT_AuthCode {
	// 票務資料表
	static Map<String, List<String>> tickets = new HashMap<>();
	static {
		//           授權碼
		tickets.put("068E", List.of("台北", "台中", "商務艙", "6", "8E", "651", "2024-05-27", "劉一"));
		tickets.put("012A", List.of("台北", "高雄", "經濟艙", "1", "2A", "656", "2024-05-28", "陳二"));
		tickets.put("124C", List.of("雲林", "彰化", "自由座", "12", "4C", "751", "2024-05-29", "張三"));
		tickets.put("123C", List.of("台北", "彰化", "自由座", "12", "3C", "851", "2024-05-30", "李四"));
		tickets.put("035C", List.of("雲林", "台北", "經濟艙", "3", "5C", "721", "2024-05-31", "王五"));
		tickets.put("126C", List.of("台北", "彰化", "自由座", "12", "6C", "711", "2024-05-29", "趙六"));
		tickets.put("047C", List.of("雲林", "高雄", "經濟艙", "4", "7C", "654", "2024-05-27", "孫七"));
		tickets.put("078C", List.of("高雄", "彰化", "自由座", "7", "8C", "123", "2024-05-28", "周八"));
		tickets.put("111C", List.of("台中", "台北", "經濟艙", "11", "1C", "854", "2024-05-29", "吳九"));
		tickets.put("129B", List.of("雲林", "台中", "經濟艙", "12", "9B", "333", "2024-05-29", "鄭十"));
	}
	
	public static void main(String[] args) throws Exception {
		
		// 1. 高鐵公司的簽名專用密鑰(JWK)
		String signingSecure = "abcdefghijklmnopqrstuvwxyz123456"; // 256bits(32bytes)
		
		// 2. 建立 payload (創建 JWT 的聲明 claims) 裡面就是票務資訊
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("高鐵票")
				.issuer("台灣高鐵")
				.claim("authcode", "068E")
				.build();
		
		// 3. 對 JWT 進行簽名並取得 token
		String token = KeyUtil.signJWT(claimsSet, signingSecure);
		
		System.out.printf("高鐵票 token: %s%n", token);
		
		// 4. 產生 QRCode
		File file = new File("src/security/jwt/ticket_qrcode.png");
		QRCode.from(token).withSize(300, 300).writeTo(new FileOutputStream(file));
		System.out.println("QRcode 產生完畢");
		
		//-------------------------------------------------------------------------------
		
		// 5. 驗證 JWT 的簽名
		if(KeyUtil.verifyJWTSignature(token, signingSecure)) {
			System.out.println("JWT 簽名驗證成功");
			// 判斷授權碼是否可以拿到有效資料
			JWTClaimsSet claims = KeyUtil.getClaimsFromToken(token);
			String authCode = claims.getStringClaim("authcode");
			System.out.printf("授權碼: %s%n", authCode);
			// 根據授權碼查詢票務資料
			List<String> data = tickets.get(authCode);
			// 驗證授權碼
			if(data == null || data.size() == 0) {
				System.out.println("授權碼錯誤...");
				return;
			}
			System.out.println("授權碼正確:");
			System.out.printf("票務資訊: %s%n", data);
		} else {
			System.out.println("JWT 簽名驗證失敗");
		}
		
		
	}

}
