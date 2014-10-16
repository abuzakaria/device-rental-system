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
	private Button returnButton;
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
		deviceImage = (ImageView) rootView.findViewById(R.id.deviceImage);
		service = RentalServiceImpl.getInstance();
		activity = (MainActivity) getActivity();

		getActivity().setTitle("Device details");

		device = activity.selectedDevice;

		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new EditDeviceFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		rentButton = (Button) rootView.findViewById(R.id.rentDevice);
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

		returnButton = (Button) rootView.findViewById(R.id.returnDevice);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				activity.rentingSignature = false;
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new SignatureFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		if (device.isAvailable()) {
			rentButton.setVisibility(View.VISIBLE);
		} else {
			returnButton.setVisibility(View.VISIBLE);
		}
		
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
							Log.i("Test", "Renter found");
							activity.selectedRenter = r;
							break;
						}
					}
				}
			}

		});

	}

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
