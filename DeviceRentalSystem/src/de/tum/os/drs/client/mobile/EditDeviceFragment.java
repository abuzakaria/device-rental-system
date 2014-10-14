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

public class EditDeviceFragment extends Fragment{

	private Button scan_btn, update_btn;
	private String scanresult, s_deviceName, s_deviceDesc, s_deviceSerial;
	private EditText deviceSerial,deviceDetails, deviceName;
	private Spinner deviceType, deviceState;
	private MainActivity _main;
	private RentalService service;
	private boolean isAvailable=true;
	private Date returnDate = null;
	
	public EditDeviceFragment(){}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("check","on createview");
		super.onCreateView(inflater, container, savedInstanceState);		
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        getActivity().setTitle("Update device");
        scan_btn = ((Button)rootView.findViewById(R.id.scan));
        deviceSerial = ((EditText)rootView.findViewById(R.id.device_serial));
        deviceName = ((EditText)rootView.findViewById(R.id.deviceName));
        deviceDetails = ((EditText)rootView.findViewById(R.id.description));
        deviceType = ((Spinner)rootView.findViewById(R.id.devicetype));
        deviceState = ((Spinner)rootView.findViewById(R.id.devicestate));
        update_btn = ((Button)rootView.findViewById(R.id.editdevice));
        scan_btn.setVisibility(View.GONE);
        deviceSerial.setKeyListener(null);
		
        _main = (MainActivity)getActivity();
        s_deviceSerial = _main.mSelectedDeviceImei;
        deviceType.setAdapter(new ArrayAdapter<DeviceType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DeviceType.values()));
        update_btn.setText("Update");
        
        service = RentalServiceImpl.getInstance();
		service.getDeviceByImei(s_deviceSerial, new Callback<Device>(){

			@Override
			public void onSuccess(Device result) {
				// TODO Auto-generated method stub
				if (result.getDescription()!=null)
					deviceDetails.setText(result.getDescription());
				if (result.getDeviceType()!=null)
					deviceType.setSelection(result.getDeviceType().index());
				if (result.getImei()!=null)
					deviceSerial.setText(result.getImei());
				if (result.getName()!=null)
					deviceName.setText(result.getName());
				isAvailable = result.isAvailable();
				returnDate = result.getEstimatedReturnDate();
				if (result.getState()!=null)
				{
					ArrayAdapter<String> adap = (ArrayAdapter<String>) deviceState.getAdapter();
					int pos =adap.getPosition(result.getState());
					deviceState.setSelection(pos);
				}
					
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				Log.i("Error", error);
			}
			
		});
        
        
        
		update_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (deviceSerial.getText().length() == 0)
					Toast.makeText(getActivity(), "Device serial can not be empty", Toast.LENGTH_SHORT).show();
				else
					editDevice();
			}

			private void editDevice() {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), deviceSerial.getText() , Toast.LENGTH_SHORT).show();
				
				s_deviceSerial = (deviceSerial.getText().length() == 0) ? null : deviceSerial.getText().toString();
				s_deviceDesc = (deviceDetails.getText().length() == 0) ? null : deviceDetails.getText().toString();
				s_deviceName = (deviceName.getText().length() == 0) ? null : deviceName.getText().toString();
				Device device = new Device(s_deviceSerial, s_deviceName, s_deviceDesc, deviceState.getSelectedItem().toString(), DeviceType.valueOf(DeviceType.class, deviceType.getSelectedItem().toString()), returnDate, isAvailable);
				
				service = RentalServiceImpl.getInstance();
				service.updateDevice(s_deviceSerial, device, new Callback<String>(){

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						Log.d("adddevice", "success");
						Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
						MainActivity temp = (MainActivity)getActivity();
						temp.mSelectedDeviceImei = s_deviceSerial; 
						getFragmentManager().popBackStack();
						final FragmentTransaction ft2 = getFragmentManager().beginTransaction();						
						ft2.replace(R.id.frame_container, new DeviceFragment(), "NewFragmentTag"); 
						ft2.commit(); 
					}

					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						Log.d("adddevice", "failure");
						Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

						if (code == 401) {
							((MainActivity) getActivity()).sessionExpired();
						}

					}
				});
			}
		});
        
        return rootView;
    }
	
	
}
