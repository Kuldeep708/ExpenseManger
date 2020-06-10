package com.example.expensemanger;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
       private BottomNavigationView bottomNavigationView;
       private FrameLayout frameLayout;
       private DashFragment dashFragment;
       private ExpenseFragment expenseFragment;
       private IncomeFragment incomeFragment;
       private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView=findViewById(R.id.btmnavigationview);
        frameLayout=findViewById(R.id.mainframe);
        //Fragments
        dashFragment=new DashFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment= new ExpenseFragment();
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navig_drawer_open,R.string.navig_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);
        setFragment(dashFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profile:
                        setFragment(dashFragment);
                       // bottomNavigationView.setItemBackgroundResource(R.color.dash);
                        return true;
                    case R.id.income:
                        setFragment(incomeFragment);
                      //  bottomNavigationView.setItemBackgroundResource(R.color.income);
                        return true;
                    case R.id.expense:
                        setFragment(expenseFragment);
                      //  bottomNavigationView.setItemBackgroundResource(R.color.expense);
                        return true;

                    default:
                        return false;

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void displaySelectedListner(int itemId)
    {
        Fragment fragment=null;
        switch (itemId)
        {
            case R.id.dashboard:
                fragment = new DashFragment();
                break;
            case R.id.income:
                fragment=new IncomeFragment();
                break;
            case R.id.expense:
                fragment=new ExpenseFragment();
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + itemId);
        }
        if(fragment != null)
        {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainframe,fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListner(item.getItemId());
        return true;
    }
    public void setFragment(Fragment fragment)
    {
     FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
     fragmentTransaction.replace(R.id.mainframe,fragment);
     fragmentTransaction.commit();
    }
}
