package garin.artemiy.simplescanner.library.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.view.*;

/**
 * Author: Artemiy Garin
 * Date: 16.10.13
 */
@SuppressWarnings("CanBeFinal")
public class SimpleCameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int DEGREES_0 = 0;
    private static final int DEGREES_90 = 90;
    private static final int DEGREES_180 = 180;
    private static final int DEGREES_270 = 270;

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.PreviewCallback previewCallback;
    private Display display;

    @SuppressWarnings({"deprecation", "ConstantConditions"})
    public SimpleCameraView(Context context, Camera.PreviewCallback previewCallback) {
        super(context);
        this.previewCallback = previewCallback;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        setKeepScreenOn(true);

        configureCamera(getResources().getConfiguration());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startCamera();
    }

    public Camera getCamera() {
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    @SuppressWarnings({"ConstantConditions"})
    public boolean configureCamera(Configuration configuration) {
        try {
            getCamera();
            if (camera != null) {
                int width = getScreenWidth();
                int height = getScreenHeight();

                int displayOrientationDegrees = getDisplayOrientationDegrees(display);
                camera.setDisplayOrientation(displayOrientationDegrees);

                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                float aspect = (float) previewSize.width / previewSize.height;

                ViewGroup.LayoutParams cameraHolderParams = getLayoutParams();
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    cameraHolderParams.height = height;
                    cameraHolderParams.width = (int) (height / aspect);
                } else {
                    cameraHolderParams.width = width;
                    cameraHolderParams.height = (int) (width / aspect);
                }

                setLayoutParams(cameraHolderParams);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    private int getScreenWidth() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2)
            return display.getWidth();
        else {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
    }

    @SuppressWarnings("deprecation")
    private int getScreenHeight() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2)
            return display.getHeight();
        else {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private int getDisplayOrientationDegrees(Display display) {
        int displayOrientationDegrees;
        int orientation = getResources().getConfiguration().orientation;

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    displayOrientationDegrees = DEGREES_90;
                else displayOrientationDegrees = DEGREES_0;
                break;
            case Surface.ROTATION_90:
                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    displayOrientationDegrees = DEGREES_0;
                else displayOrientationDegrees = DEGREES_270;
                break;
            case Surface.ROTATION_180:
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    displayOrientationDegrees = DEGREES_270;
                else displayOrientationDegrees = DEGREES_180;
                break;
            case Surface.ROTATION_270:
                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    displayOrientationDegrees = DEGREES_180;
                else displayOrientationDegrees = DEGREES_90;
                break;
            default:
                displayOrientationDegrees = DEGREES_0;
        }

        return displayOrientationDegrees;
    }

    @SuppressWarnings("WeakerAccess")
    public void stopCamera() {
        try {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void startCamera() {
        try {
            if (surfaceHolder.getSurface() == null) return;

            camera.reconnect();
            camera.setPreviewDisplay(surfaceHolder);
            if (previewCallback != null) camera.setPreviewCallback(previewCallback);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
