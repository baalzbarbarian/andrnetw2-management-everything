package com.practice.andr_networking_asm;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.practice.andr_networking_asm.controller.IntentController;
import com.practice.andr_networking_asm.controller.SessionManager;
import com.practice.andr_networking_asm.ui.user.LoginActivity;
import com.practice.andr_networking_asm.ui.user.UpdateUser.UpdateUserFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public String name, email, imageLink;
    private TextView txtFname, txtEmail, txtLogout;
    private ImageView imgUser;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundDrawable(getDrawable(R.drawable.bg_login));
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);

        name = sessionManager.getNameSaved();
        email = sessionManager.getMailSaved();
        imageLink = sessionManager.getImageLink();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        txtFname = headerView.findViewById(R.id.nav_fullname);
        txtEmail = headerView.findViewById(R.id.nav_email);
        txtFname.setText(name);
        txtEmail.setText(email);
        imgUser = headerView.findViewById(R.id.nav_image);
        setImage();

        txtLogout = findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLogin(null, null, null,  null, false);
                IntentController.directinal(MainActivity.this, LoginActivity.class);
                finish();
            }
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add, R.id.nav_home, R.id.nav_blabla)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.updateUser:
                openUpdateUserFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void openUpdateUserFragment(){
        Fragment myFragment = new UpdateUserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
    }

    private void setImage(){
        if(imageLink.equals("")){
            imgUser.setImageDrawable(getDrawable(R.drawable.imagedefault));
        }else {
            Glide.with(this).load(imageLink).into(imgUser);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}