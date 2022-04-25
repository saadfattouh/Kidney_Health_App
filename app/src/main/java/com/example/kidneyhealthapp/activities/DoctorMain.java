package com.example.kidneyhealthapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class DoctorMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener { public Toolbar toolbar;

    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;
    SharedPrefManager prefManager;
    int destination = R.id.menu_appointment_requests;

    AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        prefManager = SharedPrefManager.getInstance(this);

        setupNavigation();
    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.my_drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.menu_appointment_requests, R.id.menu_center_patients, R.id.menu_profile,R.id.menu_my_center, R.id.menu_chat_list).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(this);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.menu_appointment_requests:
                destination = R.id.menu_appointment_requests;
                break;
            case R.id.menu_center_patients:
                destination = R.id.menu_center_patients;
                break;

            case R.id.menu_profile:
                destination = R.id.menu_profile;
                break;

            case R.id.menu_my_center:
                destination = R.id.menu_my_center;
                break;

            case R.id.menu_chat_list:
                destination = R.id.menu_chat_list;
                break;

            case R.id.menu_logout:
                SharedPrefManager.getInstance(this).logout();
                startActivity( new Intent(this, Login.class));
                finish();
                break;
        }

        drawerLayout.closeDrawers();

        return true;
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        navController.navigate(destination);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}