package de.tum.os.drs.client.mobile;

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
				
				Device device = new Device("88888", "ynasy device", "tested description","very old", DeviceType.Other, new Date(), false);
		
				/*service.addDevice(device, new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						
					}
					
					
					
					
					
				});*/
				
				/*
				service.addRenter(new Renter("Test1", "72162", "test@test.com",
						"173627322", "some comments bla bla", null), new Callback<String>(){

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
				/*
				service.getAllDevices(new Callback<List<Device>>(){

					@Override
					public void onSuccess(List<Device> result) {
						for(Device d: result){
							
							Log.i("Device", d.getEstimatedReturnDate().toLocaleString());
							
						}
						
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						
					}
					
					
					
					
				});
			
				*/
				
			}

		});

		return rootView;
	}

}
