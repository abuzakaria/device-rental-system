package de.tum.os.drs.client.mobile.model;


public enum AfterScanAction {
	OPEN_DEVICE,
	SET_IMEI_FILED;
	
	public static AfterScanAction toScanAction(String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
                // For error cases
            return OPEN_DEVICE;
        }
    }
	
}
