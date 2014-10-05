package de.tum.os.drs.client.mobile.model;

import java.util.List;

public class ReturnRequest {

	private String renterMatrNr;
	private List<String> imeiCodes;
	private String comments;
	private String signatureHTML;

	public ReturnRequest(String renterMatrNr, List<String> imeiCodes,
			String comments, String signatureHTML) {
		super();
		this.renterMatrNr = renterMatrNr;
		this.imeiCodes = imeiCodes;
		this.comments = comments;
		this.signatureHTML = signatureHTML;
	}

	public String getRenterMatrNr() {
		return renterMatrNr;
	}

	public void setRenterMatrNr(String renterMatrNr) {
		this.renterMatrNr = renterMatrNr;
	}

	public List<String> getImeiCodes() {
		return imeiCodes;
	}

	public void setImeiCodes(List<String> imeiCodes) {
		this.imeiCodes = imeiCodes;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSignatureHTML() {
		return signatureHTML;
	}

	public void setSignatureHTML(String signatureHTML) {
		this.signatureHTML = signatureHTML;
	}

}
