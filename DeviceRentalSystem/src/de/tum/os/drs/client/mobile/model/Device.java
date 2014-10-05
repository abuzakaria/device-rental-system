package de.tum.os.drs.client.mobile.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import de.tum.os.drs.client.parsers.GsonHelper;

public class Device {

	/**
	 * The IMEI code of the device.
	 */
	@SerializedName("imei")
	String IMEI;

	String name;

	/**
	 * Short description of the device. Could include device specs, more about
	 * the state of the device (like how it came to have a dent or scratched
	 * display) or number/type/nature of available accessories.
	 */
	String description;

	/**
	 * The state in which the device is currently in, e.g. brand new, used but
	 * in good condition, damaged
	 */
	String state;

	/**
	 * The type of the current device: Smartphone, Tablet, Notebook, DesktopPC,
	 * Other
	 */
	DeviceType deviceType;

	/**
	 * Stores the name of a picture of the device.
	 */
	// String pictureName;

	/**
	 * False if the device was rented and not yet brought back. True otherwise.
	 */
	Boolean isAvailable = true;

	/**
	 * The approximate date when the device will be available.
	 */
	Date estimatedReturnDate = new Date();

	public Device(String imei, String name, String description, String state,
			DeviceType deviceType, Date estimatedReturnDate, boolean isAvailable) {

		IMEI = imei;
		this.name = name;
		this.description = description;
		this.state = state;
		this.deviceType = deviceType;
		this.estimatedReturnDate = estimatedReturnDate;
		this.isAvailable = isAvailable;
	}

	public String getImei() {
		return IMEI;
	}

	public void setImei(String imei) {
		IMEI = imei;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public Date getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(Date estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String asJson() {

		return GsonHelper.getGson().toJson(this);

	}

}
