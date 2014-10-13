package de.tum.os.drs.client.mobile;

import java.util.List;

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
import de.tum.os.drs.client.mobile.model.Renter;


public class DeviceFragment extends Fragment{
	private String imei;
	private MainActivity _main;
	private Button updatebtn;
	public DeviceFragment(){}
	private TextView devicedump;
	private Renter m_renter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("check","devicefragment: on createview");
		super.onCreateView(inflater, container, savedInstanceState);		
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        devicedump = ((TextView)rootView.findViewById(R.id.devicedump));
		updatebtn = (Button) rootView.findViewById(R.id.editDevice);
        getActivity().setTitle("Device details");
        
        return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

        _main = (MainActivity)getActivity();
		imei = _main.mSelectedDeviceImei; 
		updatebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_main.mSelectedDeviceImei = imei;
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new EditDeviceFragment(), "NewFragmentTag"); 
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});
		
		
        RentalService service = RentalServiceImpl.getInstance();
        service.getAllActiveRenters(new Callback<List<Renter>>(){
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<Renter> result) {
				// TODO Auto-generated method stub
				for (Renter r : result)
				{
					for(String d : r.getRentedDevices())
					{
						if (d.trim().equals(imei))
						{
							m_renter = r;
							break;
						}
					}
				}
			}
        	
        });
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
					temp += "Renter: ";
					temp += m_renter.getName();
					temp += "\n";
					
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
	}

}
