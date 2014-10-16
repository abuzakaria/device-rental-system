package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.Renter;

public class ReturnConfirmFragment extends Fragment {

	private MainActivity activity;

	private RentalService service;
	private TextView deviceDetails;
	private TextView renterDetails;
	private Button returnB;
	private EditText comments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.fragment_return_confirm, container,
				false);
		
		activity = (MainActivity) getActivity();
		
		deviceDetails = (TextView) rootView.findViewById(R.id.device_details_r);
		renterDetails = (TextView) rootView.findViewById(R.id.renter_details_r);
		comments = (EditText) rootView.findViewById(R.id.return_comments);

		activity = (MainActivity) getActivity();
		service = RentalServiceImpl.getInstance();

		returnB = (Button) rootView.findViewById(R.id.return_conf_btn);
		returnB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				returnDevice();

			}

		});
		
		showInformation();
		
		return rootView;

	}
	
	private void returnDevice(){
		//getActivity().getFragmentManager().popBackStack("HomeInitial", 0);
		//returnToHome();
		activity.returnToHome();
		
	}
	
	private void showInformation(){
		
		showDeviceInformation();
		showRenterInformation();
		
	}
	

	private void showRenterInformation() {

		if (activity.selectedRenter == null) {
			return;
		}

		Renter renter = activity.selectedRenter;

		String temp = "Name: ";
		temp += renter.getName() == null ? "<None>" : renter.getName();
		temp += "\n";

		temp += "MatriculationNumber: ";
		temp += renter.getMatriculationNumber() == null ? "<None>" : renter
				.getMatriculationNumber();
		temp += "\n";

		temp += "Email: ";
		temp += renter.getEmail() == null ? "<None>" : renter.getEmail();
		temp += "\n";

		temp += "Phone Number: ";
		temp += renter.getPhoneNumber() == null ? "<None>" : renter
				.getPhoneNumber();
		temp += "\n";

		temp += "Comments: \n";
		temp += renter.getComments() == null ? "<None>" : renter.getComments();
		temp += "\n";

		renterDetails.setText(temp);
	}

	private void showDeviceInformation() {

		if (activity.selectedDevice == null) {

			return;
		}

		Device device = activity.selectedDevice;

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

		deviceDetails.setText(temp);
	}
	
}
