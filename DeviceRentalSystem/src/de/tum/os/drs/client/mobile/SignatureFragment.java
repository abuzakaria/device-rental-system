package de.tum.os.drs.client.mobile;

import java.io.ByteArrayOutputStream;

import src.com.github.gcacace.signaturepad.views.SignaturePad;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.tum.os.drs.client.mobile.model.AfterScanAction;
import de.tum.os.drs.client.mobile.model.AfterSignatureAction;

public class SignatureFragment extends Fragment {

	private SignaturePad mSignaturePad;
	private Button mClearButton, mSaveButton;
	private MainActivity activity;
	private AfterSignatureAction action;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getActivity().setTitle("Signature");

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
				String base64 = getBase64FromBitmap(mSignaturePad
						.getSignatureBitmap());

				String signature = "<img src=\"data:image/png;base64," + base64
						+ "\" />";

				activity.onSignatureFinished(signature, action);

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
		
		Bundle b = getArguments();
		action = AfterSignatureAction.toScanAction(b.getString("action"));
		return rootView;
	}

	private String getBase64FromBitmap(Bitmap signatureBitmap) {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100,
				byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		return Base64.encodeToString(byteArray, Base64.NO_WRAP);
	}

}
