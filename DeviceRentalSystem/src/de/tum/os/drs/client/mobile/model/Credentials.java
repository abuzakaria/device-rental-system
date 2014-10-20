package de.tum.os.drs.client.mobile.model;

import de.tum.os.drs.client.mobile.authentication.Authenticator;

/**
 * Represents the persisted credentials of a loged in user
 * 
 * @author pablo
 *
 */
public class Credentials {

	private String token;
	private Authenticator authenticator;

	public Credentials(String token, Authenticator authenticator) {
		this.token = token;
		this.authenticator = authenticator;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

}
