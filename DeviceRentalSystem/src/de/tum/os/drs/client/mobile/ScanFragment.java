package de.tum.os.drs.client.mobile;

import garin.artemiy.simplescanner.library.fragments.SimpleScannerFragment;
import garin.artemiy.simplescanner.library.listeners.ScannerListener;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tum.os.drs.client.mobile.model.AfterScanAction;

public class ScanFragment extends Fragment implements ScannerListener {
	private boolean isResultReceived = false;

	private MainActivity activity;
	private AfterScanAction scanAction;

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

		Bundle b = getArguments();
		scanAction = AfterScanAction.toScanAction(b.getString("action"));

		activity = (MainActivity) getActivity();

		return rootView;
	}

	@Override
	public void onDataReceive(String result, int barcodeType) {
		if (isResultReceived == false) {
			isResultReceived = true;

			Vibrator v = (Vibrator) getActivity().getSystemService(
					Context.VIBRATOR_SERVICE);
			v.vibrate(50);

			activity.onScanFinished(scanAction, result);

		}
	}

}
