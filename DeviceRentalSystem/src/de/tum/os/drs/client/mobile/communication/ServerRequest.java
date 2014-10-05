package de.tum.os.drs.client.mobile.communication;

import java.net.URL;

public class ServerRequest {

	public enum HTTP_METHODS {
		GET, POST
	}

	private URL url;
	private HTTP_METHODS method;
	private int sessionId;
	
	//Optional, only if data is posted to the server
	private String json;

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public ServerRequest(URL url, HTTP_METHODS method) {

		this.url = url;
		this.method = method;
		
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public HTTP_METHODS getMethod() {
		return method;
	}

	public void setMethod(HTTP_METHODS method) {
		this.method = method;
	}
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
