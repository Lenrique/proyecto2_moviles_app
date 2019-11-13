package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsRecyclerViewConfig {
    private Context context;
    private PostAdapter postAdapter;
    public void setConfig(RecyclerView  recyclerView, Context context, List<DTOPost> posts){
        this.context = context;
        postAdapter = new PostAdapter(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(postAdapter);
    }

    class PostItemView extends RecyclerView.ViewHolder {
        private CircleImageView userProfileImage;
        private TextView postUserNameTextView, postDateTextView, postDescriptionTextView;
        private ImageView postImage;
        private WebView postVideo;


        public PostItemView(ViewGroup itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            postUserNameTextView = itemView.findViewById(R.id.postUserNameTextView);
            postDateTextView = itemView.findViewById(R.id.postDateTextView);
            postDescriptionTextView = itemView.findViewById(R.id.postDescriptionTextView);
            postImage = itemView.findViewById(R.id.postImage);
            postVideo = itemView.findViewById(R.id.postVideo);
        }
        public void bind(DTOPost post){
            postUserNameTextView.setText(post.userEmail);
            postDateTextView.setText(post.date.toString());
            postDescriptionTextView.setText(post.description);


        }
    }

    class PostAdapter extends RecyclerView.Adapter<PostItemView>{
        private List<DTOPost> postsList;

        public PostAdapter(List<DTOPost> postsList){
            this.postsList = postsList;
        }

        @Override
        public PostItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostItemView holder, int position) {
            holder.bind(postsList.get(position));
        }
        @Override
        public int getItemCount() {
            return postsList.size();
        }
    }
}
