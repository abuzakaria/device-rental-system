package de.tum.os.drs.client.mobile;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import de.tum.os.drs.client.mobile.adapters.RentersListAdapter;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Renter;

public class RenterSelectionFragment extends Fragment {

	private RentalService service;
	private MainActivity activity;
	private ListView list;
	private EditText inputSearch;
	private ImageButton addRenterButton;
	private ArrayAdapter<Renter> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		getActivity().setTitle("Select Renter");
		View rootView = inflater.inflate(R.layout.fragment_select_renters,
				container, false);

		list = (ListView) rootView.findViewById(R.id.renters_list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final Renter item = (Renter) parent.getItemAtPosition(position);
				activity.selectedRenter = item;

				// Start the device fragment
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, new RenterFragment());
				transaction.addToBackStack(null);
				transaction.commit();

			}

		});

		activity = (MainActivity) getActivity();
		service = RentalServiceImpl.getInstance();

		inputSearch = (EditText) rootView.findViewById(R.id.inputSearchR);
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

		addRenterButton = (ImageButton) rootView.findViewById(R.id.addRenter);
		addRenterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container,
						new AddRenterFragment());
				transaction.addToBackStack(null);
				transaction.commit();

			}

		});

		populateList();

		return rootView;

	}

	private void populateList() {

		if (activity.getRenters() != null) {
			adapter = new RentersListAdapter(activity, activity.getRenters());
			list.setAdapter(adapter);
		} else {

			activity.showLoadingDialog("Loading renters");
			service.getAllRenters(new Callback<List<Renter>>() {

				@Override
				public void onSuccess(List<Renter> result) {
					activity.hideLoadingDialog();
					activity.setRenters(result);
					adapter = new RentersListAdapter(activity, result);
					list.setAdapter(adapter);

				}

				@Override
				public void onFailure(int code, String error) {
					activity.hideLoadingDialog();

				}

			});

		}

	}
}
