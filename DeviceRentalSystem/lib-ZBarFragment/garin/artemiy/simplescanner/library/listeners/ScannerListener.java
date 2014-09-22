package garin.artemiy.simplescanner.library.listeners;

/**
 * Author: Artemiy Garin
 * Date: 17.10.13
 */
public interface ScannerListener {

    public void onDataReceive(String data, int barcodeType);

}
