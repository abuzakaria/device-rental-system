package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RentFragment extends Fragment {
	
	public RentFragment(){}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(((MainActivity)getActivity()).mDrawerTitle); 
        View rootView = inflater.inflate(R.layout.fragment_rent, container, false);

        return rootView;
    }

}
