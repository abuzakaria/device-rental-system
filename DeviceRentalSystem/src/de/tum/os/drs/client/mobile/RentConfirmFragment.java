package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.RentRequest;
import de.tum.os.drs.client.mobile.model.Renter;

public class RentConfirmFragment extends Fragment {

	private RentalService service;
	
	private TextView deviceDetails;
	private TextView renterDetails;
	private DatePicker datePicker;
	private Button rent;
	private EditText comments;

	private MainActivity activity;

	private String signature;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.fragment_rent_confirm,
				container, false);
		getActivity().setTitle("Confirm Renting");

		deviceDetails = (TextView) rootView.findViewById(R.id.device_details);
		renterDetails = (TextView) rootView.findViewById(R.id.renter_details);
		datePicker = (DatePicker) rootView
				.findViewById(R.id.confirm_date_picker);
		comments = (EditText) rootView.findViewById(R.id.rent_comments);

		activity = (MainActivity) getActivity();
		service = RentalServiceImpl.getInstance();

		signature = getArguments().getString("signature");

		rent = (Button) rootView.findViewById(R.id.rent_conf_btn);
		rent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rentDevice();
			}

		});

		showInformation();

		return rootView;
	}

	/**
	 * Sends the rent request to the server
	 * 
	 */
	private void rentDevice() {

		if (signature == null || activity.selectedRenter == null
				|| activity.selectedDevice == null) {

			return;

		}

		final Renter renter = activity.selectedRenter;
		final Device device = activity.selectedDevice;

		List<String> devices = new ArrayList<String>();
		devices.add(device.getImei());

		Date returnDate = getDateFromDatePicker(datePicker);

		//Create a rent request
		RentRequest request = new RentRequest(renter.getMatriculationNumber(),
				devices, returnDate, comments.getText().toString(), signature);

		activity.showLoadingDialog("Issuing rent request");
		service.rentDevices(request, new Callback<String>() {

			@Override
			public void onSuccess(String result) {
				
				activity.hideLoadingDialog();
				activity.showToast(result);
				
				//Update the device list
				activity.updateSingleDevice(device.getImei(), renter.getMatriculationNumber(), AfterDeviceUpdateAction.UPDATE_RENTER);

			}

			@Override
			public void onFailure(int code, String error) {
				activity.hideLoadingDialog();
				activity.showToast(error);
				
				if(code == 401 || code == 403){
					
					//Invalid session
					//We need to login again
					activity.sessionExpired();
					
				}

			}

		});

	}

	/**
	 * Shows the rental transaction information
	 * 
	 */
	private void showInformation() {

		showDeviceInformation();
		showRenterInformation();
		setDatePicker();
	}

	/**
	 * Sets the date picker 6 months in advance
	 * 
	 */
	private void setDatePicker() {

		Date date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 6);

		datePicker.updateDate(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

	}

	/**
	 * Fills up the information of the renter
	 * 
	 */
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

	/**
	 * Fills up the information of the device
	 * 
	 */
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

	private Date getDateFromDatePicker(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

}
