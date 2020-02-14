package com.example.myapplication.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Adapters.PostsRecyclerViewConfig;
import com.example.myapplication.Adapters.SearchRecyclerViewConfig;
import com.example.myapplication.DAO.DAOPost;
import com.example.myapplication.DAO.DAOSearchResult;
import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private Button buscarButton;
    private RecyclerView buscarRecyclerView;
    private EditText buscarEditText;
    private String userEmail;
    private List<DAOSearchResult> searchResultList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userEmail = getArguments().getString("userEmail");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buscarButton = view.findViewById(R.id.buscarButton);
        buscarRecyclerView = view.findViewById(R.id.buscarRecyclerView);
        buscarEditText = view.findViewById(R.id.buscarEditText);

        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

    }

    public void buscar(){
        searchResultList.clear();
        final String query = buscarEditText.getText().toString();
        if (TextUtils.isEmpty(query)) {
            buscarEditText.setError("Required.");
            return;
        }
        DAOPost.getInstance().getAllPost(new DAOPost.PostReturn() {
            @Override
            public void onSuccess(Task<QuerySnapshot> task) {
                DAOSearchResult searchResult;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DTOPost post = document.toObject(DTOPost.class);
                    if(post.description.contains(query)) {
                        searchResult = new DAOSearchResult();
                        searchResult.description = post.description;
                        searchResult.type = "Post";
                        searchResult.userEmail = post.userEmail;
                        searchResultList.add(searchResult);
                    }
                }
                finish();
            }
        });
        DAOUser.getInstance().getAllUsers(new DAOUser.UserReturn() {
            @Override
            public void onSuccess(Task<QuerySnapshot> task) {
                DAOSearchResult searchResult;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DTOUser user = document.toObject(DTOUser.class);
                    if(user.userInfo.name.contains(query) || user.userInfo.lastName.contains(query) )  {
                        searchResult = new DAOSearchResult();
                        searchResult.type = "User";
                        searchResult.userEmail = user.userInfo.email;
                        searchResultList.add(searchResult);
                    }
                }
                finish();
            }
        });
    }
    public void finish(){
        new SearchRecyclerViewConfig().setConfig(buscarRecyclerView,getContext(), searchResultList);
    }
}
