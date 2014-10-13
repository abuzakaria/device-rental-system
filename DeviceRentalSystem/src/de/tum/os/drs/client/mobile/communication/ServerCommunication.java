package de.tum.os.drs.client.mobile.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import de.tum.os.drs.client.parsers.ServerRequestCallback;

public class ServerCommunication extends
		AsyncTask<ServerRequest, Void, ServerResponse> {

	private ServerRequestCallback callback;
	private int CONNECTION_TIMEOUT = 10000;
	private int DATARETRIEVAL_TIMEOUT = 15000;

	// Used to ignore certificate erros when using self-signed certs
	public class NullHostNameVerifier implements HostnameVerifier {

	    public boolean verify(String hostname, SSLSession session) {
	        Log.i("RestUtilImpl", "Approving certificate for " + hostname);
	        return true;
	    }
	}
	
	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
        }
    } };

	public ServerCommunication(ServerRequestCallback callback) {
		this.callback = callback;


		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

	@Override
	protected void onPreExecute() {
		// TODO show progress dialog here
	}

	@Override
	protected ServerResponse doInBackground(ServerRequest... request) {

		HttpsURLConnection urlConnection = null;
		ServerResponse response = new ServerResponse();

		try {

			urlConnection = (HttpsURLConnection) request[0].getUrl()
					.openConnection();
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
			urlConnection.setRequestProperty("Authorization", (new Integer(
					request[0].getSessionId()).toString()));
			urlConnection.setDoOutput(false);

			if (request[0].getMethod() == ServerRequest.HTTP_METHODS.POST) {

				urlConnection.setRequestProperty("Content-Type",
						"application/json; charset=utf8");
				urlConnection.setRequestMethod("POST");

				urlConnection.connect();

				// setup send
				OutputStream os = new BufferedOutputStream(
						urlConnection.getOutputStream());
				os.write(request[0].getJson().toString().getBytes());
				os.flush();
				os.close();

			}

			Log.i("ServerCommunication",
					"Satus code: " + urlConnection.getResponseCode());

			if (!isCodeValid(urlConnection.getResponseCode())) {
				// Something went wrong
				// parse the error code and message and put in the server
				// response

				Log.i("ServerCommunication", "Bad status code");

				InputStream in = new BufferedInputStream(
						urlConnection.getErrorStream());
				JSONObject json = new JSONObject(getResponseText(in));

				response.setStatusCode(urlConnection.getResponseCode());
				response.setErrorMessage(json.getString("message"));

				in.close();

			} else {

				// Everything went fine and the server returned a json object
				Log.i("ServerCommunication", "Good status code");

				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());

				response.setStatusCode(urlConnection.getResponseCode());
				response.setResult(getResponseText(in));

				in.close();

			}

		} catch (ConnectException e) {
			// URL is invalid
			response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
			response.setErrorMessage("The server can't be reached.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// URL is invalid
			response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			// data retrieval or connection timed out
			response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// could not read response body
			// (could not create input stream)
			response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			// response body is no valid JSON string
			response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return response;
	}

	@Override
	protected void onPostExecute(ServerResponse response) {

		// At this point response has:
		// a) A valid json object and a positive status code
		// b) An error status code and an error message
		// c) Only a valid error code in case of a post request

		this.callback.onDownloadComplete(response);
	}

	private static String getResponseText(InputStream inStream) {
		// very nice trick from
		// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		String json = new Scanner(inStream).useDelimiter("\\A").next();
		Log.i("ServerCommunication", json);

		return json;
	}

	public static boolean isCodeValid(int code) {

		return (code < 300 && code >= 200);

	}

}
