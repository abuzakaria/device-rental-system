package de.tum.os.drs.client.mobile.communication;

import java.util.List;

import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.LoginRequest;
import de.tum.os.drs.client.mobile.model.LoginResponse;
import de.tum.os.drs.client.mobile.model.RentRequest;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;

public interface RentalService {
	
	//--------------Session------------------
	
	void login(LoginRequest request, Callback<LoginResponse> callback);
	void logout(Callback<String> callback);
	
	//---------------Devices-----------------
	void addDevice(Device device, Callback<String> callback);
	
	void getAllDevices(Callback<List<Device>> callback);
	
	void getAvailableDevices(Callback<List<Device>> callback);
	
	void getRentedDevices(Callback<List<Device>> callback);
	
	void updateDevice(String IMEI, Device device, Callback<String> callback);
	
	void getDeviceByImei(String Imei, Callback<Device> callback);
	
	//-------------Renters------------------
	
	void addRenter(Renter renter, Callback<String> callBack);
	
	void updateRenter(String mtrNr, Renter renter, Callback<String> callBack);
	
	void getAllRenters(Callback<List<Renter>> callBack);
	
	void getAllActiveRenters(Callback<List<Renter>> callBack);
	
	//------------Renting Service-------------
	
	void rentDevices(RentRequest request, Callback<String> callBack);
	
	void returnDevices(ReturnRequest request, Callback<String> callBack);
	
	
}
