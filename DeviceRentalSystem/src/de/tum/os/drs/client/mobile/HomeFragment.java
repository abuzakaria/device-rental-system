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
import android.widget.TextView;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		getActivity().setTitle("Home");
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		final Button button1 = (Button) rootView.findViewById(R.id.editdevice);
		final TextView txt1 = (TextView)rootView.findViewById(R.id.txtLabel);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				button1.setEnabled(false);
				RentalService service = RentalServiceImpl.getInstance();
		
				List<String> imeis = new ArrayList<String>();
				
				//imeis.add("600");
				//imeis.add("6.548201885173421E7");
				
				/*
				service.returnDevices(new ReturnRequest("1111111", imeis, "bla bla bla", "signature"), new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						button1.setEnabled(true);
						Log.d("webservice",result);
						txt1.setText(result);
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						button1.setEnabled(true);
						Log.d("webservice", error);
						txt1.setText(String.valueOf(code)+error);
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
				
				
			/*	Device device = new Device("7777", "whatever device", "tes7e32 description","very old", DeviceType.DesktopPC, new Date(), false);
		
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
					
		service.getDeviceByImei("500", new Callback<Device>(){

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
