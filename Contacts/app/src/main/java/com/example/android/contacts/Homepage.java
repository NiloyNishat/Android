package com.example.android.contacts;

import android.content.Intent;
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

public class Homepage extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private RecylcerViewAdapter recylcerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_homepage);
        viewPager = (ViewPager) findViewById(R.id.viewPager_homepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrgament(new FragmentContacts(), "");
        adapter.addFrgament(new FragmentCall(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_contact);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        FileUtils.deleteQuietly(this.getCacheDir());
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Contact> newList = new ArrayList<>();

        for(Contact contact : FragmentContacts.contactList){
            String name = contact.name.toLowerCase();
            if(name.contains(newText)){
                newList.add(contact);
            }
            FragmentContacts.recylcerViewAdapter.setFilter(newList);

        }
        return false;
    }
}
