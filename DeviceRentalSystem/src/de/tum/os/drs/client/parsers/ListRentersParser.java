package de.tum.os.drs.client.parsers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.model.Renter;

public class ListRentersParser extends ServerRequestCallback<List<Renter>> {

	public ListRentersParser(Callback<List<Renter>> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Renter> parseJson(String json) throws JSONException {
			
		JSONArray array = new JSONArray(json);
		
		Type listType = new TypeToken<ArrayList<Renter>>() {
		}.getType();
		List<Renter> renters = GsonHelper.getGson().fromJson(array.toString(),
				listType);

		return renters;
	}

}
