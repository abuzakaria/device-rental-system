package de.tum.os.drs.client.mobile.parsers;

import org.json.JSONException;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.Renter;

public class SingleRenterParser extends ServerRequestCallback<Renter> {

	public SingleRenterParser(Callback<Renter> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Renter parseJson(String json) throws JSONException {

		return GsonHelper.getGson().fromJson(json, Renter.class);

	}

}
