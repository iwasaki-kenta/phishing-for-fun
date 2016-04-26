package com.dranithix.fishackathon;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dranithix.fishackathon.util.CameraSource;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Typeface boldFont, semiboldFont;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.sign)
    TextView sign;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.myItems)
    TextView myItems;
    @Bind(R.id.circle_count)
    TextView circle_count;
    @Bind(R.id.addItem)
    TextView friends;
    @Bind(R.id.explore)
    TextView explore;
    @Bind(R.id.lostItems)
    TextView favourites;

    @Bind(R.id.fab)
    FloatingActionMenu fab;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.camera_view)
    SurfaceView cameraView;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    Snackbar detectMessage;
    int numDetected = 0;

    Animation shakeAnimation;

    String gearData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boldFont = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/OpenSans-Bold.ttf");
        semiboldFont = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/OpenSans-Semibold.ttf");

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        Dexter.initialize(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        title.setTypeface(boldFont);
        sign.setTypeface(semiboldFont);
        myItems.setTypeface(semiboldFont);
        circle_count.setTypeface(boldFont);
        friends.setTypeface(semiboldFont);
        explore.setTypeface(semiboldFont);
        favourites.setTypeface(semiboldFont);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Camera permission")
                        .withMessage("Camera permission is needed to analyze ghost gear.")
                        .withButtonText(android.R.string.ok)
                        .build();

        Dexter.checkPermission(dialogPermissionListener, Manifest.permission.CAMERA);

        detectMessage = Snackbar.make(fab, "", Snackbar.LENGTH_INDEFINITE);
        detectMessage.setAction("Action", null);

        // Get camera view's actual width & height in pixels then initialize it!
        cameraView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the width and height
                int width = cameraView.getWidth();
                int height = cameraView.getHeight();

                barcodeDetector = new BarcodeDetector.Builder(MainActivity.this).setBarcodeFormats(Barcode.QR_CODE).build();
                cameraSource = new CameraSource.Builder(MainActivity.this, barcodeDetector).setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO).build();

                cameraView.getHolder().addCallback(new CameraSurface());

                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                        if (barcodes.size() != numDetected) {
                            if ((numDetected = barcodes.size()) > 0) {
                                gearData = barcodes.get(barcodes.keyAt(0)).rawValue;
                                runOnUiThread(new Runnable() {
                                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        detectMessage.setText("Found " + String.valueOf(barcodes.size()) + " item(s)!");
                                        fab.getMenuIconView().startAnimation(shakeAnimation);

                                        if (!detectMessage.isShownOrQueued())
                                            detectMessage.show();
                                    }
                                });

                            } else {
                                gearData = null;
                                runOnUiThread(new Runnable() {
                                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        detectMessage.dismiss();
                                        fab.getMenuIconView().clearAnimation();
                                    }
                                });

                            }
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

    @OnClick(R.id.reportFoundButton)
    public void reportFoundGear() {
        // Gear found through QR!
        Intent intent = new Intent(this, ReportFoundActivity.class);
        if (gearData != null) {
            intent.putExtra("id", gearData);
        }

        startActivity(intent);
    }

    @OnClick(R.id.explore)
    public void exploreTest() {
        Intent test = new Intent(this, GearDetailsActivity.class);
        startActivity(test);

        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.myItems)
    public void viewMyItems() {
        Intent intent = new Intent(this, MyItemsActivity.class);
        startActivity(intent);

        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.addItem)
    public void addAnItem() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);

        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.fab)
    public void handleFab(View view) {

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu; this adds items to the action bar if it is present.
          getMenuInflater().inflate(R.menu.main, menu);
          return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.
          int id = item.getItemId();

          //noinspection SimplifiableIfStatement
          if (id == R.id.action_settings) {
              return true;
          }

          return super.onOptionsItemSelected(item);
      }
  */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
