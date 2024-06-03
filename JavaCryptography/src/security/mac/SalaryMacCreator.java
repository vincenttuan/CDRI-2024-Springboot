package security.mac;

import java.io.File;

import javax.crypto.SecretKey;

import security.KeyUtil;

/*
 * 當我們考慮使用 MAC (Message Authentication Code) 而不是單純的 Hash，
 * 主要的情境是當我們想要確保消息不只是保持完整，還要確保它是從特定的發送者傳來的。
 * 使用 MAC 的時候，我們結合了訊息和一個秘密鑰匙來生成一個摘要(mac value)。
 * 只有知道那把「鑰匙」的人才能生成或驗證這個摘要。

 * 以下是一個場景：假設你是一家公司的財務部門的員工，你每月都會從 HR 部門收到你的薪資明細 my_salary.txt。
 * 為了確保這個消息的真實性和完整性，HR 使用一個他們和你都知道的秘密鑰匙生成 MAC，然後發送給你。
 * 你收到後，也使用同一把鑰匙去驗證 MAC，以確保訊息沒有被更改，且真的是從 HR 部門傳來的。
 */
public class SalaryMacCreator {
	public static void main(String[] args) throws Exception {
		// HR 部門
		// 薪資檔案位置
		String filePath = "src/security/mac/my_salary.txt";
		
		// 建立 HMAC 密鑰
		// 若 macKey.key 已存在則不需要另外產生
		SecretKey macKey = null;
		String keyPath = "src/security/mac/macKey.key";
		
		if(new File(keyPath).exists()) { // 判斷是否有密鑰檔
			// 將密鑰檔 macKey.key 轉密鑰物件
			macKey = KeyUtil.getSecretKeyFromFile("HmacSHA256", keyPath);
		} else {
			// 自行生成密鑰物件
			macKey = KeyUtil.generateKeyForHmac();
			// 將密鑰保存到檔案中
			KeyUtil.saveSecretKeyToFile(macKey, keyPath);
		}
		
		// 取得 MAC 值
		String macValue = KeyUtil.generateMac("HmacSHA256", macKey, filePath);
		
		// 印出 mac value
		System.out.printf("請員工將 HR 提供的 MAC 值記錄下來: %s%n", macValue);
		
	}
}









