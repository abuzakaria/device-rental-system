package de.tum.os.drs.client.mobile.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import de.tum.os.drs.client.mobile.communication.Callback;

public class GenericPOSTMethodParser extends ServerRequestCallback<String> {

	public GenericPOSTMethodParser(Callback<String> callback) {
		super(callback);
	}

	@Override
	public String parseJson(String json) throws JSONException {
		
		JSONObject obj = new JSONObject(json);
		return obj.getString("message");
	}

	
}
