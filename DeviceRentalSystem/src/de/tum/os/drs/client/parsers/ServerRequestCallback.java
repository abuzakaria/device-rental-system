package de.tum.os.drs.client.parsers;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.ServerCommunication;
import de.tum.os.drs.client.mobile.communication.ServerResponse;

public abstract class ServerRequestCallback<T> {

	Callback<T> callback;

	public ServerRequestCallback(Callback<T> callback) {

		this.callback = callback;

	}

	public void onDownloadComplete(ServerResponse response) {

		if (!ServerCommunication.isCodeValid(response.getStatusCode())) {

			callback.onFailure(response.getStatusCode(), response.getErrorMessage());

		} else if(response.getResult() != null) {

			try {

				callback.onSuccess(parseJson(response.getResult()));

			} catch (JSONException e) {

				e.printStackTrace();
				callback.onFailure(HttpURLConnection.HTTP_BAD_METHOD,
						e.getMessage());

			}

		}

	}

	public abstract T parseJson(String json) throws JSONException;

}
