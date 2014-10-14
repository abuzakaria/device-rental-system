package de.tum.os.drs.client.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;
import src.com.github.gcacace.signaturepad.views.SignaturePad;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReturnFragment extends Fragment {
	private SignaturePad mSignaturePad;
	private TextView deviceDetails;
	private EditText mIMEI, mComments;
	private String sIMEI, sMatNo;
	private Renter m_renter;

	private RelativeLayout RL;
	private Button mClearButton, mSaveButton, mFindButton, mReturnButton,
			mScanButton;
	private RentalService service;

	public ReturnFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_return, container,
				false);
		getActivity().setTitle("Return a device");

		service = RentalServiceImpl.getInstance();
		mIMEI = (EditText) rootView.findViewById(R.id.device_serial);
		mComments = (EditText) rootView.findViewById(R.id.comments);
		mSignaturePad = (SignaturePad) rootView
				.findViewById(R.id.signature_pad);
		mScanButton = (Button) rootView.findViewById(R.id.scan);
		mFindButton = (Button) rootView.findViewById(R.id.find_device);
		deviceDetails = (TextView) rootView.findViewById(R.id.device_details);
		mReturnButton = (Button) rootView.findViewById(R.id.return_btn);
		RL = (RelativeLayout) rootView
				.findViewById(R.id.signature_pad_container);
		mClearButton = (Button) rootView.findViewById(R.id.clear_button);
		mSaveButton = (Button) rootView.findViewById(R.id.save_button);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sIMEI = ((MainActivity) getActivity()).mScanResult;
		if (sIMEI != null) {
			((MainActivity) getActivity()).mScanResult = null;
			Log.d("scan", sIMEI);
			mIMEI.setText(sIMEI);

		}

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

		mScanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, new ScanFragment(),
						"NewFragmentTag");
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		mFindButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mIMEI.getText().length() > 0) {
					service.getAllActiveRenters(new Callback<List<Renter>>() {
						@Override
						public void onFailure(int code, String error) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(List<Renter> result) {
							// TODO Auto-generated method stub
							for (Renter r : result) {
								for (String d : r.getRentedDevices()) {
									if (d.trim().equals(
											mIMEI.getText().toString())) {
										m_renter = r;
										sMatNo = r.getMatriculationNumber();
										break;
									}
								}
							}
						}

					});
					service.getDeviceByImei(mIMEI.getText().toString(),
							new Callback<Device>() {

								@Override
								public void onSuccess(Device result) {
									Log.i("Device", result.getDeviceType()
											.toString());
									displayDetails(result);
								}

								@Override
								public void onFailure(int code, String error) {
									Log.i("Error", error);
									deviceDetails.setVisibility(View.GONE);
									Toast.makeText(getActivity(), error,
											Toast.LENGTH_SHORT).show();

									if (code == 401) {
										((MainActivity) getActivity())
												.sessionExpired();
									}
								}

								private void displayDetails(Device d) {
									// TODO Auto-generated method stub
									String temp = "Name: ";
									temp += d.getName() == null ? "<None>" : d
											.getName();
									temp += "\n";

									temp += "IMEI: ";
									temp += d.getImei() == null ? "<None>" : d
											.getImei();
									temp += "\n";

									temp += "Description: ";
									temp += d.getDescription() == null ? "<None>"
											: d.getDescription();
									temp += "\n";

									temp += "Type: ";
									temp += d.getDeviceType() == null ? "<None>"
											: d.getDeviceType().toString();
									temp += "\n";

									temp += "State: ";
									temp += d.getState() == null ? "<None>" : d
											.getState();
									temp += "\n";

									temp += "Availabilty: ";
									temp += d.isAvailable() ? "Available"
											: "Unavailable";
									temp += "\n";

									if (!d.isAvailable()) {
										temp += "Renter: ";
										temp += m_renter.getName();
										temp += "\n";

										temp += "Estimated Return Date: ";
										temp += d.getEstimatedReturnDate() == null ? "<Not found>"
												: d.getEstimatedReturnDate()
														.toString();
										temp += "\n";

										sIMEI = d.getImei();

										mReturnButton
												.setVisibility(View.VISIBLE);
									}

									deviceDetails.setVisibility(View.VISIBLE);
									deviceDetails.setText(temp);
								}

							});
				} else
					Toast.makeText(getActivity(),
							"Search failed: Empty serial", Toast.LENGTH_SHORT)
							.show();
			}
		});

		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSignaturePad.clear();
			}
		});

		mReturnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mReturnButton.setVisibility(View.GONE);
				mScanButton.setVisibility(View.GONE);
				mIMEI.setVisibility(View.GONE);
				mFindButton.setVisibility(View.GONE);
				RL.setVisibility(View.VISIBLE);
				mComments.setVisibility(View.VISIBLE);
				mSaveButton.setVisibility(View.VISIBLE);
				mClearButton.setVisibility(View.VISIBLE);
			}
		});

		mSaveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
				if (addSignatureToGallery(signatureBitmap)) {
					Log.d("sign", "Signature saved into the Gallery");
					sendreturnrequest();

				} else {
					Log.d("sign", "Unable to store the signature");
					Toast.makeText(getActivity(), "Operation failed",
							Toast.LENGTH_SHORT).show();
				}
			}

			private void sendreturnrequest() {
				// TODO Auto-generated method stub
				List<String> imeis = new ArrayList<String>();
				if (sIMEI != null && sMatNo != null) {
					imeis.add(sIMEI);
					service.returnDevices(new ReturnRequest(sMatNo, imeis,
							mComments.getText().toString(), "signature"),
							new Callback<String>() {

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									MainActivity _main;
									_main = (MainActivity) getActivity();
									_main.mSelectedDeviceImei = sIMEI;
									final FragmentTransaction ft = getFragmentManager()
											.beginTransaction();
									ft.replace(R.id.frame_container,
											new DeviceFragment(),
											"NewFragmentTag");
									ft.commit();
								}

								@Override
								public void onFailure(int code, String error) {
									// TODO Auto-generated method stub

								}

							});
				}
			}

		});
	}

	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e("SignaturePad", "Directory not created");
		}
		return file;
	}

	public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
		stream.close();
	}

	public boolean addSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			File photo = new File(getAlbumStorageDir("Device-rental-system"),
					String.format("Signature_%d.jpg",
							System.currentTimeMillis()));
			saveBitmapToPNG(signature, photo);
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			getActivity().sendBroadcast(mediaScanIntent);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
