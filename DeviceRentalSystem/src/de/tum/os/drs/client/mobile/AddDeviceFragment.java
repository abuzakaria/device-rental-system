package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.AfterScanAction;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.DeviceType;

public class AddDeviceFragment extends Fragment {

	private Button add_btn, scanButton;
	private String s_deviceName, s_deviceDesc, s_deviceSerial;
	private EditText deviceSerial, deviceDetails, deviceName;
	private Spinner deviceType, deviceState;
	private RentalService service;
	private Boolean isDuplicate = true;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("check", "on createview");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_add_device,
				container, false);
		getActivity().setTitle("Add a device");
		deviceSerial = ((EditText) rootView.findViewById(R.id.device_serial));
		deviceName = ((EditText) rootView.findViewById(R.id.deviceName));
		deviceDetails = ((EditText) rootView.findViewById(R.id.description));
		deviceType = ((Spinner) rootView.findViewById(R.id.devicetype));
		deviceState = ((Spinner) rootView.findViewById(R.id.devicestate));
		add_btn = ((Button) rootView.findViewById(R.id.editdevice));
		scanButton = (Button) rootView.findViewById(R.id.scan_add);
		service = RentalServiceImpl.getInstance();

		deviceSerial.setText("");
		deviceType.setAdapter(new ArrayAdapter<DeviceType>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, DeviceType
						.values()));

		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (deviceSerial.getText().length() == 0) {

					Toast.makeText(getActivity(),
							"Device serial can not be empty",
							Toast.LENGTH_SHORT).show();
				} else {
					addDevice();
				}

			}

		});
		
		scanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MainActivity activity = (MainActivity) getActivity();
				activity.scanAction = AfterScanAction.SET_IMEI_FILED;
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new ScanFragment()); 
				ft.addToBackStack(null);
				ft.commit(); 
			}

		});

		return rootView;
	}

	private void addDevice() {

		s_deviceSerial = (deviceSerial.getText().length() == 0) ? null
				: deviceSerial.getText().toString();
		s_deviceDesc = (deviceDetails.getText().length() == 0) ? null
				: deviceDetails.getText().toString();
		s_deviceName = (deviceName.getText().length() == 0) ? null : deviceName
				.getText().toString();

		// Create device object with the provided parameters
		Device device = new Device(s_deviceSerial, s_deviceName, s_deviceDesc,
				deviceState.getSelectedItem().toString(), DeviceType.valueOf(
						DeviceType.class, deviceType.getSelectedItem()
								.toString()), null, true);

		// Send the new device to the server
		RentalService service = RentalServiceImpl.getInstance();
		service.addDevice(device, new Callback<String>() {

			@Override
			public void onSuccess(String result) {

				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
						.show();

				onFinished();

			}

			@Override
			public void onFailure(int code, String error) {

				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

				if (code == 401) {
					((MainActivity) getActivity()).sessionExpired();
				}

			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		String result = ((MainActivity) getActivity()).scanResult;
		
        if (result != null)   
        {
        	((MainActivity) getActivity()).scanResult = null;
        	deviceSerial.setText(result);
        	
        }
	}
	
	
	private void onFinished() {
		MainActivity activity = (MainActivity) getActivity();
		activity.newDeviceImei = s_deviceSerial;
		activity.updateAction = AfterDeviceUpdateAction.OPEN_DEVICE;
		activity.updateDevices();
		
	}

}
