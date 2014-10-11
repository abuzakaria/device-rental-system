package de.tum.os.drs.client.parsers;

import org.json.JSONException;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.LoginResponse;

public class LoginResponseParser extends ServerRequestCallback<LoginResponse>{

	public LoginResponseParser(Callback<LoginResponse> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LoginResponse parseJson(String json) throws JSONException {
		
		return GsonHelper.getGson().fromJson(json, LoginResponse.class);
		
	}
	
	

}
