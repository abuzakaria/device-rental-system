package de.tum.os.drs.client.mobile.parsers;

import java.util.Date;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.Device;

public class SingleDeviceParser extends ServerRequestCallback<Device> {

	public SingleDeviceParser(Callback<Device> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Device parseJson(String json) throws JSONException {
		
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
		
		return gson.fromJson(json, Device.class);
	}

}
