package de.tum.os.drs.client.parsers;

import org.json.JSONException;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.Device;

public class SingleDeviceParser extends ServerRequestCallback<Device> {

	public SingleDeviceParser(Callback<Device> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Device parseJson(String json) throws JSONException {
		
		return GsonHelper.getGson().fromJson(json, Device.class);
	}

}
