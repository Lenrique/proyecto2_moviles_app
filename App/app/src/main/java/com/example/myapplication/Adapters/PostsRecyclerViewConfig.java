package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication.Activities.LoginActivity;
import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Activities.ProfileActivity;
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
        private Button like;



        public PostItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.post_list_item, parent, false));
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            postUserNameTextView = itemView.findViewById(R.id.postUserNameTextView);
            postDateTextView = itemView.findViewById(R.id.postDateTextView);
            postDescriptionTextView = itemView.findViewById(R.id.postDescriptionTextView);
            postImage = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.likeButton);





        }
        public void bind(final DTOPost post){
            postUserNameTextView.setText(post.userEmail);
            postDateTextView.setText(post.date.toString());
            postDescriptionTextView.setText(post.description);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, post.userEmail, Toast.LENGTH_LONG).show();
                }
            });










            if(post.image != null){
                Log.e("POSTS", post.image);
                Picasso.get().load(post.image).into(postImage);
                postImage.setVisibility(View.VISIBLE);
            }else if(post.videoUrl != null){
                postVideo = itemView.findViewById(R.id.postVideo);

                post.videoUrl = "https://www.youtube.com/embed/"+post.videoUrl;
                String test = "https://www.youtube.com/embed/"+post.videoUrl;
                Log.e("ERRRRORRR", post.videoUrl  );
                String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"300\" height=\"200\" src=\""+ post.videoUrl +"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";


                postVideo.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                WebSettings webSettings = postVideo.getSettings();
                webSettings.setJavaScriptEnabled(true);
                postVideo.loadData(frameVideo, "text/html", "utf-8");
                postVideo.setVisibility(View.VISIBLE);
            }

            DAOUser.getInstance().getUser(post.userEmail, new DAOUser.UserStatus() {
                @Override
                public void onSuccess(DTOUser user) {
                    setImage(user.userInfo.profilePhoto);
                    setName(user.userInfo.name+" "+user.userInfo.lastName);
                }

                @Override
                public void onFailure() {

                }
            });
            userProfileImage.setTag(post.userEmail);
            userProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPref = context.getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userEmail", userProfileImage.getTag().toString());
                    editor.apply();
                    Intent intent = new Intent(context, ProfileActivity.class);



                    context.startActivity(intent);
                }
            });


        }
        public void setImage(String url){
            Picasso.get().load(url).into(userProfileImage);
        }
        public void setName(String name) {postUserNameTextView.setText(name);}
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
