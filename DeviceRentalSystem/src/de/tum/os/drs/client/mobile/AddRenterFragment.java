package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterRentersUpdateAction;
import de.tum.os.drs.client.mobile.model.Renter;

public class AddRenterFragment extends Fragment {

	private EditText name;
	private EditText matrNr;
	private EditText phone;
	private EditText email;
	private EditText comments;

	private Button add;

	private RentalService service;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_add_renter,
				container, false);
		
		getActivity().setTitle("Add renter");

		name = (EditText) rootView.findViewById(R.id.renterNameAdd);
		matrNr = (EditText) rootView.findViewById(R.id.mtrNrAdd);
		phone = (EditText) rootView.findViewById(R.id.phoneNrAdd);
		email = (EditText) rootView.findViewById(R.id.emailAdd);
		comments = (EditText) rootView.findViewById(R.id.renterCommentsAdd);

		service = RentalServiceImpl.getInstance();
		activity = (MainActivity) getActivity();

		add = (Button) rootView.findViewById(R.id.addRenter);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addRenter();
			}

		});

		return rootView;

	}

	/**
	 * 
	 * Adds a renter to the database. 
	 * 
	 */
	private void addRenter() {

		//Matr Nr., Name, and email are mandatory
		if (matrNr.getText().length() > 0 && name.getText().length() > 0
				&& email.getText().length() > 0) {

			String mat = matrNr.getText().toString();

			String rEmail = email.getText().toString();

			String rPhone = "";
			if (phone.getText().length() > 0) {

				rPhone = phone.getText().toString();
			}

			String rName = name.getText().toString();

			String comm = "";
			if (comments.getText().length() > 0) {
				comm = comments.getText().toString();
			}

			
			//Construct new renter from provided data
			final Renter renter = new Renter(rName, mat, rEmail, rPhone, comm);

			activity.showLoadingDialog("Adding renter");
			service.addRenter(renter, new Callback<String>() {

				@Override
				public void onSuccess(String result) {

					activity.hideLoadingDialog();
					activity.showToast(result);
					//Update the local copy and show the renter in the information fragment
					activity.updateRenters(renter.getMatriculationNumber(), AfterRentersUpdateAction.OPEN_RENTER_FRAGMENT);

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

		} else {

			activity.showToast("Name, Matriculation number and email are mandatory");

		}

	}

}
