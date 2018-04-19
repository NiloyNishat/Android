package com.example.android.contacts;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.apache.commons.io.FileUtils;


public class Homepage extends AppCompatActivity{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tabLayout = findViewById(R.id.tabLayout_homepage);
        viewPager = findViewById(R.id.viewPager_homepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrgament(new FragmentUser(), "");
        adapter.addFrgament(new FragmentContacts(), "");
        adapter.addFrgament(new FragmentCall(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_web);

        if(getIntent().getStringExtra("page") != null && getIntent().getStringExtra("page").equals("3")){
            viewPager.setCurrentItem(2,true);
        }
        else{
            viewPager.setCurrentItem(0,true);
        }

    }

    @Override
    public void onBackPressed() {
        FileUtils.deleteQuietly(this.getCacheDir());
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }

    @Override
    public void finish() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }


}
