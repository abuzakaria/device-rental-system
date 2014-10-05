package de.tum.os.drs.client.mobile.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import de.tum.os.drs.client.parsers.ServerRequestCallback;
import android.os.AsyncTask;
import android.util.Log;

public class ServerCommunication extends
		AsyncTask<ServerRequest, Void, ServerResponse> {

	private ServerRequestCallback callback;
	private int CONNECTION_TIMEOUT = 10000;
	private int DATARETRIEVAL_TIMEOUT = 15000;

	public ServerCommunication(ServerRequestCallback callback) {
		this.callback = callback;

	}

	@Override
	protected void onPreExecute() {
		// TODO show progress dialog here
	}

	@Override
	protected ServerResponse doInBackground(ServerRequest... request) {

		HttpURLConnection urlConnection = null;
		ServerResponse response = new ServerResponse();

		try {

			urlConnection = (HttpURLConnection) request[0].getUrl()
					.openConnection();
			// connection.setRequestProperty("Authorization",basicAuth);
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
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

			Log.i("ServerCommunication", "Satus code: " + urlConnection.getResponseCode());
			
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
