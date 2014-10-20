package de.tum.os.drs.client.mobile.model;

/**
 * Action to be performed after the signatur has been fetched
 * 
 * @author pablo
 *
 */
public enum AfterSignatureAction {
	//Go to the rental confirmation fragment
	OPEN_RENT_CONFIRM,
	//Go to the return confirmation fragment
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
