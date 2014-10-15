package de.tum.os.drs.client.mobile;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;

public class AvailableDevicesFragment extends Fragment{

	private static final String TAG = "AvailableDevices";

	private ListView list;
	private MainActivity activity;
	private RentalService service;
	private ProgressDialog dialog;

	private EditText inputSearch;
	private ArrayAdapter<Device> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.available_devices_tab,
				container, false);

		list = (ListView) rootView.findViewById(R.id.available_devices_list);
		inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		activity = (MainActivity) getActivity();
		service = RentalServiceImpl.getInstance();

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final Device item = (Device) parent.getItemAtPosition(position);
				activity.selectedDevice = item;

				activity.startDeviceFragment();
				/*
				//Start the device fragment
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_container, new DeviceFragment());
				transaction.addToBackStack(null);
				transaction.commit();
*/
			}

		});

		populateList();

		return rootView;

	}

	private void populateList() {

		// dialog = ProgressDialog.show(activity, "Please wait ...",
		// "Loading devices..", true);
		// dialog.setCancelable(false);

		service.getAvailableDevices(new Callback<List<Device>>() {

			@Override
			public void onSuccess(List<Device> result) {
				adapter = new DeviceListAdapter(activity, result);
				list.setAdapter(adapter);
				// dialog.dismiss();

			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				// dialog.dismiss();

			}

		});
	}

	
}
