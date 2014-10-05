package de.tum.os.drs.client.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	
	public static Gson getGson(){
		
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		
	}

}
