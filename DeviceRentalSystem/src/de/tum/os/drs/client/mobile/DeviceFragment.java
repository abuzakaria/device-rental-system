package de.tum.os.drs.client.mobile;

import java.util.List;

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
import de.tum.os.drs.client.mobile.model.AfterSignatureAction;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.Renter;

public class DeviceFragment extends Fragment {

	private MainActivity activity;

	private Button updateButton;
	private Button rentButton;
	private Button returnButton;
	private ImageView deviceImage;
	private TextView devicedump;
	private TextView renterDump;

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
		renterDump = (TextView) rootView.findViewById(R.id.renterDumpDevice);
		updateButton = (Button) rootView.findViewById(R.id.editDevice);
		deviceImage = (ImageView) rootView.findViewById(R.id.deviceImage);
		service = RentalServiceImpl.getInstance();
		activity = (MainActivity) getActivity();

		getActivity().setTitle("Device details");

		device = activity.selectedDevice;

		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Start the edit fragment
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

				// Start the renter selection fragment
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new RenterSelectionFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		returnButton = (Button) rootView.findViewById(R.id.returnDevice);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Start the signature fragment

				SignatureFragment f = new SignatureFragment();

				// Open the return confirmation after the signature has been
				// fetched
				Bundle b = new Bundle();
				b.putString("action",
						AfterSignatureAction.OPEN_RETURN_CONFIRM.toString());
				f.setArguments(b);

				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, f);
				transaction.addToBackStack(null);
				transaction.commit();

			}
		});

		if (device.isAvailable()) {
			rentButton.setVisibility(View.VISIBLE);
		} else {

			// Show renter if device is rented
			returnButton.setVisibility(View.VISIBLE);
			renterDump.setVisibility(View.VISIBLE);
		}

		showDeviceDetails();

		return rootView;
	}

	/**
	 * Fills up the TextViews with the device and renter details
	 * 
	 */
	private void showDeviceDetails() {

		if (device != null) {

			fillDeviceTextDetails();

			deviceImage.setImageResource(activity.getDeviceImage(device
					.getName()));

			if (!device.isAvailable()) {

				if (activity.getRenters() == null) {

					//The renters gaven't been fetched yet
					 
					activity.showLoadingDialog("Loading renter information");
					service.getAllRenters(new Callback<List<Renter>>() {

						@Override
						public void onSuccess(List<Renter> result) {
							activity.hideLoadingDialog();
							activity.setRenters(result);
							
							//Fills up the text view with the information of the renter
							showRenterDetails();

						}

						@Override
						public void onFailure(int code, String error) {
							activity.hideLoadingDialog();

						}

					});

				} else {
					
					//Renters are there, we can show the information right away

					showRenterDetails();
				}

			}

		}
	}

	/**
	 * Returns a renter from a device Imei
	 * 
	 * @param imei
	 * @return
	 * 	
	 */
	private Renter getRenterFromDeviceImei(String imei) {

		for(Renter r : activity.getRenters()) {
			
			//Log.i("Test", r.getName());
			//Log.i("Test", r.getRentedDevices().toString());
			for(String d : r.getRentedDevices()) {

				//Log.i("Test", d + " " + imei);
				
				if (d.trim().equals(imei.trim())) {
					// Used by confirmation fragment
					activity.selectedRenter = r;
					Log.i("Test", activity.selectedRenter.getName());
					return r;
				}
			}
		}

		activity.selectedRenter = null;
		return null;
	}

	/**
	 * Sets the device information
	 * 
	 * 
	 */
	private void fillDeviceTextDetails() {
		
		if(device == null){
			
			return;
		}
			
		String temp = "Name: ";
		temp += device.getName() == null ? "<None>" : device.getName();
		temp += "\n";

		temp += "IMEI: ";
		temp += device.getImei() == null ? "<None>" : device.getImei();
		temp += "\n";

		temp += "Description: \n";
		temp += device.getDescription() == null ? "<None>" : device
				.getDescription();
		temp += "\n\n";

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

		devicedump.setText(temp);
	}

	/**
	 * Fills the renter details
	 * 
	 */
	private void showRenterDetails() {

		String temp = "";

		Renter renter = getRenterFromDeviceImei(device.getImei());
		
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
			
		} else {

			temp += "Renter not found";
		}

		renterDump.setText(temp);

	}
}
