package com.example.devicerentalsystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;

public class ScanFragment extends Fragment implements IScanResultHandler{
	
    Button btn;
    String result = null;
	public ScanFragment(){}
	BarcodeFragment cameraFrag = new BarcodeFragment();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.sample, (Fragment)cameraFrag).commit();
		cameraFrag.setScanResultHandler(this);
        btn = ((Button)rootView.findViewById(R.id.scan));
        btn.setEnabled(false); 
        
        return rootView;
    }

	
	@Override
	public void scanResult(ScanResult result) {
		// Auto-generated method stub
        btn.setEnabled(true);
        this.result = result.getRawResult().getText();
		Toast.makeText(getActivity(), this.result, Toast.LENGTH_LONG).show();
		MainActivity a = (MainActivity)getActivity();
		a.mScanResult = this.result;
	}
	
	


}
