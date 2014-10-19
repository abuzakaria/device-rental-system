package de.tum.os.drs.client.mobile.model;

import java.util.Date;
import java.util.List;

import de.tum.os.drs.client.mobile.parsers.GsonHelper;

public class RentRequest {

	String renterMatrNr;

	List<String> devicesImeiCodes;

	Date estimatedReturnDate;

	String eventComments;
	String signatureHTML;

	public RentRequest(String renterMatrNr, List<String> devicesImeiCodes,
			Date estimatedReturnDate, String eventComments, String signatureHTML) {
		super();
		this.renterMatrNr = renterMatrNr;
		this.devicesImeiCodes = devicesImeiCodes;
		this.estimatedReturnDate = estimatedReturnDate;
		this.eventComments = eventComments;
		this.signatureHTML = signatureHTML;
	}

	public String getRenterMatrNr() {
		return renterMatrNr;
	}

	public void setRenterMatrNr(String renterMatrNr) {
		this.renterMatrNr = renterMatrNr;
	}

	public List<String> getDevicesImeiCodes() {
		return devicesImeiCodes;
	}

	public void setDevicesImeiCodes(List<String> devicesImeiCodes) {
		this.devicesImeiCodes = devicesImeiCodes;
	}

	public Date getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(Date estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public String getEventComments() {
		return eventComments;
	}

	public void setEventComments(String eventComments) {
		this.eventComments = eventComments;
	}

	public String getSignatureHTML() {
		return signatureHTML;
	}

	public void setSignatureHTML(String signatureHTML) {
		this.signatureHTML = signatureHTML;
	}

	public String asJson() {

		return GsonHelper.getGson().toJson(this);

	}

}
