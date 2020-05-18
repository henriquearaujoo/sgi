package util;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;



public class CheckAddress {

	public static void main(String[] args) throws IOException {
			//verificaData();
			
			Pattern pat = Pattern.compile("((\\\\d)*([a-z])*([A-Z])*([@#$%])*){6,20}");
			
			if (!pat.matcher("senha1@").matches()) {
			 System.out.println("Teste!!!");  
			}
			
	}

	
	public static int verificaData() throws IOException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		
		System.out.println(sdf.format(calendar.getTime()));
		
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 60);
//		
//		BigDecimal t1 = new BigDecimal("5");
//		BigDecimal t2 = new BigDecimal("4");
//		
//		System.out.println(t1.compareTo(t2));
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(calendar.getTime());
		
		return 0;
	}
	
	
	public static int verificaInternet2() throws IOException {

		try {
			java.net.URL mandarMail = new java.net.URL("http://fas-xquiz.dyndns-ip.com:10510/pir");
			java.net.URLConnection conn = mandarMail.openConnection();
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
			httpConn.connect();
			int x = httpConn.getResponseCode();
			if (x == 200) {
				System.out.println("Conectado");
			}

			return x;
		} catch (java.net.MalformedURLException urlmal) {
			return 0;
		} catch (java.io.IOException ioexcp) {
			return 0;
		}

	}

	public static Boolean verificaInternet() throws IOException {
		InetAddress endereco = InetAddress.getByName("sgi.fas-amazonas.org");

		// InetAddress endereco =
		// InetAddress.getByName("fas-xquiz.dyndns-ip.com:10510");

		if (endereco.isReachable(30000)) {
			return true;
		}

		return false;
	}

	public static String getLatest(String str, int pos) {
		String out = "";
		str = lpad(str, pos + 1, "0");

		str = str.substring(str.length() - pos);

		return str;
	}

	public static String lpad(String str, int pos, String caracter) {
		String out = "";
		if (str == null)
			str = "";
		int index = str.length();

		for (int i = index; i <= pos - 1; i++) {
			str = caracter + str;
		}

		return str;
	}

}
