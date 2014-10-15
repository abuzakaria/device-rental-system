package de.tum.os.drs.client.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.authentication.Authenticator;
import de.tum.os.drs.client.mobile.authentication.DefaultTrustManager;
import de.tum.os.drs.client.mobile.authentication.SessionManager;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.CredentialStore;
import de.tum.os.drs.client.mobile.model.Credentials;
import de.tum.os.drs.client.mobile.model.LoginRequest;
import de.tum.os.drs.client.mobile.model.LoginResponse;

public class AuthenticationActivity extends Activity {

	private static final String TAG = "Authentication Activity";

	private WebView mWebView;
	private LinearLayout loginOptions;
	private Authenticator mAuthenticator;
	private CredentialStore store;
	private ProgressDialog dialog;

	private Button googleLogin;
	private Button facebookLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		googleLogin = (Button) findViewById(R.id.googleLogin);
		googleLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAuthenticator = Authenticator.google;
				startAuthenticationFlow();
			}

		});

		facebookLogin = (Button) findViewById(R.id.facebookLogin);
		facebookLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAuthenticator = Authenticator.facebook;
				startAuthenticationFlow();
			}

		});

		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.clearCache(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.setWebViewClient(mWebViewClient);

		loginOptions = (LinearLayout) findViewById(R.id.login_buttons);

		store = new CredentialStore(
				PreferenceManager.getDefaultSharedPreferences(this));

		showLoadingDialog();

		// Try to login the user
		login();
	}

	/**
	 * Starts the login flow. It is checked if the user has a valid token
	 * stored, if not, the user is asked for credentials.
	 * 
	 * 
	 */
	private void login() {

		Log.i(TAG, "Attempting to login the user");
		// Check if token exists
		// store.clearCredentials();
		Credentials c = store.getStoredCredentials();

		if (c == null) {

			Log.i(TAG, "No token found, user has to login");

			hideLoadingDialog();
			// We need to start from the beginning, no current token was found
			showLoginOptions();

		} else {

			Log.i(TAG, "Existing token found...");
			mAuthenticator = c.getAuthenticator();
			// There is a token store, but is it still valid?
			checkTokenValidity(c.getToken());
		}

	}

	/**
	 * Checks whether a given token is still
	 * 
	 * 
	 * @param currentToken
	 */
	private void checkTokenValidity(final String currentToken) {

		Log.i(TAG, "Checking token validity");

		(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) {
				URL url;
				HttpsURLConnection conn = null;
				try {

					Uri.Builder b = Uri
							.parse(mAuthenticator.getTokenCheckURL())
							.buildUpon();
					b.appendQueryParameter("access_token", currentToken);

					// Log.i(TAG, b.build().toString());

					url = new URL(b.build().toString());
					conn = (HttpsURLConnection) url.openConnection();
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000);
					conn.connect();

					Log.i(TAG, "Response code " + conn.getResponseCode());

					if (conn.getResponseCode() >= 200
							&& conn.getResponseCode() < 300) {

						InputStream in = new BufferedInputStream(
								conn.getInputStream());
						// Log.i(TAG, getResponseText(in));

						return true;

					} else if (conn.getResponseCode() == 401) {
						// The token was invalid.
						Log.i(TAG, "Inavlid token found. We have to refresh it");

						store.clearCredentials();

					}

					// The token is invalid. We got most likely a 401
					/*
					 * InputStream in = new BufferedInputStream(
					 * conn.getErrorStream()); String error =
					 * getResponseText(in);
					 */
					// Log.e(TAG, error);

					return false;

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} finally {
					if (conn != null) {
						conn.disconnect();
					}

				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean tokenValid) {

				if (tokenValid) {
					// Token is valid! Send the token to the server!
					sendTokenToServer(currentToken);
				} else {
					// The token has expired, we need to reissue it

					mAuthenticator = store.getStoredCredentials()
							.getAuthenticator();

					store.clearCredentials();
					startAuthenticationFlow();

				}

			}

		}).execute();
	}

	private void startAuthenticationFlow() {

		showLoadingDialog();
		getAuthorizationCode();

	}

	private void getAuthorizationCode() {

		String url = getAuthorizationURL(mAuthenticator);
		Log.i(TAG, "Authorization URL: " + url);

		mWebView.loadUrl(url);
		showWebView();
	}

	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if ((url != null) && (url.startsWith(Authenticator.CALLBACK_URL))) {

				mWebView.stopLoading();
				// Hide the callback webpage
				mWebView.setVisibility(View.INVISIBLE);
				mWebView.loadUrl("about:blank");
				parseAuthenticationCode(url);

			} else {
				super.onPageStarted(view, url, favicon);				
			}
		}
		
		@Override
		public void onPageFinished(WebView view, String url){
			hideLoadingDialog();
		}
	};

	private void parseAuthenticationCode(String redirectUrl) {

		//showLoadingDialog();
		
		Uri uri = Uri.parse(redirectUrl);

		if (uri.getQueryParameterNames().contains("error")) {

			Log.i(TAG,
					"An error ocurred while fetchign the auth code: "
							+ uri.getQueryParameter("error"));

			
			hideLoadingDialog();
			showLoginOptions();

		} else {

			String code = uri.getQueryParameter("code");
			Log.i(TAG, "Authorization code from " + mAuthenticator + ": "
					+ code);

			getToken(code);
		}

	}

	private void getToken(String code) {

		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("code", code));
		queryParams.add(new BasicNameValuePair("client_id", mAuthenticator
				.getClientId()));
		queryParams.add(new BasicNameValuePair("client_secret", mAuthenticator
				.getClientSecret()));
		queryParams.add(new BasicNameValuePair("redirect_uri",
				Authenticator.CALLBACK_URL));
		queryParams.add(new BasicNameValuePair("grant_type",
				"authorization_code"));

		POSTRequestListener listener = new POSTRequestListener() {

			@Override
			public void onPOSTComplete(String json) {
				processToken(json);

			}

		};

		(new POSTRequest(listener, mAuthenticator.getTokenAccessURL(),
				queryParams)).execute();
	}

	private void processToken(String response) {

		switch (mAuthenticator) {
		case facebook:
			processFacebookToken(response);
			break;
		case google:
			processGoogleToken(response);
			break;
		default:
			break;

		}

	}

	private void processGoogleToken(String json) {

		Log.i(TAG, "Token recieved : " + json);

		try {
			JSONObject obj = new JSONObject(json);

			String access_token = obj.getString("access_token");

			store.storeCredentials(new Credentials(access_token, mAuthenticator));

			Log.i(TAG, "Token received : " + access_token);

			sendTokenToServer(access_token);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processFacebookToken(String response) {

		Log.i(TAG, response);

		String[] split = response.split("&");
		String access_token = split[0].substring(13, split[0].length());

		Log.i(TAG, "Facebook Token: " + access_token);

		store.storeCredentials(new Credentials(access_token, mAuthenticator));

		sendTokenToServer(access_token);
	}

	private void sendTokenToServer(String currentToken) {

		Log.i(TAG, "Sending token to server...");

		showLoadingDialog();
		
		RentalService service = RentalServiceImpl.getInstance();

		service.login(new LoginRequest(currentToken, mAuthenticator),
				new Callback<LoginResponse>() {

					@Override
					public void onSuccess(LoginResponse result) {

						hideLoadingDialog();

						SessionManager.currentSession = result.getSessionId();
						Log.i(TAG, result.getMessage());
						Log.i(TAG, "Session id: " + result.getSessionId());

						showToast(result.getMessage());

						startMainActivity();
						finish();

					}

					@Override
					public void onFailure(int code, String error) {

						hideLoadingDialog();
						Log.i(TAG, error);
						showToast(error);

						store.clearCredentials();
						showLoginOptions();

					}

				});
	}

	private void startMainActivity() {

		Intent in = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(in);
	}

	private String getAuthorizationURL(Authenticator auth) {

		Uri.Builder builder = Uri.parse(auth.getAuthorizationURL()).buildUpon();
		builder.appendQueryParameter("response_type", "code")
				.appendQueryParameter("client_id", auth.getClientId())
				.appendQueryParameter("redirect_uri",
						Authenticator.CALLBACK_URL)
				.appendQueryParameter("scope", auth.getScope());

		return builder.build().toString();

	}

	private interface POSTRequestListener {

		void onPOSTComplete(String json);

	}

	private class POSTRequest extends AsyncTask<Void, Void, String> {

		private final String url;
		private List<NameValuePair> queryParams;
		private POSTRequestListener listener;

		public POSTRequest(POSTRequestListener listener, String url,
				List<NameValuePair> queryParams) {

			this.url = url;
			this.queryParams = queryParams;
			this.listener = listener;

		}

		@Override
		protected String doInBackground(Void... params) {
			URL urlC;
			HttpsURLConnection conn = null;
			try {
				urlC = new URL(url);
				conn = (HttpsURLConnection) urlC.openConnection();
				conn.setRequestMethod("POST");
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(os, "UTF-8"));

				writer.write(getQuery(queryParams));
				writer.flush();
				writer.close();
				os.close();

				conn.connect();

				Log.i(TAG, "Response code " + conn.getResponseCode());

				if (conn.getResponseCode() >= 200
						&& conn.getResponseCode() < 300) {

					InputStream in = new BufferedInputStream(
							conn.getInputStream());

					return getResponseText(in);

				} else {

					InputStream in = new BufferedInputStream(
							conn.getErrorStream());

					String error = getResponseText(in);

					Log.e(TAG, error);
				}

			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
					dialog.dismiss();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String json) {
			if (json != null) {
				listener.onPOSTComplete(json);
			}
		}
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private static String getResponseText(InputStream inStream) {
		// very nice trick from
		// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		String json = new Scanner(inStream).useDelimiter("\\A").next();
		return json;
	}

	private void showLoginOptions() {

		mWebView.setVisibility(View.INVISIBLE);
		loginOptions.setVisibility(View.VISIBLE);

	}

	private void showWebView() {

		mWebView.setVisibility(View.VISIBLE);
		loginOptions.setVisibility(View.INVISIBLE);

	}

	private void showLoadingDialog() {

		dialog = ProgressDialog.show(this, "Please wait ...", "Logging in..", true);
		dialog.setCancelable(false);
	}

	private void hideLoadingDialog() {

		if (dialog != null) {

			dialog.dismiss();

		}

	}

	private void showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();

	}

}
