package de.tum.os.drs.client.mobile.communication;

public interface Callback<T> {

	void onSuccess(T result);
	
	void onFailure(int code, String error);
	
}
