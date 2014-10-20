package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import de.tum.os.drs.client.mobile.adapters.DeviceListAdapter;
import de.tum.os.drs.client.mobile.model.Device;

public class AvailableDevicesFragment extends Fragment {

	private static final String TAG = "AvailableDevices";

	private ListView list;
	private MainActivity activity;
	
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
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final Device item = (Device) parent.getItemAtPosition(position);
				activity.selectedDevice = item;

				activity.startDeviceFragment();

			}

		});

		populateList();

		return rootView;

	}

	private void populateList() {

		adapter = new DeviceListAdapter(activity,
				activity.getAvailableDevices());
		list.setAdapter(adapter);
	}
}
