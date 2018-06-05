package com.example.android.safehome.UIHandler;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.safehome.Controller.DoorControl;
import com.example.android.safehome.R;
import com.skyfishjy.library.RippleBackground;

import org.eclipse.paho.client.mqttv3.MqttException;

import dmax.dialog.SpotsDialog;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView bg1_iv, bg2_iv, settings, lock_st, lock;
    private Activity activity;
    private pl.droidsonroids.gif.GifImageButton gif_upArrow;
    DoorControl doorControl;

    private AppBarLayout appBarLayout_appliances;
    public static Boolean toastFlag = false;
    private TouchToUnLockView mUnlockView;
    DrawerLayout drawer;
    private View mContainerView;
    public static int flag = -1;
    int i=1;

//    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

        initiateAttributes();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        handleBackgroundAnimation();
        takePermissionForTransparentStatusBar();

        handleUpArrow();
        handleRipple();
        initView();


        handleSettings();
        handleApplianceToolbar();
    }

    private void handleClient() {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doorControl.destroyClient();
                try {
                    doorControl.reconnect();
                    doorControl = new DoorControl(activity, false);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Log.d("rebuild", Integer.toString(i++));

            }
        },0);
    }

    private void handleRipple() {
        final RippleBackground rippleBackground= findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
    }

    private void handleApplianceToolbar() {

    }

    private void handleSettings() {
        settings = findViewById(R.id.bt_upper_fragment);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                Toast.makeText(activity, "open appliances", Toast.LENGTH_LONG).show();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.secondary_container, new SettingsFragment());
                fragmentTransaction.commit();
                findViewById(R.id.secondary_container).setVisibility(View.VISIBLE);
                findViewById(R.id.secondary_container).bringToFront();

                fragmentTransaction.addToBackStack(null);
            }
        });
    }

    private void handleUpArrow() {
        gif_upArrow = findViewById(R.id.gif_up_arrow);
        gif_upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "open appliances", Toast.LENGTH_LONG).show();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.secondary_container, new AppliancesFragment());

                fragmentTransaction.commit();
                findViewById(R.id.secondary_container).setVisibility(View.VISIBLE);
                findViewById(R.id.secondary_container).bringToFront();
                fragmentTransaction.addToBackStack(null);
            }
        });
    }

    private void initView() {

        doorControl = new DoorControl(activity, true);

        findViewById(R.id.appBarLayout).bringToFront();
        findViewById(R.id.bottom_layout).bringToFront();


        mUnlockView.setOnTouchToUnlockListener(new TouchToUnLockView.OnTouchToUnlockListener() {
            @Override
            public void onTouchLockArea() {
            }

            @Override
            public void onSlidePercent(float percent) {
                if (mContainerView != null) {
                }
            }

            @Override
            public void onSlideToUnlock() {
//                Toast.makeText(activity, "unlocked", Toast.LENGTH_SHORT).show();
                handleProgressbar();
            }

            private void handleProgressbar() {

                @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> progressBarAsyncTask = new AsyncTask<String, String, String>() {

                    AlertDialog dialogue = new SpotsDialog(activity, R.style.Custom);
                    @Override
                    protected void onPreExecute() {
                        this.dialogue.setCancelable(false);
                        this.dialogue.show();
                        handleClient();
                    }

                    @Override
                    protected String doInBackground(String... strings) {

                        doorControl.handleDoorLock();
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {

                        if ( dialogue!=null && dialogue.isShowing() ){
                            dialogue.dismiss();
                        }
                        Log.d("mess main flag", Integer.toString(flag));
                        if (flag == -1){
                            Toast.makeText(activity, "Device unavailable", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(flag == 1){
                                Toast.makeText(activity, "Unlocked", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                };
                progressBarAsyncTask.execute();
                flag = doorControl.fl;
                Log.d("mess main flag", Integer.toString(flag));

            }

            @Override
            public void onSlideAbort() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doorControl.destroyClient();
    }

    private void initiateAttributes() {
        activity = this;
        activity.setTitle("");
        bg1_iv = findViewById(R.id.background_one);
        bg2_iv = findViewById(R.id.background_two);
        bg1_iv.setVisibility(View.VISIBLE);

        gif_upArrow = findViewById(R.id.gif_up_arrow);
        appBarLayout_appliances = findViewById(R.id.appBarLayout_appliances);

        mContainerView = findViewById(R.id.relel_ContentContainer);
        mUnlockView = findViewById(R.id.tulv_UnlockView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
//        if(drawer.isShown()) drawer.bringToFront();
        toggle.syncState();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.top_menu_settings_icon);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        toolbar.setOverflowIcon(d);


    }

    private void handleBackgroundAnimation() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(40000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg1_iv.getWidth();
                final float translationX = width * progress;
                bg1_iv.setTranslationX(translationX - width);
                bg2_iv.setTranslationX(translationX);
                animator.setRepeatMode(ValueAnimator.REVERSE);
            }
        });
        animator.start();
    }

    private void takePermissionForTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item0) {
            // Handle the camera action
        }
        else if (id == R.id.item1) {

        }
        else if (id == R.id.item3) {

        }
        else if (id == R.id.item4) {

        }
        else if (id == R.id.item5) {

        }
        else if (id == R.id.item6) {

        }
        else if (id == R.id.item7) {

        }
        else if (id == R.id.item8) {

        }
        else if (id == R.id.item9) {

        }
//        drawer.bringToFront();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
