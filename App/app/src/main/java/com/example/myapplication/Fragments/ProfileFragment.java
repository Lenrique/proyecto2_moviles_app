package com.example.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {}

    String user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent();
        user = intent.getStringExtra("user");
        if(user != null){
            Toast.makeText(getContext(),"Hay Usuario", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"No Usuario", Toast.LENGTH_LONG).show();
        }
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user != null){
            Toast.makeText(getContext(),"Hay Usuario", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"No Usuario", Toast.LENGTH_LONG).show();
        }
    }
}
