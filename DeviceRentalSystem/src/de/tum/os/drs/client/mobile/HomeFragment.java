package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.DeviceType;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;

public class HomeFragment extends Fragment {

	private Button button;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(
				((MainActivity) getActivity()).mDrawerTitle);
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		button = (Button) rootView.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				RentalService service = RentalServiceImpl.getInstance();
		
				List<String> imeis = new ArrayList<String>();
				
				imeis.add("600");
				//imeis.add("6.548201885173421E7");
				
				/*
				service.returnDevices(new ReturnRequest("1111111", imeis, "bla bla bla", "signature"), new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				*/
				/*
				service.rentDevices(new RentRequest("1111111", imeis, new Date(), "some comments", "Signature"), new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFailure(int code, String error) {
						Log.i("RentService", error);
						
					}
					
					
					
					
				});
				*/
				/*
				service.getAllRenters(new Callback<List<Renter>>(){

					@Override
					public void onSuccess(List<Renter> result) {
						
						for(Renter r : result){
							
							Log.i("Renters", r.getName());
							
							for(String imei : r.getRentedDevices()){
								Log.i("Test", imei);
							}
							
						}
						
						
					}
*
					@Override
					public void onFailure(int code, String error) {
						Log.i("AllRenters", error);
						
					}
					
					
					
					
				});
				
				
			*/	
				
				
				Device device = new Device("7777", "whatever device", "tes7e32 description","very old", DeviceType.DesktopPC, new Date(), false);
		/*
				service.addDevice(device, new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						
					}
					
					
					
					
					
				});
				*/
				/*
				service.updateRenter("3111111", new Renter("Test renter", "3222222", "test1@test1.com",
						"+49173627322", "canged the comments"), new Callback<String>(){

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onFailure(int code, String error) {
								// TODO Auto-generated method stub
								
							}
					
				});
				
			*/	
	/*		
		service.getDeviceByImei("600", new Callback<Device>(){

			@Override
			public void onSuccess(Device result) {
				
				Log.i("Device", result.getDeviceType().toString());
				Log.i("Device", result.getEstimatedReturnDate().toLocaleString());
				
				
			}

			@Override
			public void onFailure(int code, String error) {
				
				Log.i("Error", error);
				
			}
			
			
		});
		*/		
				/*service.getAllRenters(new Callback<List<Renter>>(){

					@Override
					public void onSuccess(List<Renter> result) {
						
						for(Renter r : result){
							
							Log.i("Renters", r.getName());
						}
						
						
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						
					}
					
					
					
					
				});
*/
				
				service.getAllDevices(new Callback<List<Device>>(){

					@Override
					public void onSuccess(List<Device> result) {
						for(Device d: result){
							
							Log.i("Device", d.getName());
							Log.i("Device", d.getEstimatedReturnDate().toLocaleString());
							
						}
						
					}

					@Override
					public void onFailure(int code, String error) {
						
						Log.i("TAG", code+"");
						Log.i("TAG", error);
						
						
					}
					
					
					
					
				});
			
				
				
			}

		});

		return rootView;
	}

}
