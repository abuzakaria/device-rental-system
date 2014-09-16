package com.example.devicerentalsystem;

import com.example.devicerentalsystem.ScanFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AddFragment extends Fragment {
	
    Button btn;
	public AddFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        btn = ((Button)rootView.findViewById(R.id.scan));
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new ScanFragment(), "NewFragmentTag"); 
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});
        return rootView;
    }
}
