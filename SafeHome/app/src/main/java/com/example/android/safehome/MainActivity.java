package com.example.android.safehome;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView bg1_iv, bg2_iv, settings, lock_st, lock;
    private Activity activity;
    private SeekBar sb;
    private pl.droidsonroids.gif.GifImageButton gif_upArrow;
    private TextView notice;
    private AppBarLayout appBarLayout_appliances;
    private TouchToUnLockView mUnlockView;
    private View mContainerView;

//    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

        initiateAttributes();

//        animationLock();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        handleBackgrondAnimation();
        takePermissionForTransparentStatusBar();
//        unlock();
        handleUpArrow();
        handleRipple();
        initView();

        handleSettings();
        handleApplianceToolbar();
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
//                    fragmentTransaction.setCustomAnimations( R.animator.slide_up, 0, 0, R.animator.slide_down);

                fragmentTransaction.replace(R.id.secondary_container, new AppliancesFragment());

                fragmentTransaction.commit();
                findViewById(R.id.secondary_container).setVisibility(View.VISIBLE);
                findViewById(R.id.secondary_container).bringToFront();
                fragmentTransaction.addToBackStack(null);
            }
        });
    }

    private void initView() {
//        mContainerView.bringToFront();
        mUnlockView.bringToFront();
        findViewById(R.id.appBarLayout).bringToFront();
        findViewById(R.id.bottom_layout).bringToFront();
        mUnlockView.setOnTouchToUnlockListener(new TouchToUnLockView.OnTouchToUnlockListener() {
            @Override
            public void onTouchLockArea() {
//                if (mContainerView != null) {
//                    mContainerView.setBackgroundColor(Color.parseColor("#66000000"));
//                }
            }

            @Override
            public void onSlidePercent(float percent) {
                if (mContainerView != null) {
//                    mContainerView.setAlpha(1 - percent < 0.05f ? 0.05f : 1 - percent);
//                    mContainerView.setScaleX(1 + (percent > 1f ? 1f : percent) * 0.08f);
//                    mContainerView.setScaleY(1 + (percent > 1f ? 1f : percent) * 0.08f);
                }
            }

            @Override
            public void onSlideToUnlock() {
                Toast.makeText(activity, "unlocked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSlideAbort() {
//                if (mContainerView != null) {
//                    mContainerView.setAlpha(1.0f);
//                    mContainerView.setBackgroundColor(0);
//                    mContainerView.setScaleX(1f);
//                    mContainerView.setScaleY(1f);
//                }
            }
        });

    }


    private void initiateAttributes() {
        activity = this;
        activity.setTitle("");
        bg1_iv = findViewById(R.id.background_one);
        bg2_iv = findViewById(R.id.background_two);
        bg1_iv.setVisibility(View.VISIBLE);
//        lock = findViewById(R.id.im_1001);
//        sb = findViewById(R.id.myseek);
        gif_upArrow = findViewById(R.id.gif_up_arrow);
//        notice = findViewById(R.id.tv_slideToUnlock);
        appBarLayout_appliances = findViewById(R.id.appBarLayout_appliances);

        mContainerView = findViewById(R.id.relel_ContentContainer);

//        mUnlockView = ViewUtils.get(this, R.id.tulv_UnlockView);
        mUnlockView = findViewById(R.id.tulv_UnlockView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.top_menu_settings_icon);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        toolbar.setOverflowIcon(d);


    }

    private void handleBackgrondAnimation() {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
