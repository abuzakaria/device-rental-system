package de.tum.os.drs.client.mobile.communication;

import java.util.List;

import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.RentRequest;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;

public interface RentalService {
	
	//---------------Devices-----------------
	void addDevice(Device device, Callback<String> callback);
	
	void getAllDevices(Callback<List<Device>> callback);
	
	void getAvailableDevices(Callback<List<Device>> callback);
	
	void getRentedDevices(Callback<List<Device>> callback);
	
	void updateDevice(Device device, Callback<String> callback);
	
	void getDeviceByImei(String Imei, Callback<Device> callback);
	
	//TODO needed?
	//void deleteDevice(Device device);
	

	//-------------Renters------------------
	
	void addRenter(Renter renter, Callback<String> callBack);
	
	void updateRenter(Renter renter, Callback<String> callBack);
	
	void getAllRenters(Callback<List<Renter>> callBack);
	
	void getAllActiveRenters(Callback<List<Renter>> callBack);
	
	void getRenter(Callback<Renter> callBack);
	
	//TODO needed?
	//void deleteRenter();
	
	//------------Renting Service-------------
	
	void rentDevices(RentRequest request, Callback<String> callBack);
	
	void returnDevices(ReturnRequest request, Callback<String> callBack);
	
	//-----------Event service---------------
	
	//TODO
	//void fetchEvents();
 
	
}
