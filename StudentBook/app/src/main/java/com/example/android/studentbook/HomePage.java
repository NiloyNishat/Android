package com.example.android.studentbook;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {
    DrawerLayout navBar;
    ActionBarDrawerToggle drawerBar;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navBar = (DrawerLayout) findViewById(R.id.nav_drawer);
        drawerBar = new ActionBarDrawerToggle(this, navBar, R.string.open, R.string.close);
        navBar.addDrawerListener(drawerBar);
        drawerBar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragement());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Homepage");
        selectDrawer();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerBar.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void selectDrawer(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item1){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new Images());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Image List");
                    item.setChecked(true);
                }
                else if(item.getItemId() == R.id.item2){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new Info());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Info List");
                    item.setChecked(true);
                }
                else if(item.getItemId() == R.id.item0){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new HomeFragement());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Homepage");
                    item.setChecked(true);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }




}















