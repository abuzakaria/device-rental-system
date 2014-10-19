package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.model.Renter;

public class RenterFragment extends Fragment {

	private TextView dump;
	private MainActivity activity;
	private Renter renter;
	private Button rentButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		getActivity().setTitle("Renter Details");
		View rootView = inflater.inflate(R.layout.fragment_renter, container,
				false);

		dump = (TextView) rootView.findViewById(R.id.renterDump);
		activity = (MainActivity) getActivity();
		renter = activity.selectedRenter;
		
		rentButton = (Button) rootView.findViewById(R.id.rentToRenter);
		rentButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				activity.rentingSignature = true;
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_container, new SignatureFragment());
				transaction.addToBackStack(null);
				transaction.commit();
				
			}
			
		});
		
		showRenterInformation();
		return rootView;
	}

	private void showRenterInformation() {

		if (renter == null) {
			return;
		}

		String temp = "Name: ";
		temp += renter.getName() == null ? "<None>" : renter.getName();
		temp += "\n";

		temp += "MatriculationNumber: ";
		temp += renter.getMatriculationNumber() == null ? "<None>" : renter.getMatriculationNumber();
		temp += "\n";

		temp += "Email: ";
		temp += renter.getEmail() == null ? "<None>" : renter.getEmail();
		temp += "\n";

		temp += "Phone Number: ";
		temp += renter.getPhoneNumber() == null ? "<None>" : renter.getPhoneNumber();
		temp += "\n";

		temp += "Comments: \n";
		temp += renter.getComments() == null ? "<None>" : renter.getComments();
		temp += "\n";

		dump.setText(temp);
	}

}
