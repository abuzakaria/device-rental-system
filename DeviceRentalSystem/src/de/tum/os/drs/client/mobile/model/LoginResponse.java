package de.tum.os.drs.client.mobile.model;


/**
 * Returned by the server after a login request
 * 
 * @author pablo
 *
 */
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
