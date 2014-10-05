package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReturnFragment extends Fragment {
	
	public ReturnFragment(){}	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(((MainActivity)getActivity()).mDrawerTitle);
        View rootView = inflater.inflate(R.layout.fragment_return, container, false);
         
        return rootView;
    }

}
