package de.tum.os.drs.client.mobile.model;

import de.tum.os.drs.client.mobile.authentication.Authenticator;
import de.tum.os.drs.client.parsers.GsonHelper;

public class LoginRequest {

	private String token;
	private Authenticator auth;

	public LoginRequest(String token, Authenticator auth) {
		super();
		this.token = token;
		this.auth = auth;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Authenticator getAuth() {
		return auth;
	}

	public void setAuth(Authenticator auth) {
		this.auth = auth;
	}

	public String asJson() {

		return GsonHelper.getGson().toJson(this);

	}

}
