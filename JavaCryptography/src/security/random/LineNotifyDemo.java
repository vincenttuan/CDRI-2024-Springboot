package security.random;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LineNotifyDemo {

    public static void main(String[] args) throws Exception {
        // 1. 要發送的資料
        String message = "OTP:" + OTP.generateOTP();
        // 2. 存取權杖(也稱為:授權 Token)
        String token = "2gGB4P29tI8UAYodsIe7aAF1bmTANdjFu5AnpjllnMN";
        // 3. Line Notify 的發送位置
        String lineNotifyUrl = "https://notify-api.line.me/api/notify";

        // 4. 發送前設定
        byte[] postData = ("message=" + message).getBytes("UTF-8");
        URL url = new URL(lineNotifyUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        // 5. 訊息發送
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }

        // 6. 回應資料
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }
}
