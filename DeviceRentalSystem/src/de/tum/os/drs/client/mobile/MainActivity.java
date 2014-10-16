package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import de.tum.os.drs.client.mobile.adapter.NavDrawerListAdapter;
import de.tum.os.drs.client.mobile.communication.Callback;
import de.tum.os.drs.client.mobile.communication.RentalService;
import de.tum.os.drs.client.mobile.communication.RentalServiceImpl;
import de.tum.os.drs.client.mobile.model.AfterDeviceUpdateAction;
import de.tum.os.drs.client.mobile.model.AfterScanAction;
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

	// Actions to be performed after a certain event
	public AfterScanAction scanAction;
	public AfterDeviceUpdateAction updateAction;
	public String newDeviceImei;

	// The currently selected device and renter
	public Device selectedDevice;
	public Renter selectedRenter;
	
	public String scanResult;

	// The serialized signature as a string
	public String signature;

	public boolean rentingSignature;

	// nav drawer title
	public CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	// Used to store access tokens
	private CredentialStore store;

	// List of fetched devices
	private List<Device> availableDevices;
	private List<Device> rentedDevices;

	private RentalService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

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
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		setTitle(R.string.app_name);
		store = new CredentialStore(
				PreferenceManager.getDefaultSharedPreferences(this));

		service = RentalServiceImpl.getInstance();

		updateAction = AfterDeviceUpdateAction.GO_TO_HOME;
		updateDevices();

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
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
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
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Displaying fragment view for selected nav drawer list item
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
			scanAction = AfterScanAction.OPEN_DEVICE;
			fragment = new ScanFragment();
			break;
		case 3:
			logout();
			break;
		default:
			break;
		}

		if (fragment != null) {

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

	public void startDeviceFragment(boolean putInStack) {

		if (putInStack) {

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, new DeviceFragment())
					.addToBackStack(null).commit();

		} else {

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, new DeviceFragment())
					.commit();
		}

	}

	public void onListUpdateFinished() {

		switch (updateAction) {
		case GO_TO_HOME:
			returnToHome();
			break;
		case OPEN_DEVICE:
			selectAndShowDevice(newDeviceImei);
			break;
		default:
			break;

		}
		
		updateAction = null;

	}

	public void onScanFinished(String result) {

		switch (scanAction) {
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
		
		scanAction = null;

	}

	private void selectAndShowDevice(String imei) {
		Device d = getDeviceFromImei(imei);

		if (d != null) {

			selectedDevice = d;
			startDeviceFragment(false);
		} else {

			// TODO toast
		}

	}

	private Device getDeviceFromImei(String result) {

		List<Device> available = getAvailableDevices();

		for (Device d : available) {

			if (d.getImei().equals(result)) {

				return d;
			}

		}

		List<Device> rented = getRentedDevices();

		for (Device d : rented) {

			if (d.getImei().equals(result)) {

				return d;
			}

		}

		return null;

	}

	public void updateDevices() {

		service.getAvailableDevices(new Callback<List<Device>>() {

			@Override
			public void onSuccess(List<Device> result) {

				availableDevices = result;
				fetchRentedDevices();
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void fetchRentedDevices() {

		service.getRentedDevices(new Callback<List<Device>>() {

			@Override
			public void onSuccess(List<Device> result) {

				rentedDevices = result;
				onListUpdateFinished();
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void clearFragmentStack() {

		FragmentManager fm = this.getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}

	}

	public List<Device> getAvailableDevices() {

		return availableDevices;

	}

	public List<Device> getRentedDevices() {
		return rentedDevices;

	}

	private void logout() {

		Log.i(TAG, "Loging out...");

		RentalServiceImpl.getInstance().logout(new Callback<String>() {

			@Override
			public void onSuccess(final String result) {
				Log.i(TAG, result);

				store.clearCredentials();
				showToast(result);
				startAuthenticationActivity();
				finish();

			}

			@Override
			public void onFailure(int code, String error) {

				Log.i(TAG, error);

				showToast("Error code received: " + code + " " + error);
			}

		});

	}

	public void sessionExpired() {

		finish();
		startAuthenticationActivity();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public void returnToHome() {

		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager
					.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		displayView(0);

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

		if (f instanceof DeviceFragment || f instanceof RentConfirmFragment || f instanceof ReturnConfirmFragment) {

			returnToHome();
			return;

		}

		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

			if (f instanceof HomeFragment) {
				// don't do anything if the current fragment is the
				mDrawerLayout.openDrawer(mDrawerList);
			} else {

				returnToHome();

			}

		} else {

			MainActivity.super.onBackPressed();
		}

	}

	private void showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();

	}

}
