package com.example.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.Activities.NewPublicationActivity;
import com.example.myapplication.Adapters.PostsRecyclerViewConfig;
import com.example.myapplication.DAO.DAOPost;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {

    private String userEmail;
    private Button  publicarButton;
    private RecyclerView postRecyclerView;
    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userEmail = getArguments().getString("userEmail");
        //userEmail = getActivity().getIntent().getExtras().getString("userEmail");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line, container, false);
    }

    private void publicar(){
        Log.e("TIMELINE","PUBLICANDOP");
        Intent intent = new Intent(getContext(), NewPublicationActivity.class);
        intent.putExtra("userEmail",userEmail);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        publicarButton = view.findViewById(R.id.publicarButton);
        publicarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeLineFragment.this.publicar();
            }
        });
        postRecyclerView = view.findViewById(R.id.postRecyclerView);

        DAOPost.getInstance().getAllPost(new DAOPost.PostReturn() {
            @Override
            public void onSuccess(Task<QuerySnapshot> task) {
                List<DTOPost> posts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    posts.add(document.toObject(DTOPost.class));
                }
                new PostsRecyclerViewConfig().setConfig(postRecyclerView,getContext(), posts);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        DAOPost.getInstance().getAllPost(new DAOPost.PostReturn() {
            @Override
            public void onSuccess(Task<QuerySnapshot> task) {
                List<DTOPost> posts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    posts.add(document.toObject(DTOPost.class));
                }
                new PostsRecyclerViewConfig().setConfig(postRecyclerView,getContext(), posts);
            }
        });
    }
}
