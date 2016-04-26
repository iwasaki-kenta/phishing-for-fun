package com.dranithix.fishackathon;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import com.dranithix.fishackathon.util.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrActivity extends AppCompatActivity {
    public static final int QR_CODE_FOUND = 1;
    @Bind(R.id.camera_view)
    SurfaceView cameraView;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);

        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Camera permission")
                        .withMessage("Camera permission is needed to analyze ghost gear.")
                        .withButtonText(android.R.string.ok)
                        .build();

        Dexter.checkPermission(dialogPermissionListener, Manifest.permission.CAMERA);

        // Get camera view's actual width & height in pixels then initialize it!
        cameraView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the width and height
                int width = cameraView.getWidth();
                int height = cameraView.getHeight();

                barcodeDetector = new BarcodeDetector.Builder(QrActivity.this).setBarcodeFormats(Barcode.QR_CODE).build();
                cameraSource = new CameraSource.Builder(QrActivity.this, barcodeDetector).setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO).build();

                cameraView.getHolder().addCallback(new CameraSurface());

                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                        if (barcodes.size() > 0) {
                            System.out.println(barcodes.get(barcodes.keyAt(0)).rawValue);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent data = new Intent();
                                    data.putExtra("result", barcodes.get(barcodes.keyAt(0)).rawValue);
                                    setResult(QR_CODE_FOUND, data);

                                    finish();
                                }
                            });
                        }

                    }
                });

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    cameraView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    cameraView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private class CameraSurface implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                cameraSource.start(cameraView.getHolder());
            } catch (IOException ie) {
                Log.e("CAMERA SOURCE", ie.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            cameraSource.stop();
        }
    }

}
