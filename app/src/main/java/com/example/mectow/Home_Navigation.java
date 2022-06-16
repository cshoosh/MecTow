package com.example.mectow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.mectow.R.id;
import com.example.mectow.ui.about_us.AboutFragment;
import com.example.mectow.ui.contact_us.ContactFragment;
import com.example.mectow.ui.history.HistoryFragment;
import com.example.mectow.ui.home.HomeFragment;
import com.example.mectow.ui.notification.NotificationFragment;
import com.example.mectow.ui.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home_Navigation extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Fragment temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_history2, R.id.nav_notification,R.id.nav_profile,R.id.nav_contact_us,R.id.nav_about_us,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  id.nav_logout:
                    {
                        showPopup();
                        return true;

                    }

                    case id.nav_home:
                    {
                        Toast.makeText(Home_Navigation.this,"Home",Toast.LENGTH_SHORT).show();
                        temp=new HomeFragment();
                        break;
                    }
                    case id.nav_about_us:
                    {
                        Toast.makeText(Home_Navigation.this,"About Us",Toast.LENGTH_SHORT).show();
                        temp=new AboutFragment();
                        break;
                    }
                    case id.nav_history2:
                    {
                        Toast.makeText(Home_Navigation.this,"History",Toast.LENGTH_SHORT).show();
                        temp=new HistoryFragment();
                        break;
                    }
                    case id.nav_contact_us:
                    {
                        Toast.makeText(Home_Navigation.this,"Contact Us",Toast.LENGTH_SHORT).show();
                        temp=new ContactFragment();
                        break;
                    }
                    case id.nav_notification:
                    {
                        Toast.makeText(Home_Navigation.this,"Notification",Toast.LENGTH_SHORT).show();
                        temp=new NotificationFragment();
                        break;
                    }
                    case id.nav_profile:
                    {
                        Toast.makeText(Home_Navigation.this,"Profile",Toast.LENGTH_SHORT).show();
                        temp=new ProfileFragment();
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(id.nav_host_fragment,temp).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home__navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Home_Navigation.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        logout(); // Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void logout() {
        startActivity(new Intent(this,login.class));
        Toast.makeText(Home_Navigation.this,"Logged Out",Toast.LENGTH_SHORT).show();
        finish();
    }


}