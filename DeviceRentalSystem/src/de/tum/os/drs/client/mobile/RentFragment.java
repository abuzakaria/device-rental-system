package de.tum.os.drs.client.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.DeviceType;
import de.tum.os.drs.client.mobile.model.RentRequest;
import de.tum.os.drs.client.mobile.model.Renter;

public class RentFragment extends Fragment {
	
	private SignaturePad mSignaturePad;
	TextView deviceDetails, renterDetails;
	private Button mClearButton, mSaveButton, mFindButton, mRentButton,
			mScanButton, mConfirmButton;
	RentalService service;
	Spinner renterSpinner;
	DatePicker datepicker;
	ArrayList<Renter> m_renters;
	ArrayList<String> m_rentersName;
	private EditText mIMEI, mComments;
	String sIMEI, sMatNo;
	RelativeLayout RL;
	Renter m_renter;

	public RentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_rent, container,
				false);
		getActivity().setTitle("Rent a device");
		service = RentalServiceImpl.getInstance();
		mSignaturePad = (SignaturePad) rootView
				.findViewById(R.id.signature_pad);
		mClearButton = (Button) rootView.findViewById(R.id.clear_button);
		mSaveButton = (Button) rootView.findViewById(R.id.save_button);
		mFindButton = (Button) rootView.findViewById(R.id.find_device);
		mRentButton = (Button) rootView.findViewById(R.id.rent_btn);
		mScanButton = (Button) rootView.findViewById(R.id.scan);
		mConfirmButton = (Button) rootView.findViewById(R.id.confirm_btn);
		//renterSpinner = (Spinner) rootView.findViewById(R.id.renter_spinner);
		deviceDetails = (TextView) rootView.findViewById(R.id.device_details);
		renterDetails = (TextView) rootView.findViewById(R.id.renter_details);
		mIMEI = (EditText) rootView.findViewById(R.id.device_serial);
		mComments = (EditText) rootView.findViewById(R.id.comments);
		datepicker = (DatePicker) rootView.findViewById(R.id.datePicker);
		RL = (RelativeLayout) rootView
				.findViewById(R.id.signature_pad_container);

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
									} else {
										sIMEI = d.getImei();
										mRentButton.setVisibility(View.VISIBLE);
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

		mRentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mIMEI.setVisibility(View.GONE);
				mFindButton.setVisibility(View.GONE);
				mScanButton.setVisibility(View.GONE);
				mRentButton.setVisibility(View.GONE);
				populateRenterList();
				renterSpinner.setVisibility(View.VISIBLE);

			}

			private void populateRenterList() {
				// TODO Auto-generated method stub
				service.getAllRenters(new Callback<List<Renter>>() {

					@Override
					public void onSuccess(List<Renter> result) {
						m_renters = new ArrayList<Renter>();
						m_rentersName = new ArrayList<String>();
						m_renters.addAll(result);
						for (Renter r : m_renters) {
							m_rentersName.add(r.getName());
						}
						renterSpinner.setAdapter(new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_dropdown_item,
								m_rentersName));
					}

					@Override
					public void onFailure(int code, String error) {
						Log.i("AllRenters", error);
					}
				});
			}
		});

		renterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				displayDetails(m_renters.get(arg2));
				renterDetails.setVisibility(View.VISIBLE);
				datepicker.setVisibility(View.VISIBLE);
				mConfirmButton.setVisibility(View.VISIBLE);
				// Toast.makeText(getActivity(), m_renters.get(arg2).getName(),
				// Toast.LENGTH_SHORT).show();
			}

			private void displayDetails(Renter d) {
				// TODO Auto-generated method stub
				String temp = "Name: ";
				temp += d.getName() == null ? "<None>" : d.getName();
				temp += "\n";

				temp += "Email: ";
				temp += d.getEmail() == null ? "<None>" : d.getEmail();
				temp += "\n";

				temp += "Mat. No: ";
				temp += d.getMatriculationNumber() == null ? "<None>" : d
						.getMatriculationNumber();
				temp += "\n";

				temp += "Phone: ";
				temp += d.getPhoneNumber() == null ? "<None>" : d
						.getPhoneNumber().toString();
				temp += "\n";

				temp += "Comments: ";
				temp += d.getComments() == null ? "<None>" : d.getComments();
				temp += "\n";

				sMatNo = d.getMatriculationNumber();
				renterDetails.setText(temp);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
				if (addSignatureToGallery(signatureBitmap)) {
					Log.d("sign", "Signature saved into the Gallery");
					sendrentrequest();

				} else {
					Log.d("sign", "Unable to store the signature");
				}
			}

			private void sendrentrequest() {
				// TODO Auto-generated method stub
				List<String> imeis = new ArrayList<String>();
				if (sIMEI != null && sMatNo != null) {
					imeis.add(sIMEI);
					service.rentDevices(new RentRequest(sMatNo, imeis,
							getDateFromDatePicker(datepicker), mComments
									.getText().toString(), "Signature"),
							new Callback<String>() {

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									MainActivity _main;
									_main = (MainActivity) getActivity();
								//	_main.mSelectedDeviceImei = sIMEI;
									final FragmentTransaction ft = getFragmentManager()
											.beginTransaction();
									ft.replace(R.id.frame_container,
											new DeviceFragment(),
											"NewFragmentTag");
									ft.commit();
								}

								@Override
								public void onFailure(int code, String error) {
									Log.i("RentService", error);
									Toast.makeText(getActivity(), error,
											Toast.LENGTH_SHORT).show();

									if (code == 401) {
										((MainActivity) getActivity())
												.sessionExpired();
									}
								}
							});
				}

			}

			private java.util.Date getDateFromDatePicker(DatePicker datePicker) {
				int day = datePicker.getDayOfMonth();
				int month = datePicker.getMonth();
				int year = datePicker.getYear();

				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				return calendar.getTime();
			}
		});

		mConfirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mConfirmButton.setVisibility(View.GONE);
				renterSpinner.setVisibility(View.GONE);
				RL.setVisibility(View.VISIBLE);
				mComments.setVisibility(View.VISIBLE);
				mClearButton.setVisibility(View.VISIBLE);
				mSaveButton.setVisibility(View.VISIBLE);
			}
		});
		return rootView;
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

	@Override
	public void onResume() {
		super.onResume();
		sIMEI = ((MainActivity) getActivity()).mScanResult;
		if (sIMEI != null) {
			((MainActivity) getActivity()).mScanResult = null;
			Log.d("scan", sIMEI);
			mIMEI.setText(sIMEI);

		}
	}

}
