package de.tum.os.drs.client.mobile.model;

import java.util.List;

import de.tum.os.drs.client.parsers.GsonHelper;

public class Renter {

	/**
	 * Full name of the person having rented a device.
	 */
	String name;

	String matriculationNumber;

	String email;

	String phoneNumber;

	String comments;

	/**
	 * Stores a list of the IMEI codes of the rented devices
	 */

	List<String> rentedDevices;

	public Renter(String name, String matriculationNumber, String email,
			String phoneNumber, String comments, List<String> rentedDevices) {

		this.name = name;
		this.matriculationNumber = matriculationNumber;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.comments = comments;
		this.rentedDevices = rentedDevices;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatriculationNumber() {
		return matriculationNumber;
	}

	public void setMatriculationNumber(String matriculationNumber) {
		this.matriculationNumber = matriculationNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<String> getRentedDevices() {
		return rentedDevices;
	}

	public void setRentedDevices(List<String> rentedDevices) {
		this.rentedDevices = rentedDevices;
	}

	public String asJson() {

		return GsonHelper.getGson().toJson(this);

	}

}
