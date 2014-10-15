package de.tum.os.drs.client.mobile;

import garin.artemiy.simplescanner.library.fragments.SimpleScannerFragment;
import garin.artemiy.simplescanner.library.listeners.ScannerListener;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tum.os.drs.client.mobile.model.Device;

public class ScanFragment extends Fragment implements ScannerListener {
	private boolean isResultReceived = false;

	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		isResultReceived = false;
		View rootView = inflater.inflate(R.layout.fragment_scan, container,
				false);
		FragmentManager fragmentManager = getFragmentManager();
		SimpleScannerFragment ssFrag = new SimpleScannerFragment();
		fragmentManager.beginTransaction()
				.replace(R.id.sample, (Fragment) ssFrag).commit();
		ssFrag.setScannerListener(this);

		activity = (MainActivity) getActivity();
		return rootView;
	}

	@Override
	public void onDataReceive(String result, int barcodeType) {
		if (isResultReceived == false) {
			isResultReceived = true;
			((MainActivity) getActivity()).mScanResult = result;
			Vibrator v = (Vibrator) getActivity().getSystemService(
					Context.VIBRATOR_SERVICE);
			v.vibrate(50);

			Log.i("Test", result);
			
			Device d = processScan(result);
			
			activity.receiveScannedDevice(d);
			
			this.getFragmentManager().popBackStack();

		}
	}

	private Device processScan(String result) {

		List<Device> available = activity.getAvailableDevices();

		for (Device d : available) {

			if (d.getImei().equals(result)) {

				return d;
			}

		}

		List<Device> rented = activity.getRentedDevices();

		for (Device d : rented) {

			if (d.getImei().equals(result)) {

				return d;
			}

		}
		
		return null;

	}

}
