package club.sk1er.mods.publicmod;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class Sk1erMod {

	public static String rawWithAgent(String url) {
		return Sk1erMod.rawWithAgent(url, "Mozilla/4.76");
	}

	public static String rawWithAgent(String url, String userAgent) {
		System.out.println("Fetching: " + url);
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(true);
			connection.addRequestProperty("User-Agent", userAgent);
			connection.setReadTimeout(15000);
			connection.setConnectTimeout(15000);
			connection.setDoOutput(true);
			InputStream is = connection.getInputStream();
			Charset encoding = Charset.defaultCharset();
			String s = IOUtils.toString(is, encoding);
			if (s != null)
				return s;

		} catch (Exception e) {
			e.printStackTrace();
		}
		// JsonObject object = new JsonObject();
		// object.addProperty("success", false);
		// object.addProperty("cause", "Exception");
		// return object.toString();

		return null;
	}

}
