package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.adapters.NavDrawerListAdapter;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.AfterRentersUpdateAction;
import de.tum.os.drs.client.mobile.model.AfterScanAction;
import de.tum.os.drs.client.mobile.model.AfterSignatureAction;
import de.tum.os.drs.client.mobile.model.CredentialStore;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.NavDrawerItem;
import de.tum.os.drs.client.mobile.model.Renter;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";

	// The navigation drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	// Used to store access tokens
	private CredentialStore store;

	// The result of the barcode scanning
	public String scanResult;

	// The currently selected device and renter
	public Device selectedDevice;
	public Renter selectedRenter;

	// List of fetched devices
	private List<Device> availableDevices;
	private List<Device> rentedDevices;

	// List of renters
	private List<Renter> renters;

	// The server stub instance
	private RentalService service;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {

			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));

		}

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {

				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {

				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		setTitle(R.string.app_name);

		store = new CredentialStore(
				PreferenceManager.getDefaultSharedPreferences(this));

		service = RentalServiceImpl.getInstance();

		updateDevices(null, AfterDeviceUpdateAction.GO_TO_HOME);
	}

	private void startAuthenticationActivity() {

		Intent intent = new Intent(this, AuthenticationActivity.class);
		startActivity(intent);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	/**
	 * Listens for menu item clicks.
	 * 
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// We only use an about dialog as menu item
		switch (item.getItemId()) {
		case R.id.action_about:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"This app was developed as a project for Android Practical Summer School Course by Pablo Arias & Abu Zakaria")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do things
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Displaying fragment view when selected from the navigation drawer list
	 * item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments

		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new AddDeviceFragment();
			break;
		case 2:
			fragment = new ScanFragment();
			// Open a device after scanned
			Bundle b = new Bundle();
			b.putString("action", AfterScanAction.OPEN_DEVICE.toString());
			fragment.setArguments(b);
			break;
		case 3:
			// Log the user out
			logout();
			break;
		case 4:
			// Close the app
			System.exit(1);

		default:
			break;
		}

		if (fragment != null) {

			// Clear the fragment stack.
			FragmentManager manager = getSupportFragmentManager();
			if (manager.getBackStackEntryCount() > 0) {
				FragmentManager.BackStackEntry first = manager
						.getBackStackEntryAt(0);
				manager.popBackStack(first.getId(),
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}

			// Start the selected fragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/**
	 * Starts the device fragment
	 * 
	 */
	public void startDeviceFragment() {

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, new DeviceFragment())
				.addToBackStack(null).commit();

	}

	/**
	 * Callback for the device update action. Called after a successful fetching
	 * of devices.
	 * 
	 * 
	 * @param newImei
	 *            The imei of the device to show. Null if no device is to be
	 *            shown
	 * @param action
	 *            Action to be performed after the update has finished
	 */
	private void onListUpdateFinished(final String newImei, final String mtrNr,
			final AfterDeviceUpdateAction action) {

		switch (action) {
		case UPDATE_RENTER:
			// Called when device was returned or rented. Now update the renter.
			updateSingleRenter(mtrNr, AfterRentersUpdateAction.GO_TO_HOME);
			break;
		case OPEN_DEVICE:
			// Show a device (e.g. when device was added or updated)
			selectAndShowDevice(newImei);
			break;
		case GO_TO_HOME:
			// Return to the home fragment
			returnToHome();
			break;
		default:
			break;

		}

	}

	/**
	 * 
	 * Callback for the renters update event
	 * 
	 * @param mtrNr
	 * @param action
	 *            Action to be performed afterwards
	 */
	private void onRentersUpdateFinished(final String mtrNr,
			final AfterRentersUpdateAction action) {

		switch (action) {
		case GO_TO_HOME:
			hideLoadingDialog();
			returnToHome();
			break;
		case OPEN_RENTER_FRAGMENT: {

			// Start the renter information fragment
			Renter renter = getRenterFromMtrNr(mtrNr);

			if (renter != null) {
				selectedRenter = renter;

				// Start renter fragment

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, new RenterFragment());
				transaction.addToBackStack(null);
				transaction.commit();

			} else {

				Log.e(TAG, "Renter not found");

			}

		}
			break;
		default:
			break;

		}

	}

	/**
	 * Callback for the scan event. Called after the scan fragment has finished
	 * reading the barcode.
	 * 
	 * @param action
	 *            Action to be performed after the scan
	 * @param result
	 *            The result of the scan.
	 * 
	 */
	public void onScanFinished(AfterScanAction action, String result) {

		Log.i(TAG, "Scan result:" + result);

		switch (action) {
		case OPEN_DEVICE: {
			selectAndShowDevice(result);
			break;
		}

		case SET_IMEI_FILED:
			scanResult = result;
			this.getSupportFragmentManager().popBackStack();
			break;
		default:
			break;

		}

	}

	/**
	 * Callback for signature event. Called after a signature has been entered.
	 * 
	 * @param signature
	 *            The serialized signature as base64
	 * @param action
	 *            Action to perform after the signature has been fetched
	 */
	public void onSignatureFinished(final String signature,
			final AfterSignatureAction action) {

		// Provide the signature to the confirmation fragment
		Bundle b = new Bundle();
		b.putString("signature", signature);

		switch (action) {

		case OPEN_RENT_CONFIRM: {

			// Open rent confirmation screen
			RentConfirmFragment f = new RentConfirmFragment();
			f.setArguments(b);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.frame_container, f);
			transaction.addToBackStack(null);
			transaction.commit();

		}
			break;
		case OPEN_RETURN_CONFIRM: {
			// Open return confirmation screen

			ReturnConfirmFragment f = new ReturnConfirmFragment();
			f.setArguments(b);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.frame_container, f);
			transaction.addToBackStack(null);
			transaction.commit();

		}
			break;
		default:
			break;

		}

	}

	/**
	 * Queries a device by Imei and starts the DeviceFragment
	 * 
	 * 
	 * @param imei
	 *            The imei of the device to be shown
	 */
	private void selectAndShowDevice(String imei) {
		Device d = getDeviceFromImei(imei);

		if (d != null) {

			selectedDevice = d;
			startDeviceFragment();
		} else {

			this.getSupportFragmentManager().popBackStack();
			showToast("The scanned device could not be found");
			returnToHome();
		}

	}

	/**
	 * Returns a device matching the provided Imei
	 * 
	 * @param result
	 *            The imei found
	 * 
	 * @return The found device. Null if not found
	 * 
	 * 
	 */
	private Device getDeviceFromImei(String imei) {

		List<Device> available = getAvailableDevices();

		for (Device d : available) {

			if (d.getImei().equals(imei)) {

				return d;
			}

		}

		List<Device> rented = getRentedDevices();

		for (Device d : rented) {

			if (d.getImei().equals(imei)) {

				return d;
			}

		}

		return null;

	}

	/**
	 * Returns a renter matching the provided matr nr.
	 * 
	 * 
	 * @param mtr
	 * @return
	 */
	public Renter getRenterFromMtrNr(String mtr) {

		for (Renter r : renters) {

			if (r.getMatriculationNumber().equals(mtr)) {

				return r;
			}

		}

		return null;

	}

	/**
	 * Updates the local device database Called when changes in the devices are
	 * made, e.g. after renting or editing.
	 * 
	 * @param newImei
	 *            Imei of the device to be shown
	 * @param action
	 *            Action to be performed after the update
	 */
	public void updateDevices(final String newImei,
			final AfterDeviceUpdateAction action) {

		showLoadingDialog("Updating devices");

		// Get all available devices first
		service.getAvailableDevices(new Callback<List<Device>>() {

			@Override
			public void onSuccess(List<Device> result) {

				availableDevices = result;
				// Now get the rented devices
				fetchRentedDevices(newImei, action);

			}

			@Override
			public void onFailure(int code, String error) {
				showToast(error);
				hideLoadingDialog();

				if (code == 401 || code == 403) {

					// Invalid session
					// We need to login again
					sessionExpired();

				}

			}

		});

	}

	/**
	 * Updates a single device and refreshes the list
	 * 
	 * 
	 * @param newImei
	 * @param action
	 */
	public void updateSingleDevice(final String imei, final String mtrNr,
			final AfterDeviceUpdateAction action) {

		showLoadingDialog("Refreshing device");
		service.getDeviceByImei(imei, new Callback<Device>() {

			@Override
			public void onSuccess(Device result) {
				hideLoadingDialog();
				updateSingleDeviceInList(imei, result);
				onListUpdateFinished(imei, mtrNr, action);

			}

			@Override
			public void onFailure(int code, String error) {
				showToast(error);
				hideLoadingDialog();

				if (code == 401 || code == 403) {

					// Invalid session
					// We need to login again
					sessionExpired();

				}

			}

		});

	}

	private void updateSingleDeviceInList(String imei, Device newDevice) {

		if (availableDevices != null) {

			for (int i = 0; i < availableDevices.size(); i++) {

				if (availableDevices.get(i).getImei().equals(imei)) {

					availableDevices.remove(i);

					if (newDevice.isAvailable()) {

						availableDevices.add(newDevice);

					} else {

						rentedDevices.add(newDevice);
					}

					return;

				}

			}

		}

		if (rentedDevices != null) {

			for (int i = 0; i < rentedDevices.size(); i++) {

				if (rentedDevices.get(i).getImei().equals(imei)) {

					rentedDevices.remove(i);

					if (newDevice.isAvailable()) {

						availableDevices.add(newDevice);

					} else {

						rentedDevices.add(newDevice);

					}

					return;

				}

			}

		}

		if (newDevice.isAvailable()) {

			availableDevices.add(newDevice);

		} else {

			rentedDevices.add(newDevice);
		}

	}

	public void updateSingleRenter(final String mtrNr,
			final AfterRentersUpdateAction action) {

		showLoadingDialog("Updating renter");
		service.getRenterFromMtrNr(mtrNr, new Callback<Renter>() {

			@Override
			public void onSuccess(Renter result) {
				hideLoadingDialog();
				updateSingleRenterInList(result);
				onRentersUpdateFinished(mtrNr, action);
			}

			@Override
			public void onFailure(int code, String error) {

				showToast(error);
				hideLoadingDialog();

				if (code == 401 || code == 403) {

					// Invalid session
					// We need to login again
					sessionExpired();

				}
			}

		});

	}

	private void updateSingleRenterInList(Renter newRenter) {

		if (renters != null) {

			for (int i = 0; i < renters.size(); i++) {

				if (renters.get(i).getMatriculationNumber()
						.equals(newRenter.getMatriculationNumber())) {

					renters.remove(i);
					renters.add(newRenter);
					return;

				}

			}

			renters.add(newRenter);

		}

	}

	/**
	 * Queries the rented devices from the server Updates the device list
	 * 
	 * 
	 * @param newImei
	 * @param action
	 *            Action to be performed after updating
	 */
	private void fetchRentedDevices(final String newImei,
			final AfterDeviceUpdateAction action) {

		service.getRentedDevices(new Callback<List<Device>>() {

			@Override
			public void onSuccess(List<Device> result) {
				hideLoadingDialog();
				rentedDevices = result;
				onListUpdateFinished(newImei, null, action);
			}

			@Override
			public void onFailure(int code, String error) {
				hideLoadingDialog();

				if (code == 401 || code == 403) {

					// Invalid session
					// We need to login again
					sessionExpired();

				}

			}

		});

	}

	/**
	 * Fetches the renters from the server and updates the database. Opens the
	 * renter fragment with the provided renter
	 * 
	 * 
	 * @param mtrNr
	 *            Renter to be shown afterwards
	 */
	public void updateRenters(final String mtrNr,
			final AfterRentersUpdateAction action) {

		showLoadingDialog("Updating renters");
		service.getAllRenters(new Callback<List<Renter>>() {

			@Override
			public void onSuccess(List<Renter> result) {
				hideLoadingDialog();
				// Update the local renters
				renters = result;

				onRentersUpdateFinished(mtrNr, action);

			}

			@Override
			public void onFailure(int code, String error) {
				hideLoadingDialog();
				showToast(error);

				if (code == 401 || code == 403) {

					// Invalid session
					// We need to login again
					sessionExpired();

				}

			}

		});

	}

	public List<Renter> getRenters() {
		return renters;

	}

	public void setRenters(List<Renter> r) {

		renters = r;
	}

	public List<Device> getAvailableDevices() {

		return availableDevices;

	}

	public List<Device> getRentedDevices() {
		return rentedDevices;

	}

	/**
	 * Logs the user out of the app
	 * 
	 */
	private void logout() {

		Log.i(TAG, "Loging out...");

		RentalServiceImpl.getInstance().logout(new Callback<String>() {

			@Override
			public void onSuccess(final String result) {
				Log.i(TAG, result);

				// Clear the user's credentials
				store.clearCredentials();
				showToast(result);

				// Close and start from the authentication activity
				startAuthenticationActivity();
				finish();

			}

			@Override
			public void onFailure(int code, String error) {

				Log.i(TAG, error);
				showToast("Error code received: " + code + " " + error);

				// Clear the user's credentials
				store.clearCredentials();

				// Close and start from the authentication activity
				startAuthenticationActivity();
				finish();

			}

		});

	}

	/**
	 * The app has an invalid token. Logout the user and refresh the session.
	 * 
	 * 
	 */
	public void sessionExpired() {

		finish();
		startAuthenticationActivity();
	}

	/**
	 * Clears the whole fragment stack and relaunches the HomeFragment
	 * 
	 */
	public void returnToHome() {

		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);

		displayView(0);

	}

	private void clearStack() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager
					.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		Fragment f = getSupportFragmentManager().findFragmentById(
				R.id.frame_container);

		// Return immediatelly to the home fragment. Don't go back the stack.
		if (f instanceof DeviceFragment || f instanceof RentConfirmFragment
				|| f instanceof ReturnConfirmFragment) {

			returnToHome();
			return;

		}

		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

			if (f instanceof HomeFragment) {
				// Don't close the home fragment
				mDrawerLayout.openDrawer(mDrawerList);
			} else {

				// Return to safe state
				returnToHome();
			}

		} else {

			MainActivity.super.onBackPressed();
		}

	}

	public void showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();

	}

	/**
	 * Returns the ID of the image assigned to the device name
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public int getDeviceImage(String name) {

		int r = unknown_device_picture;

		if (name == null) {

			return r;
		}

		String lower = name.toLowerCase().toString();

		if (deviceNameToImageNameMap.containsKey(lower)) {
			r = deviceNameToImageNameMap.get(lower);

		}

		return r;

	}

	public void showLoadingDialog(String message) {

		if (dialog != null) {

			dialog.dismiss();
		}

		dialog = ProgressDialog.show(this, "Please wait ...", message, true);
		dialog.setCancelable(false);
	}

	public void hideLoadingDialog() {

		if (dialog != null) {

			dialog.dismiss();

		}

	}

	// Image map for stored drawables
	private int unknown_device_picture = R.drawable.ic_action_hardware_phone;
	private HashMap<String, Integer> deviceNameToImageNameMap = new HashMap<String, Integer>() {
		private static final long serialVersionUID = -4645423715285941470L;
		{
			put("nexus one", R.drawable.nexus_one);
			put("nexus s", R.drawable.nexus_s);
			put("galaxy nexus", R.drawable.galaxy_nexus);
			put("nexus 4", R.drawable.nexus_4);
			put("nexus 7", R.drawable.nexus_7);
			put("htc one", R.drawable.htc_one);
			put("htc one x", R.drawable.htc_one_x);
			put("htc one x+", R.drawable.htc_one_x_plus);
			put("nexus 10", R.drawable.nexus_10);
		}
	};

}
