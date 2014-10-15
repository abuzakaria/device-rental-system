package de.tum.os.drs.client.mobile;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.Renter;

public class DeviceFragment extends Fragment {
	
	private MainActivity activity;
	private Button updateButton;
	private Button rentButton;
	private ImageView deviceImage;
	private TextView devicedump;
	private Renter renter;
	private Device device;
	private RentalService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_device, container,
				false);
		
		devicedump = (TextView) rootView.findViewById(R.id.deviceDump);
		updateButton = (Button) rootView.findViewById(R.id.editDevice);
		rentButton = (Button) rootView.findViewById(R.id.rentDevice);
		deviceImage = (ImageView) rootView.findViewById(R.id.deviceImage);
		service = RentalServiceImpl.getInstance();
		activity = (MainActivity) getActivity();

		getActivity().setTitle("Device details");
		
		device = activity.selectedDevice;
		
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// _main.mSelectedDeviceImei = imei;
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new EditDeviceFragment(),
						"NewFragmentTag");
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		
		rentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new RenterSelectionFragment(),
						"NewFragmentTag");
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		showDeviceDetails();
		
		return rootView;
	}

	private void showDeviceDetails() {

		if (device != null) {

			if (!device.isAvailable()) {

				setRenter();
			}

			// TODO set image

			fillDeviceTextDetails();

		} else {

		}
		
	}

	private void setRenter() {

		service.getAllActiveRenters(new Callback<List<Renter>>() {
			@Override
			public void onFailure(int code, String error) {
				

			}

			@Override
			public void onSuccess(List<Renter> result) {
				for (Renter r : result) {
					for (String d : r.getRentedDevices()) {
						if (d.trim().equals(device.getImei())) {
							renter = r;
							break;
						}
					}
				}
			}

		});

	}
/*
	@Override
	public void onResume() {
		super.onResume();

		_main = (MainActivity) getActivity();
		// imei = _main.mSelectedDeviceImei;
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_main.mSelectedDeviceImei = imei;
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new EditDeviceFragment(),
						"NewFragmentTag");
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		RentalService service = RentalServiceImpl.getInstance();
		service.getAllActiveRenters(new Callback<List<Renter>>() {
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<Renter> result) {
				// TODO Auto-generated method stub
				for (Renter r : result) {
					for (String d : r.getRentedDevices()) {
						if (d.trim().equals(imei)) {
							renter = r;
							break;
						}
					}
				}
			}

		});
		service.getDeviceByImei(imei, new Callback<Device>() {

			@Override
			public void onSuccess(Device result) {
				Log.i("Device", result.getDeviceType().toString());
				// displayDetails(result);
			}

			@Override
			public void onFailure(int code, String error) {
				Log.i("Error", error);
			}
		});
	}
*/
	private void fillDeviceTextDetails() {
		String temp = "Name: ";
		temp += device.getName() == null ? "<None>" : device.getName();
		temp += "\n";

		temp += "IMEI: ";
		temp += device.getImei() == null ? "<None>" : device.getImei();
		temp += "\n";

		temp += "Description: ";
		temp += device.getDescription() == null ? "<None>" : device
				.getDescription();
		temp += "\n";

		temp += "Type: ";
		temp += device.getDeviceType() == null ? "<None>" : device
				.getDeviceType().toString();
		temp += "\n";

		temp += "State: ";
		temp += device.getState() == null ? "<None>" : device.getState();
		temp += "\n";

		temp += "Availabilty: ";
		temp += device.isAvailable() ? "Available" : "Unavailable";
		temp += "\n";

		if (renter != null) {
			temp += "Renter: ";
			temp += renter.getName();
			temp += "\n";
			
			temp += "Matriculation Number: ";
			temp += renter.getMatriculationNumber();
			temp += "\n";

			temp += "Estimated Return Date: ";
			temp += device.getEstimatedReturnDate() == null ? "<Not found>"
					: device.getEstimatedReturnDate().toString();
			temp += "\n";
		}

		devicedump.setText(temp);
	}

}
