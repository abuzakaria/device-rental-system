package com.example.devicerentalsystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import src.com.github.gcacace.signaturepad.views.SignaturePad;

public class RentFragment extends Fragment {
	private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
	public RentFragment(){}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rent, container, false);
        mSignaturePad = (SignaturePad) rootView.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });
        mClearButton = (Button) rootView.findViewById(R.id.clear_button);
        mSaveButton = (Button) rootView.findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if(addSignatureToGallery(signatureBitmap)) {
                    Log.d("sign", "Signature saved into the Gallery");
                } else {
                	Log.d("sign", "Unable to store the signature");
                }
            }
        });
        return rootView;
    }
	
	public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("Device-rental-system"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToPNG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
