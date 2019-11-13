package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;


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



        public PostItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.post_list_item, parent, false));
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            postUserNameTextView = itemView.findViewById(R.id.postUserNameTextView);
            postDateTextView = itemView.findViewById(R.id.postDateTextView);
            postDescriptionTextView = itemView.findViewById(R.id.postDescriptionTextView);
            postImage = itemView.findViewById(R.id.postImage);
            postVideo = itemView.findViewById(R.id.postVideo);


            WebView postVideo = (WebView)itemView.findViewById(R.id.postVideo);
            postVideo.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            WebSettings webSettings = postVideo.getSettings();
            webSettings.setJavaScriptEnabled(true);
            postVideo.loadData("https://www.youtube.com/embed/lY2H2ZP56K4", "text/html", "utf-8");
        }
        public void bind(DTOPost post){
            postUserNameTextView.setText(post.userEmail);
            postDateTextView.setText(post.date.toString());
            postDescriptionTextView.setText(post.description);

            if(post.image != null){
                Log.e("POSTS", post.image);
                Picasso.get().load(post.image).into(postImage);
                postImage.setVisibility(View.VISIBLE);
            }else if(post.videoUrl != null){
                postVideo.setVisibility(View.VISIBLE);
            }

            DAOUser.getInstance().getUser(post.userEmail, new DAOUser.UserStatus() {
                @Override
                public void onSuccess(DTOUser user) {
                    setImage(user.userInfo.profilePhoto);
                }

                @Override
                public void onFailure() {

                }
            });


        }
        public void setImage(String url){
            Picasso.get().load(url).into(userProfileImage);
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
