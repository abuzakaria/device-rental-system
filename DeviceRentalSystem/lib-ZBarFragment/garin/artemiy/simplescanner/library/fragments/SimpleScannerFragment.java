package garin.artemiy.simplescanner.library.fragments;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import garin.artemiy.simplescanner.library.listeners.ScannerListener;
import garin.artemiy.simplescanner.library.views.SimpleCameraView;
import net.sourceforge.zbar.*;

/**
 * Author: Artemiy Garin
 * Date: 16.10.13
 */
@SuppressWarnings("CanBeFinal")
public class SimpleScannerFragment extends Fragment {

    private static final String Z_BAR_LIBRARY = "iconv";
    private static final String GREY_COLOR_SPACE = "Y800";

    private static final long AUTOFOCUS_REFRESH_DELAY = 30;
    private static final long CONFIGURE_DELAY = 50;

    private ImageScanner scanner;
    private SimpleCameraView cameraView;
    private PackageManager packageManager;

    private Handler configurationHandler = new Handler();
    private Handler autoFocusHandler = new Handler();

    private Runnable reconfigureRunnable = new CustomConfigureRunnable();
    private Runnable runAutoFocus = new CustomAutoFocusRunnable();
    private Camera.PreviewCallback previewCallback = new CustomPreviewCallback();
    private Camera.AutoFocusCallback autoFocusCallback = new CustomAutoFocusCallback();

    private ScannerListener scannerListener;

    static {
        System.loadLibrary(Z_BAR_LIBRARY);
    }

    public void setScannerListener(ScannerListener scannerListener) {
        this.scannerListener = scannerListener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
    }
/*
    @Override
    public void onPause() {
        super.onPause();
        try {
            cameraView.stopCamera();
            stopAutofocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        try {
            configureCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAutofocus() {
        if (isHaveAutoFocus() && cameraView.getCamera() != null) {
            autoFocusHandler.removeCallbacks(runAutoFocus);
            cameraView.getCamera().cancelAutoFocus();
        }
    }

    private void startAutofocus() {
        if (isHaveAutoFocus()) {
            autoFocusHandler.postDelayed(runAutoFocus, AUTOFOCUS_REFRESH_DELAY);
            cameraView.getCamera().autoFocus(autoFocusCallback);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureCamera();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraView = new SimpleCameraView(inflater.getContext(), previewCallback);
        return cameraView;
    }

    private boolean isHaveAutoFocus() {
        if (packageManager == null) packageManager = getActivity().getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    private void configureCamera() {
        configurationHandler.postDelayed(reconfigureRunnable, CONFIGURE_DELAY);
    }

    private class CustomAutoFocusCallback implements Camera.AutoFocusCallback {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(runAutoFocus, AUTOFOCUS_REFRESH_DELAY);
        }
    }

    private class CustomAutoFocusRunnable implements Runnable {
        public void run() {
            try {
                if (cameraView != null && cameraView.getCamera() != null && isHaveAutoFocus())
                    cameraView.getCamera().autoFocus(autoFocusCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CustomConfigureRunnable implements Runnable {
        @Override
        public void run() {
            try {
                boolean isConfigured = cameraView.configureCamera(getResources().getConfiguration());

                if (!isConfigured) {
                    configurationHandler.postDelayed(this, CONFIGURE_DELAY);
                    cameraView.stopCamera();
                } else {
                    configurationHandler.removeCallbacks(this);
                    cameraView.startCamera();
                    startAutofocus();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CustomPreviewCallback implements Camera.PreviewCallback {

        @SuppressWarnings("ConstantConditions")
        public void onPreviewFrame(byte[] data, Camera incomingCamera) {
            try {
                Camera.Parameters cameraParameters = incomingCamera.getParameters();
                Camera.Size previewSize = cameraParameters.getPreviewSize();
                Image barcode = new Image(previewSize.width, previewSize.height, GREY_COLOR_SPACE);
                barcode.setData(data);

                if (scanner.scanImage(barcode) != 0) {
                    SymbolSet scannerResults = scanner.getResults();


                    for (Symbol symbol : scannerResults)
                        if (scannerListener == null){
                            Toast.makeText(getActivity(), symbol.getData(), Toast.LENGTH_LONG).show();
                    		break;
                    		}
                        else
                            scannerListener.onDataReceive(symbol.getData(), symbol.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
