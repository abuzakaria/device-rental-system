package de.tum.os.drs.client.parsers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.Device;

public class ListDevicesParser extends ServerRequestCallback<List<Device>> {

	public ListDevicesParser(Callback<List<Device>> callback) {
		super(callback);

	}

	@Override
	public List<Device> parseJson(String json) throws JSONException {
		
		JSONArray array = new JSONArray(json);
		
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
		
		Type listType = new TypeToken<ArrayList<Device>>() {
		}.getType();
		List<Device> devices = gson.fromJson(array.toString(),
				listType);

		return devices;

	}

}
