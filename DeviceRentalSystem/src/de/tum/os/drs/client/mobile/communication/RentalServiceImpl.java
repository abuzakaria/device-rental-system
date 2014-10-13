package de.tum.os.drs.client.mobile.communication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.net.Uri;
import de.tum.os.drs.client.mobile.model.Device;
import de.tum.os.drs.client.mobile.model.LoginRequest;
import de.tum.os.drs.client.mobile.model.LoginResponse;
import de.tum.os.drs.client.mobile.model.RentRequest;
import de.tum.os.drs.client.mobile.model.Renter;
import de.tum.os.drs.client.mobile.model.ReturnRequest;
import de.tum.os.drs.client.parsers.GenericPOSTMethodParser;
import de.tum.os.drs.client.parsers.ListDevicesParser;
import de.tum.os.drs.client.parsers.ListRentersParser;
import de.tum.os.drs.client.parsers.LoginResponseParser;
import de.tum.os.drs.client.parsers.ServerRequestCallback;
import de.tum.os.drs.client.parsers.SingleDeviceParser;

public class RentalServiceImpl implements RentalService {

	private static final String BASE_URL = "http://192.168.0.101:8080";
	private static final String BASE_PATH = "/rental-server-mobile/rest/";

	// The singleton instance
	private static RentalService theInstance = null;

	public static RentalService getInstance() {

		if (theInstance == null) {
			theInstance = new RentalServiceImpl();
		}

		return theInstance;
	}

	// private for singleton pattern
	private RentalServiceImpl() {

	}

	// -------------------Devices--------------------

	@Override
	public void addDevice(Device device, Callback<String> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath("add");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			request.setJson(device.asJson());

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getAllDevices(Callback<List<Device>> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath("all");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<List<Device>> serverCallback = new ListDevicesParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getAvailableDevices(Callback<List<Device>> callback) {
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath("available");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<List<Device>> serverCallback = new ListDevicesParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getRentedDevices(Callback<List<Device>> callback) {
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath("rented");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<List<Device>> serverCallback = new ListDevicesParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void updateDevice(String Imei, Device device,
			Callback<String> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath("update");
		builder.appendPath(Imei);

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			request.setJson(device.asJson());

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getDeviceByImei(String Imei, Callback<Device> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("devices");
		builder.appendPath(Imei);

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<Device> serverCallback = new SingleDeviceParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	// -----------------Renters----------------------

	@Override
	public void addRenter(Renter renter, Callback<String> callback) {
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("renters");
		builder.appendPath("add");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			request.setJson(renter.asJson());

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void updateRenter(String mtrNr, Renter renter,
			Callback<String> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("renters");
		builder.appendPath("update");
		builder.appendPath(mtrNr);

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			request.setJson(renter.asJson());

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getAllRenters(Callback<List<Renter>> callback) {
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("renters");
		builder.appendPath("all");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<List<Renter>> serverCallback = new ListRentersParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void getAllActiveRenters(Callback<List<Renter>> callback) {
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("renters");
		builder.appendPath("active");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<List<Renter>> serverCallback = new ListRentersParser(
					callback);

			ServerRequest request = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(request);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void rentDevices(RentRequest request, Callback<String> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("rental-service");
		builder.appendPath("rent");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest serverRequest = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			serverRequest.setJson(request.asJson());

			new ServerCommunication(serverCallback).execute(serverRequest);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void returnDevices(ReturnRequest request, Callback<String> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("rental-service");
		builder.appendPath("return");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(
					callback);

			ServerRequest serverRequest = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			serverRequest.setJson(request.asJson());

			new ServerCommunication(serverCallback).execute(serverRequest);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void login(LoginRequest request, Callback<LoginResponse> callback) {

		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("session");
		builder.appendPath("login");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<LoginResponse> serverCallback = new LoginResponseParser(
					callback);

			ServerRequest serverRequest = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.POST);

			serverRequest.setJson(request.asJson());

			new ServerCommunication(serverCallback).execute(serverRequest);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void logout(Callback<String> callback) {
		
		Uri.Builder builder = getBaseBuilder();
		builder.appendPath("session");
		builder.appendPath("logout");

		URL url;

		try {
			url = new URL(builder.build().toString());

			ServerRequestCallback<String> serverCallback = new GenericPOSTMethodParser(callback);

			ServerRequest serverRequest = new ServerRequest(url,
					ServerRequest.HTTP_METHODS.GET);

			new ServerCommunication(serverCallback).execute(serverRequest);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

		
	}

	private Uri.Builder getBaseBuilder() {

		Uri.Builder b = Uri.parse(BASE_URL).buildUpon();
		b.path(BASE_PATH);

		return b;

	}

}
