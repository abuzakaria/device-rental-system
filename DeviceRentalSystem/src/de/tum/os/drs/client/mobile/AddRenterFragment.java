package de.tum.os.drs.client.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
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

		name = (EditText) rootView.findViewById(R.id.renter_name);
		matrNr = (EditText) rootView.findViewById(R.id.mtrNr);
		phone = (EditText) rootView.findViewById(R.id.phoneNr);
		email = (EditText) rootView.findViewById(R.id.email);
		comments = (EditText) rootView.findViewById(R.id.renter_comments);
		
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

	private void addRenter() {
		
		String mat = matrNr.getText().toString();
		
		if(!mat.isEmpty()){
			
			String rEmail = email.getText().toString();
			String rPhone = phone.getText().toString();
			String rName = name.getText().toString();
			String comm = comments.getText().toString();
			
			final Renter renter = new Renter(rName, mat, rEmail,rPhone, comm);
			
			service.addRenter(renter, new Callback<String>(){

				@Override
				public void onSuccess(String result) {
					
					//TODO toast
					
					activity.selectedRenter = renter;
					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.frame_container, new RenterFragment());
					transaction.commit();
					
				}

				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		} else {
			
			
			
		}

	}

}
