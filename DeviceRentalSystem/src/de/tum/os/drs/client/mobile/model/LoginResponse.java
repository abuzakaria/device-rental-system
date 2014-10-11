package de.tum.os.drs.client.mobile.model;


public class LoginResponse {

	private int sessionId;
	private String message;

	public LoginResponse(int sessionId, String message) {
		super();
		this.sessionId = sessionId;
		this.message = message;
	}

	public int getSessionId() {
		return sessionId;
	}

	public String getMessage() {
		return message;
	}
	
	

}
