package de.tum.os.drs.client.mobile;

import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.DeviceType;

public class EditDeviceFragment extends Fragment {

	private Button updateButton;
	private Device device;

	private EditText deviceSerial, deviceDetails, deviceName;
	private TextView dateTag;
	private Spinner deviceType, deviceState;
	private MainActivity activity;
	private RentalService service;
	private DatePicker returnDate = null;
	private String oldImei;
	private ProgressDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("check", "on createview");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_edit_device,
				container, false);
		getActivity().setTitle("Update device");

		deviceSerial = (EditText) rootView.findViewById(R.id.device_serial);
		deviceName = (EditText) rootView.findViewById(R.id.deviceName);
		deviceDetails = (EditText) rootView.findViewById(R.id.description);
		deviceType = (Spinner) rootView.findViewById(R.id.devicetype);
		deviceState = (Spinner) rootView.findViewById(R.id.devicestate);
		returnDate = (DatePicker) rootView.findViewById(R.id.returnDate);
		updateButton = (Button) rootView.findViewById(R.id.editdevice);

		dateTag = (TextView) rootView.findViewById(R.id.returnDateTex);

		service = RentalServiceImpl.getInstance();
		activity = (MainActivity) getActivity();

		device = activity.selectedDevice;
		oldImei = device.getImei();

		deviceType.setAdapter(new ArrayAdapter<DeviceType>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, DeviceType
						.values()));

		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (deviceSerial.getText().length() == 0) {
					Toast.makeText(getActivity(),
							"Device serial can not be empty",
							Toast.LENGTH_SHORT).show();
				} else {
					editDevice();
				}
			}
		});

		showDeviceInformation();

		return rootView;
	}

	private void showDeviceInformation() {

		if (device != null) {

			deviceSerial.setText(device.getImei());
			deviceName.setText(device.getName());
			deviceType.setSelection(device.getDeviceType().index());
			deviceDetails.setText(device.getDescription());

			if (!device.isAvailable()) {

				Date date = device.getEstimatedReturnDate();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				returnDate.updateDate(calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
				returnDate.setVisibility(View.VISIBLE);
				dateTag.setVisibility(View.VISIBLE);

			}

			if (device.getState() != null) {
				ArrayAdapter<String> adap = (ArrayAdapter<String>) deviceState
						.getAdapter();
				int pos = adap.getPosition(device.getState());
				deviceState.setSelection(pos);
			}

		}

	}

	private void editDevice() {

		final String imei = (deviceSerial.getText().length() == 0) ? null
				: deviceSerial.getText().toString();
		String description = (deviceDetails.getText().length() == 0) ? null
				: deviceDetails.getText().toString();
		String name = (deviceName.getText().length() == 0) ? null : deviceName
				.getText().toString();
		Date date = getDateFromDatePicker(returnDate);

		// Device device = new Device();
		device.setImei(imei);
		device.setDescription(description);
		device.setName(name);
		device.setEstimatedReturnDate(date);

		String state = deviceState.getSelectedItem().toString();
		device.setState(state);

		dialog = ProgressDialog.show(activity, "Please wait ...",
				"Logging in..", true);
		dialog.setCancelable(false);

		service.updateDevice(oldImei, device, new Callback<String>() {

			@Override
			public void onSuccess(String result) {
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
						.show();
				dialog.dismiss();
				activity.updateDevices(imei, AfterDeviceUpdateAction.OPEN_DEVICE);

			}

			@Override
			public void onFailure(int code, String error) {
				dialog.dismiss();

				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

				if (code == 401) {
					((MainActivity) getActivity()).sessionExpired();
				}

			}
		});
	}

	private Date getDateFromDatePicker(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

}
