package de.tum.os.drs.client.mobile.model;

import de.tum.os.drs.client.mobile.R;
import de.tum.os.drs.client.mobile.authentication.Authenticator;
import android.content.SharedPreferences;

public class CredentialStore {

	private SharedPreferences prefs;

	private final static String TOKEN_KEY = "current_token";
	private final static String REFRESH_TOKEN_KEY = "refresh_token";
	private final static String AUTHENTICATOR_KEY = "current_authenticator";

	public CredentialStore(SharedPreferences prefs) {

		this.prefs = prefs;

	}

	public void clearCredentials() {

		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(TOKEN_KEY);
		editor.remove(REFRESH_TOKEN_KEY);
		editor.remove(AUTHENTICATOR_KEY);
		editor.commit();

	}

	public void storeCredentials(Credentials credentials) {

		clearCredentials();

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TOKEN_KEY, credentials.getToken());
		editor.putString(REFRESH_TOKEN_KEY, credentials.getRefreshToken());
		editor.putString(AUTHENTICATOR_KEY, credentials.getAuthenticator()
				.toString());
		editor.commit();
	}

	public Credentials getStoredCredentials() {

		String token = prefs.getString(TOKEN_KEY, "");
		String refreshToken = prefs.getString(REFRESH_TOKEN_KEY, "");
		String authString = prefs.getString(AUTHENTICATOR_KEY, "");

		if (token.isEmpty() || refreshToken.isEmpty() || authString.isEmpty()) {
			clearCredentials();
			return null;
		} else {

			return new Credentials(token,
					Authenticator.toAuthenticator(authString), refreshToken);
		}
	}

	public void refreshToken(String newToken) {

		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(TOKEN_KEY);
		editor.putString(TOKEN_KEY, newToken);
		editor.commit();

	}

}
