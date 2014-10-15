package de.tum.os.drs.client.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

	  private FragmentTabHost mTabHost;

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	
	        mTabHost = new FragmentTabHost(getActivity());
	        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
	        
	        mTabHost.addTab(mTabHost.newTabSpec("devices").setIndicator("Available"),
	                AvailableDevicesFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("devics").setIndicator("All"),
	                RentedDevicesFragment.class, null);
	        
	        return mTabHost;
	    }

	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        mTabHost = null;
	    }

}
