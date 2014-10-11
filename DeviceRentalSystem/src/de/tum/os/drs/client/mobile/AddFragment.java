package de.tum.os.drs.client.mobile;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.DeviceType;

public class AddFragment extends Fragment {
    Button scan_btn, add_btn;
    String scanresult, s_deviceName, s_deviceDesc, s_deviceSerial;
    EditText deviceSerial,deviceDetails, deviceName;
    Spinner deviceType, deviceState;
	public AddFragment(){}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("check","on createview");
		super.onCreateView(inflater, container, savedInstanceState);		
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        scan_btn = ((Button)rootView.findViewById(R.id.scan));
        deviceSerial = ((EditText)rootView.findViewById(R.id.device_serial));
        deviceName = ((EditText)rootView.findViewById(R.id.deviceName));
        deviceDetails = ((EditText)rootView.findViewById(R.id.description));
        deviceType = ((Spinner)rootView.findViewById(R.id.devicetype));
        deviceState = ((Spinner)rootView.findViewById(R.id.devicestate));
        add_btn = ((Button)rootView.findViewById(R.id.adddevice));
        
        deviceSerial.setText("");
        deviceType.setAdapter(new ArrayAdapter<DeviceType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DeviceType.values()));
        
        
        scan_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new ScanFragment(), "NewFragmentTag"); 
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});
        
        add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (deviceSerial.getText().length() == 0)
					Toast.makeText(getActivity(), "Device serial can not be empty", Toast.LENGTH_SHORT).show();
				else
					addDevice();
			}

			private void addDevice() {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), deviceSerial.getText() , Toast.LENGTH_SHORT).show();
				
				s_deviceSerial = (deviceSerial.getText().length() == 0) ? null : deviceSerial.getText().toString();
				s_deviceDesc = (deviceDetails.getText().length() == 0) ? null : deviceDetails.getText().toString();
				s_deviceName = (deviceName.getText().length() == 0) ? null : deviceName.getText().toString();
				Device device = new Device(s_deviceSerial, s_deviceName, s_deviceDesc, deviceState.getSelectedItem().toString(), DeviceType.valueOf(DeviceType.class, deviceType.getSelectedItem().toString()), null, true);
				
				RentalService service = RentalServiceImpl.getInstance();
				service.addDevice(device, new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						Log.d("adddevice", "success");
						Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						Log.d("adddevice", "failure");
						Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
						
					}
				});
			}
		});
        
        return rootView;
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		scanresult = ((MainActivity)getActivity()).mScanResult;
        if (scanresult != null)   
        {
        	((MainActivity)getActivity()).mScanResult = null;
        	Log.d("scan", scanresult);
        	deviceSerial.setText(scanresult);
        	
        }
	}
}
