import java.security.MessageDigest;
import java.io.*;
import java.net.*;
import java.security.Principal;
import java.util.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

public class checkIfAvailable{

	public static void main(String[] args) throws Exception{
		if (args.length > 0){
			if (args[0].equals("ios")){
				System.out.println("Creating token for iOS");
				String MACMAC = args[1] + args[1];
				/* Berechnung */
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.reset();
				md5.update(MACMAC.getBytes());
				byte[] result = md5.digest();

				/* Ausgabe */
				StringBuffer hexString = new StringBuffer();
				for (int i=0; i<result.length; i++) {
					if(result[i] <= 15 && result[i] >= 0){
						hexString.append("0");
					}
					hexString.append(Integer.toHexString(0xFF & result[i]));
				}
				System.out.println(hexString.toString());
				check(hexString.toString(), args[2], args[3]);
			}else if (args[0].equals("android")){
				System.out.println("Creating token for Android");
				String IMEI = new StringBuffer(args[1]).reverse().toString();
				/* Berechnung */
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.reset();
				md5.update(IMEI.getBytes());
				byte[] result = md5.digest();

				/* Ausgabe */
				StringBuffer hexString = new StringBuffer();
				for (int i=0; i<result.length; i++) {
					if(result[i] <= 15 && result[i] >= 0){
						hexString.append("0");
					}
					hexString.append(Integer.toHexString(0xFF & result[i]));
				}
				System.out.println(hexString.toString());
				check(hexString.toString(), args[2], args[3]);
			}else{
				System.out.println("Wrong Platform");
			}
		}else{
			System.out.println("No Arguments given");
			System.out.println("Please use \'ios <MAC-Address> <Country_Code> <Phone>_Number>\' for iOS");
			System.out.println("Please use \'android <IMEI> <Country_Code> <Phone>_Number>\' for Android");
		}
	}

	public static void check(String token, String CountryCode, String PhoneNumber){
		String addr = "https://r.whatsapp.net/v1/exist.php?cc=" + CountryCode + "&in=" + PhoneNumber + "&udid=" + token;
		try{
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String text = null;
			while ((text = reader.readLine()) != null){
				if (text.contains("ok") == true){
					System.out.println("Positive Response!");
					conn.disconnect();
					return;
				}
			}
			System.out.println("Negative Response!");
			conn.disconnect();
			return;
		}catch(IOException ex){
			ex.printStackTrace();
			System.out.println("made it here");
		}
	}
	
}