package de.tum.os.drs.client.mobile.model;

import de.tum.os.drs.client.mobile.authentication.Authenticator;

public class Credentials {
	
	private String token;
	private Authenticator authenticator;
	private String refreshToken;
	public Credentials(String token, Authenticator authenticator,
			String refreshToken) {
		super();
		this.token = token;
		this.authenticator = authenticator;
		this.refreshToken = refreshToken;
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
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
