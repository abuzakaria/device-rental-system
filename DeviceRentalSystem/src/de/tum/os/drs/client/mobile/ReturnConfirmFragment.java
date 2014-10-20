package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;

public class ReturnConfirmFragment extends Fragment {

	private MainActivity activity;

	private RentalService service;
	private TextView deviceDetails;
	private TextView renterDetails;
	private Button returnB;
	private EditText comments;
	private Renter renter;
	private Device device;
	
	private String signature;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.fragment_return_confirm, container,
				false);
		
		activity = (MainActivity) getActivity();
		getActivity().setTitle("Confirm returning");
		
		deviceDetails = (TextView) rootView.findViewById(R.id.device_details_r);
		renterDetails = (TextView) rootView.findViewById(R.id.renter_details_r);
		comments = (EditText) rootView.findViewById(R.id.return_comments);

		activity = (MainActivity) getActivity();
		renter = activity.selectedRenter;
		device = activity.selectedDevice;
		service = RentalServiceImpl.getInstance();
		
		signature = getArguments().getString("signature");
		
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
	
	/**
	 * Sends the return message to the server
	 * 
	 */
	private void returnDevice(){
		
		List<String> devices = new ArrayList<String>();
		devices.add(device.getImei());
		ReturnRequest request = new ReturnRequest(renter.getMatriculationNumber(), devices, comments.getText().toString(), signature);
		
		activity.showLoadingDialog("Returning devices");
		service.returnDevices(request, new Callback<String>(){

			@Override
			public void onSuccess(String result) {
				activity.hideLoadingDialog();
				activity.showToast(result);
				
				//Update the device list
				activity.updateDevices(null, AfterDeviceUpdateAction.GO_TO_HOME);
				
			}

			@Override
			public void onFailure(int code, String error) {
				activity.hideLoadingDialog();
				activity.showToast(error);
				
				
			}
			
			
		});		
	
		
		
	}
	
	
	private void showInformation(){
		
		showDeviceInformation();
		showRenterInformation();
		
	}
	
	private void showRenterInformation() {

		if (renter == null) {
			return;
		}

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

		if (device == null) {

			return;
		}

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
