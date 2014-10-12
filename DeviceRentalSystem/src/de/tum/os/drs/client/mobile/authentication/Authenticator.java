package de.tum.os.drs.client.mobile.authentication;

public enum Authenticator {
	google(
			"155367116448-2teug5re8bb7bthani6hv0ohvgcisq1a.apps.googleusercontent.com",
			"MgCyA66FP-j1bkijAnObLYb4",
			"https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
			"https://accounts.google.com/o/oauth2/auth", 
			"https://accounts.google.com/o/oauth2/token",
			"https://www.googleapis.com/oauth2/v2/userinfo",
			"https://accounts.google.com/o/oauth2/revoke"),
	
	facebook("816880461697165", "4d20569eacb96ebae5131156abf5a714", "email user_about_me", "", "","", "");

	public String getTokenAccessURL() {
		return tokenAccessURL;
	}

	public static final String CALLBACK_URL = "http://www.example.com/oauth2callback";
	
	private final String clientId;
	private final String clientSecret;
	private final String scope;
	private final String authorizationURL;
	private final String tokenAccessURL;
	private final String tokenCheckURL;
	private final String revokationURL;

	Authenticator(String clientId, String clientSecret, String scope,
			String authorizationURL, String tokenAccessURL, String tokenCheckURL, String revokationUrl) {

		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.authorizationURL = authorizationURL;
		this.tokenAccessURL = tokenAccessURL;
		this.tokenCheckURL = tokenCheckURL;
		this.revokationURL = revokationUrl;

	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public String getAuthorizationURL() {
		return authorizationURL;
	}

	public String getTokenCheckURL() {
		return tokenCheckURL;
	}

	public String getRevokationURL() {
		return revokationURL;
	}
	
	 public static Authenticator toAuthenticator(String myEnumString) {
	        try {
	            return valueOf(myEnumString);
	        } catch (Exception ex) {
	                // For error cases
	            return google;
	        }
	    }

}
