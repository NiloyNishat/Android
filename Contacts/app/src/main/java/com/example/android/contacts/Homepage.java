package com.example.android.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private RecylcerViewAdapter recylcerViewAdapter;
    static int tabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_homepage);
        viewPager = (ViewPager) findViewById(R.id.viewPager_homepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrgament(new FragmentUser(), "");
        adapter.addFrgament(new FragmentContacts(), "");
        adapter.addFrgament(new FragmentCall(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_web);
        viewPager.setCurrentItem(0,true);

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
