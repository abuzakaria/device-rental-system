package de.tum.os.drs.client.mobile.model;

/**
 * Taken from original implementation 
 * 
 */
public enum DeviceType {
	Smartphone(0), Tablet(1), Notebook(2), DesktopPC(3), Other(4);

	private final int index;

	DeviceType(int index) {
		this.index = index;
	}

	public int index() {
		return index;
	}
}
