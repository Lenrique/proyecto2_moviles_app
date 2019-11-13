package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Adapters.PageAdapter;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Bundle bundle = new Bundle();
    TabLayout tabLayout;
    public static ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem timeLineTab, searchTab, profileTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle.putString("userEmail", getIntent().getStringExtra("userEmail"));
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentUserEmail", getIntent().getStringExtra("userEmail"));
        editor.apply();

        initComponents();
        initListeners();


    }

    private void initComponents(){
        tabLayout = findViewById(R.id.tabLayout);
        timeLineTab = findViewById(R.id.timeLineTab);
        searchTab = findViewById(R.id.searchTab);
        profileTab = findViewById(R.id.profileTab);
        viewPager = findViewById(R.id.viewPager);


        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), bundle);
        viewPager.setAdapter(pageAdapter);

    }
    private void initListeners(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


}
