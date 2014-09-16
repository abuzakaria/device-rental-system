package com.example.devicerentalsystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;

public class ScanFragment extends Fragment implements IScanResultHandler{
	
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
        
        return rootView;
    }

	
	@Override
	public void scanResult(ScanResult result) {
		// Auto-generated method stub
		Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(50);
		Toast.makeText(getActivity(), result.getRawResult().getText(), Toast.LENGTH_LONG).show();
		((MainActivity)getActivity()).mScanResult = result.getRawResult().getText();
		this.getFragmentManager().popBackStack();
	}
}
