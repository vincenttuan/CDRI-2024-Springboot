package security.jwt;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nimbusds.jwt.JWTClaimsSet;

import net.glxn.qrgen.QRCode;
import security.KeyUtil;

// 建立一個高鐵票的 JWT, 撤銷令牌/退票
public class SimpleCancelJWT {
	
	// 儲存已撤銷的 JWT (黑名單)
	static Set<String> canceledTokens = new HashSet<>();
	
	public static void main(String[] args) throws Exception {
		
		// 1. 高鐵公司的簽名專用密鑰(JWK)
		String signingSecure = "abcdefghijklmnopqrstuvwxyz123456"; // 256bits(32bytes)
		
		Date time = new Date(new Date().getTime() + (5 * 1000)); //現在時刻 + 5秒
		// 2. 建立 payload (創建 JWT 的聲明 claims) 裡面就是票務資訊
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("高鐵票")
				.issuer("台灣高鐵")
				.claim("authcode", "082E")
				.expirationTime(time) // 設定有效期限
				.build();
		
		// 3. 對 JWT 進行簽名並取得 token
		String token = KeyUtil.signJWT(claimsSet, signingSecure);
		
		System.out.printf("高鐵票 token: %s%n", token);
		
		// 4. 模擬撤銷 JWT
		canceledTokens.add(token); // 將要撤銷的 token 放入指定集合中
		System.out.printf("撤銷 Token: %s%n", token);
		
		//-------------------------------------------------------------------------------
		
		// 5. 驗證 JWT 的簽名
		if(KeyUtil.verifyJWTSignature(token, signingSecure)) {
			System.out.println("JWT 簽名驗證成功");
			// 確認此 Token 是否已被撤銷
			if(canceledTokens.contains(token)) {
				System.out.println("此 Token 已被撤銷 / 高鐵票已經取消");
				return;
			}
			System.out.println("驗票閘門開啟...");
		} else {
			System.out.println("JWT 簽名驗證失敗/令牌已過期");
		}
		
	}

}
