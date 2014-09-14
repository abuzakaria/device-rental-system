package com.example.devicerentalsystem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddFragment extends Fragment {
	public AddFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
         
        return rootView;
    }

}
