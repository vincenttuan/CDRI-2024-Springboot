package security.jwt;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.nimbusds.jwt.JWTClaimsSet;

import net.glxn.qrgen.QRCode;
import security.KeyUtil;

// 建立一個高鐵票的 JWT
public class SimpleJWT {

	public static void main(String[] args) throws Exception {
		
		// 1. 高鐵公司的簽名專用密鑰(JWK)
		String signingSecure = "abcdefghijklmnopqrstuvwxyz123456"; // 256bits(32bytes)
		
		// 2. 建立 payload (創建 JWT 的聲明 claims) 裡面就是票務資訊
		
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.jwtID(UUID.randomUUID().toString()) // 生成一個不會重複的 ID 來當作 JWT ID
				.subject("高鐵票")
				.issuer("台灣高鐵")
				.claim("起點", "台北")
				.claim("終點", "台中")
				.claim("艙等", "商務艙")
				.claim("車廂", "6")
				.claim("座位", "8E")
				.claim("車次", "651")
				.claim("日期", "2024-05-27")
				.claim("姓名", "王小名")
				.build();
		
		System.out.printf("JWT ID: %s%n", claimsSet.getJWTID());
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
			System.out.println("驗票閘門開啟...");
		} else {
			System.out.println("JWT 簽名驗證失敗");
		}
		
		
	}

}
