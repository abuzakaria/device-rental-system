package de.tum.os.drs.client.mobile.communication;

/**
 * Custom callback for issuing server requests
 * Needs to be provided by each request
 * 
 * @author pablo
 *
 * @param <T>
 * 	Specified the return type of the server operation
 */
public interface Callback<T> {

	void onSuccess(T result);
	
	void onFailure(int code, String error);
	
}
