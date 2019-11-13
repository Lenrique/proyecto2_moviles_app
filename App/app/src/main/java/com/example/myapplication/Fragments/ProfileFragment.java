package com.example.myapplication.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.PostsRecyclerViewConfig;
import com.example.myapplication.DAO.DAOPost;
import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private String userEmail;
    private TextView userNameTextView;
    private RecyclerView userPostsRecylerView;
    private CircleImageView profile_image;
    public ProfileFragment() {}

    String user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userEmail = getArguments().getString("userEmail");
        update();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        profile_image = view.findViewById(R.id.profile_image);
        userPostsRecylerView = view.findViewById(R.id.userPostsRecylerView);
        update();

    }
    public void setImage(String url){
        Picasso.get().load(url).into(profile_image);
    }

    public void update(){
        try {
            Context context = getActivity();
            SharedPreferences sharedPref = context.getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);

            Toast.makeText(getContext(), sharedPref.getString("userEmail",""), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
        }

        DAOUser.getInstance().getUser(userEmail, new DAOUser.UserStatus() {
            @Override
            public void onSuccess(DTOUser user) {
                userNameTextView.setText(user.userInfo.name+" " +user.userInfo.lastName);
            }

            @Override
            public void onFailure() {

            }
        });
        DAOPost.getInstance().getAllPost(new DAOPost.PostReturn() {
            @Override
            public void onSuccess(Task<QuerySnapshot> task) {
                List<DTOPost> posts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    posts.add(document.toObject(DTOPost.class));
                }
                new PostsRecyclerViewConfig().setConfig(userPostsRecylerView,getContext(), posts);
            }
        });
        DAOUser.getInstance().getUser(userEmail, new DAOUser.UserStatus() {
            @Override
            public void onSuccess(DTOUser user) {
                setImage(user.userInfo.profilePhoto);
            }

            @Override
            public void onFailure() {

            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        update();
        Toast.makeText(getContext(), "errorResume", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
        Toast.makeText(getContext(), "errorStart", Toast.LENGTH_LONG).show();
    }
}
