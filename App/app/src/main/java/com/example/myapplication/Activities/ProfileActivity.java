package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.PostsRecyclerViewConfig;
import com.example.myapplication.DAO.DAOImage;
import com.example.myapplication.DAO.DAOPost;
import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String userEmail;
    private TextView userNameTextView;
    private RecyclerView userPostsRecylerView;
    private CircleImageView profile_image;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userNameTextView = findViewById(R.id.userNameTextView);
        profile_image = findViewById(R.id.profile_image);
        userPostsRecylerView = findViewById(R.id.userPostsRecylerView);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);
                String current = sharedPref.getString("currentUserEmail","");
                if (current.equals(userEmail))
                    openFileChooser();
            }
        });
        update();
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image = data.getData();
            Picasso.get().load(image).into(profile_image);
            DAOImage daoImage = new DAOImage();
            daoImage.insertImage(image, getFileExtention(image), new DAOImage.ImageStatus() {
                @Override
                public void onSuccess(String image) {
                    DAOUser.getInstance().updatePhoto(userEmail,image);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void update(){
        try {
            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences("UsuarioActual", Context.MODE_PRIVATE);
            userEmail = sharedPref.getString("userEmail","");
            Log.e("t", userEmail);
            Toast.makeText(getApplicationContext(), sharedPref.getString("userEmail",""), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
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
                    if(document.toObject(DTOPost.class).userEmail.equals(userEmail))
                        posts.add(document.toObject(DTOPost.class));
                }
                new PostsRecyclerViewConfig().setConfig(userPostsRecylerView,getApplicationContext(), posts);
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
    public void setImage(String url){
        Picasso.get().load(url).into(profile_image);
    }
}
