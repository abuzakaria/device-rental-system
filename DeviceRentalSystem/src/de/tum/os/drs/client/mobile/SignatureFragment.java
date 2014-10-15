package de.tum.os.drs.client.mobile;

import src.com.github.gcacace.signaturepad.views.SignaturePad;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SignatureFragment extends Fragment {

	private SignaturePad mSignaturePad;
	private Button mClearButton, mSaveButton;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.fragment_signature,
				container, false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mSignaturePad = (SignaturePad) rootView
				.findViewById(R.id.signature_pad);

		mClearButton = (Button) rootView.findViewById(R.id.clear_button);
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSignaturePad.clear();
			}
		});

		mSaveButton = (Button) rootView.findViewById(R.id.save_button);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

				activity.signature = "Test Signature";

				if (activity.rentingSignature) {

					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.frame_container,
							new RentConfirmFragment());
					transaction.addToBackStack(null);
					transaction.commit();

				} else {
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.frame_container,
							new ReturnConfirmFragment());
					transaction.addToBackStack(null);
					transaction.commit();

				}

			}

		});

		mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
			@Override
			public void onSigned() {
				mSaveButton.setEnabled(true);
				mClearButton.setEnabled(true);
			}

			@Override
			public void onClear() {
				mSaveButton.setEnabled(false);
				mClearButton.setEnabled(false);
			}
		});

		activity = (MainActivity) getActivity();

		return rootView;
	}

}
