package de.tum.os.drs.client.mobile.model;

public enum AfterSignatureAction {
	OPEN_RENT_CONFIRM,
	OPEN_RETURN_CONFIRM;
	
	public static AfterSignatureAction toScanAction(String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
                // For error cases
            return OPEN_RENT_CONFIRM;
        }
    }

}
