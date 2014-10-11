package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;


public class DeviceFragment extends Fragment{
	private String imei;
	MainActivity _main;
	public DeviceFragment(){}
	TextView devicedump;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("check","devicefragment: on createview");
		super.onCreateView(inflater, container, savedInstanceState);		
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        devicedump = ((TextView)rootView.findViewById(R.id.devicedump));
        getActivity().setTitle("Device details");
        
        _main = (MainActivity)getActivity();
		imei = _main.mSelectedDeviceImei; 
		_main.mSelectedDeviceImei = null;
		Button updatebtn = (Button) rootView.findViewById(R.id.editDevice);
		updatebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_main.mSelectedDeviceImei = imei;
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new EditDeviceFragment(), "NewFragmentTag"); 
				ft.commit(); 
			}
		});
		
		
        RentalService service = RentalServiceImpl.getInstance();
		service.getDeviceByImei(imei, new Callback<Device>(){

			@Override
			public void onSuccess(Device result) {				
				Log.i("Device", result.getDeviceType().toString());
				displayDetails(result);
			}

			private void displayDetails(Device d) {
				// TODO Auto-generated method stub
				String temp = "Name: ";
				temp += d.getName()== null ? "<None>": d.getName(); 
				temp += "\n";
				
				temp += "IMEI: ";
				temp += d.getImei()== null ? "<None>": d.getImei();  
				temp += "\n";
				
				temp += "Description: ";
				temp += d.getDescription()==null ? "<None>": d.getDescription();  
				temp += "\n";				
				
				temp += "Type: ";
				temp += d.getDeviceType() == null ? "<None>": d.getDeviceType().toString();  
				temp += "\n";
				
				temp += "State: ";
				temp += d.getState()== null ? "<None>": d.getState();  
				temp += "\n";

				temp += "Availabilty: ";
				temp += d.isAvailable() ? "Available": "Unavailable";  
				temp += "\n";
				
				if (!d.isAvailable())
				{
					temp += "Estimated Return Date: ";
					temp += d.getEstimatedReturnDate()==null ? "<Not found>": d.getEstimatedReturnDate().toString();  
					temp += "\n";
				}
				
				
				devicedump.setText(temp);
			}

			@Override
			public void onFailure(int code, String error) {				
				Log.i("Error", error);
			}
		});
        return rootView;
	}

}
