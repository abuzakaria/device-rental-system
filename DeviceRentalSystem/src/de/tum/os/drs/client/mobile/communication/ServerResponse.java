package de.tum.os.drs.client.mobile.communication;

import org.json.JSONObject;

public class ServerResponse {

	private int statusCode;
	private String result;
	private String errorMessage;

	public ServerResponse(){
		
		
	}
	
	public ServerResponse(int statusCode, String result) {

		this.statusCode = statusCode;
		this.result = result;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResult() {
		return result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	

}
