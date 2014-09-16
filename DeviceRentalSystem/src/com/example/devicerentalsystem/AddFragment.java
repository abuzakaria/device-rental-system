package com.example.devicerentalsystem;

import com.example.devicerentalsystem.ScanFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFragment extends Fragment {
    Button btn;
    String scanresult;
    EditText deviceSerial;
	public AddFragment(){}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("check","on createview");
		super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        btn = ((Button)rootView.findViewById(R.id.scan));
        deviceSerial = ((EditText)rootView.findViewById(R.id.device_serial));
        deviceSerial.setText("");
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
				ft.replace(R.id.frame_container, new ScanFragment(), "NewFragmentTag"); 
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});
        
        return rootView;
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		scanresult = ((MainActivity)getActivity()).mScanResult;
        if (scanresult != null)   
        {
        	((MainActivity)getActivity()).mScanResult = null;
        	Log.d("scan", scanresult);
        	deviceSerial.setText(scanresult);
        	
        }
	}
}
