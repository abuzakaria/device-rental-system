package de.tum.os.drs.client.mobile.authentication;

/**
 * This class contains the parameters of the authenticators. 
 * 
 * 
 * @author pablo
 *
 */
public enum Authenticator {
	
	//TODO changeme
	google(
			"155367116448-2teug5re8bb7bthani6hv0ohvgcisq1a.apps.googleusercontent.com",
			"MgCyA66FP-j1bkijAnObLYb4",
			"https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
			"https://accounts.google.com/o/oauth2/auth", 
			"https://accounts.google.com/o/oauth2/token",
			"https://www.googleapis.com/oauth2/v2/userinfo"),
	
	facebook("816880461697165", 
			"4d20569eacb96ebae5131156abf5a714",
			"email user_about_me", 
			"https://www.facebook.com/dialog/oauth", 
			"https://graph.facebook.com/oauth/access_token",
			"https://graph.facebook.com/me?access_token=");

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

	Authenticator(String clientId, String clientSecret, String scope,
			String authorizationURL, String tokenAccessURL, String tokenCheckURL) {

		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.authorizationURL = authorizationURL;
		this.tokenAccessURL = tokenAccessURL;
		this.tokenCheckURL = tokenCheckURL;

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

	 public static Authenticator toAuthenticator(String myEnumString) {
	        try {
	            return valueOf(myEnumString);
	        } catch (Exception ex) {
	                // For error cases
	            return google;
	        }
	    }

}
