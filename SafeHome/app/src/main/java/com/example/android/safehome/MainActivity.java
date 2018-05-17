package com.example.android.safehome;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView bg1_iv, bg2_iv, settings, lock;
    private Activity activity;
    private SeekBar sb;
    private pl.droidsonroids.gif.GifImageButton gif_upArrow;

//    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

        initiateAttributes();

        animationLock();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        handleBackgrondAnimation();
        takePermissionForTransparentStatusBar();
        unlock();
        handleUpArrow();
        handleSettings();
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
        SeekBar seekBar = findViewById(R.id.below_swiper);
        seekBar.bringToFront();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 25) {
                    seekBar.setProgress(0);
                    Toast.makeText(activity, "open appliances", Toast.LENGTH_LONG).show();

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.secondary_container, new AppliancesFragment());
                    fragmentTransaction.commit();
                    findViewById(R.id.secondary_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.secondary_container).bringToFront();
                    fragmentTransaction.addToBackStack(null);
                }
            }
        });
    }

    private void unlock() {
        sb.bringToFront();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 90) {
                    sb.setProgress(0);
                    Toast.makeText(activity, "Unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void animationLock() {
        ImageView circle1, circle2, circle3;
        circle1 = findViewById(R.id.im_1002);
        circle2 = findViewById(R.id.im_1003);
        circle3 = findViewById(R.id.im_1004);
        Animation circleAnimation1 = AnimationUtils.loadAnimation(this, R.anim.circle1_transition);
        Animation circleAnimation2 = AnimationUtils.loadAnimation(this, R.anim.circle2_transition);
        Animation circleAnimation3 = AnimationUtils.loadAnimation(this, R.anim.circle3_transition);
        circle1.startAnimation(circleAnimation1);
        circle2.startAnimation(circleAnimation2);
        circle3.startAnimation(circleAnimation3);
        circleExpnadAnimation(circle1);
        circleExpnadAnimation(circle2 );
        circleExpnadAnimation(circle3 );

    }

    private void circleExpnadAnimation(ImageView circle) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(circle, "scaleX", 1.0f, 2.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(circle, "scaleY", 1.0f, 2.0f);

        AnimatorSet scaleAnim = new AnimatorSet();
        scaleAnim.setDuration(2000);
        scaleAnim.play(scaleX).with(scaleY);
        scaleAnim.start();
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.RESTART);
        scaleY.setRepeatMode(ObjectAnimator.RESTART);
    }

    private void initiateAttributes() {
        activity = this;
        activity.setTitle("");
        bg1_iv = findViewById(R.id.background_one);
        bg2_iv = findViewById(R.id.background_two);
        bg1_iv.setVisibility(View.VISIBLE);
        lock = findViewById(R.id.im_1001);
        sb = findViewById(R.id.myseek);
        gif_upArrow = findViewById(R.id.gif_up_arrow);

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
        findViewById(R.id.appBarLayout).bringToFront();
        findViewById(R.id.bottom_layout).bringToFront();

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
