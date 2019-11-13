package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Activities.ProfileActivity;
import com.example.myapplication.DAO.DAOSearchResult;
import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.Fragments.ProfileFragment;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerViewConfig {
    private Context context;
    private SearchAdapter searchAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<DAOSearchResult> searchResults){
        this.context = context;
        searchAdapter = new SearchAdapter(searchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(searchAdapter);

    }


    class SearchItemView extends RecyclerView.ViewHolder {
        private TextView typeTextView, nameTextView, descriptionTextView;
        private CircleImageView profile_image;



        public SearchItemView (ViewGroup parent){
            super(LayoutInflater.from(context).inflate(R.layout.search_list_item,parent,false));
            typeTextView = itemView.findViewById(R.id.typeTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            profile_image = itemView.findViewById(R.id.profile_image);
        }

        public void bind(DAOSearchResult daoSearchResult){
            typeTextView.setText(daoSearchResult.type);
            nameTextView.setText(daoSearchResult.userName);
            descriptionTextView.setText(daoSearchResult.description);

            DAOUser.getInstance().getUser(daoSearchResult.userEmail, new DAOUser.UserStatus() {
                @Override
                public void onSuccess(DTOUser user) {
                    setImage(user.userInfo.profilePhoto);
                    setName(user.userInfo.name+" "+user.userInfo.lastName);
                }

                @Override
                public void onFailure() {

                }
            });
            profile_image.setTag(daoSearchResult.userEmail);
            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPref = context.getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userEmail", profile_image.getTag().toString());
                    editor.apply();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);

                }
            });

        }
        public void setImage(String url){
            Picasso.get().load(url).into(profile_image);
        }
        public void setName(String name) {nameTextView.setText(name);}

        public void write(){

        }
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchItemView>{
        private List<DAOSearchResult> searchResults;

        public SearchAdapter (List<DAOSearchResult> searchResults){this.searchResults = searchResults;}

        @NonNull
        @Override
        public SearchItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SearchItemView(parent);
        }
        @Override
        public void onBindViewHolder(@NonNull SearchItemView holder, int position) {
            holder.bind(searchResults.get(position));
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }

    }
}
