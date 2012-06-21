package tawusel.communcation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tawusel.android.tools.config.PropertyManager;

import android.util.Log;
import android.widget.Toast;

/**
 * @author mareikeziese
 * 
 */
public class JSONCommunicator {

	// Bezeichnung der Klasse:

	private static final String TAG = "JSONCommunicator";
	private static final String JSONServer = "http://10.0.2.2:9000/";

	/**
	 * Get the json-result of the url
	 * 
	 * @param String
	 *            url
	 * @return JSONObject
	 */
	@SuppressWarnings("finally")
	public static JSONObject getJSONObject(String method, String params) throws ClientProtocolException{
		// String host = PropertyManager.getJSONServer();
		// Log.i(TAG,host);
		String url = JSONServer + method + params;
		Log.i(TAG, url);
		System.out.println(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
		JSONObject json = null;

		try {
			response = httpClient.execute(httpGet);
			// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.
			Log.i(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					sb.append(line + "n");
				String result = sb.toString();
				Log.i(TAG, result);
				instream.close();
				json = new JSONObject(result);
				JSONArray nameArray = json.names();
				JSONArray valArray = json.toJSONArray(nameArray);
				for (int i = 0; i < valArray.length(); i++) {
					// TODO: Inhalte der Arrays verarbeiten
				}
			}
		} catch (ClientProtocolException e) {

			throw e;

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			httpGet.abort();
			return json;

		}
	}

	public static String getHashString(String hstr){
			try {
				return SHA1(hstr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	/**
	 * return true if post was successful, else false
	 * 
	 * @param url
	 * @param data
	 * @param objectName
	 * @return boolean
	 */
	@SuppressWarnings("finally")
	public static boolean postJSONObject(String url, JSONObject data,
			String objectName) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(url);
		boolean success = false;

		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(objectName, data.toString());
			postMethod.setParams(params);
			httpClient.execute(postMethod);
			success = true;
			Log.i(TAG, "Post request, data: " + params.toString());

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			postMethod.abort();
			return success;

		}

	}
}