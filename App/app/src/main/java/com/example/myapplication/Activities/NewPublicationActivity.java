package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.DAO.DAOImage;
import com.example.myapplication.DAO.DAOPost;
import com.example.myapplication.DAO.DAOUser;
import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class NewPublicationActivity extends AppCompatActivity {

    private static final String TAG = "NewPublicationActivity";
    private String userEmail;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText descriptionEditText, videoEditText;
    private Button imageVideo, imageButton, publicarButton;
    private Uri image;
    private String tipo, profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);
        initComponents();
        initListeners();
        userEmail =  getIntent().getStringExtra("userEmail");
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void publicar(){
        final String descripcion = descriptionEditText.getText().toString();
        if (TextUtils.isEmpty(descripcion)) {
            descriptionEditText.setError("Required.");
            return;
        }


        DAOUser.getInstance().getUser(userEmail, new DAOUser.UserStatus() {
            @Override
            public void onSuccess(DTOUser user) {
                asignPhoto(user.userInfo.profilePhoto);
            }

            @Override
            public void onFailure() {

            }
        });


        if(image != null && tipo.equals("image")){
            DAOImage daoImage = new DAOImage();
            daoImage.insertImage(image, getFileExtention(image), new DAOImage.ImageStatus() {
                @Override
                public void onSuccess(String image) {
                    DTOPost post = new DTOPost();
                    post.date = Calendar.getInstance().getTime();
                    post.image = image;
                    post.userEmail = userEmail;
                    post.description = descripcion;


                    DAOPost.getInstance().addPost(post, new DAOPost.PostStatus() {
                        @Override
                        public void onSuccess(DTOPost post) {
                            Log.e(TAG, userEmail);
                            DAOUser.getInstance().addPost(userEmail,post.getPostId());
                            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                            back();
                        }
                        @Override
                        public void onFailure() {
                        }
                    });
                }
                @Override
                public void onFailure() {

                }
            });
        }else if(tipo.equals("video")){
            String videoUrl = videoEditText.getText().toString();
            if (TextUtils.isEmpty(videoUrl)) {
                videoEditText.setError("Required.");
            }else{
                DTOPost post = new DTOPost();
                post.date = Calendar.getInstance().getTime();
                post.videoUrl = videoUrl;
                post.userEmail = userEmail;
                post.description = descripcion;

                DAOPost.getInstance().addPost(post, new DAOPost.PostStatus() {
                    @Override
                    public void onSuccess(DTOPost post) {
                        Log.e(TAG, userEmail);
                        DAOUser.getInstance().addPost(userEmail,post.getPostId());
                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        }else{
            DTOPost post = new DTOPost();
            post.date = Calendar.getInstance().getTime();
            post.userEmail = userEmail;
            post.description = descripcion;

            DAOPost.getInstance().addPost(post, new DAOPost.PostStatus() {
                @Override
                public void onSuccess(DTOPost post) {
                    Log.e(TAG, userEmail);
                    DAOUser.getInstance().addPost(userEmail,post.getPostId());
                    Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure() {

                }
            });
        }

    }
    public void asignPhoto(String photo){
        profilePhoto = photo;
    }
    public void back(){
        this.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image = data.getData();
            Picasso.get().load(image).into(imageView);

        }
    }

    private void initComponents(){
        imageView = findViewById(R.id.image);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        imageVideo = findViewById(R.id.imageVideo);
        imageButton = findViewById(R.id.imageButton);
        publicarButton = findViewById(R.id.buttonPublicar);
        videoEditText = findViewById(R.id.videoEditText);
    }
    private void initListeners(){
        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = "video";
                imageView.setVisibility(View.GONE);
                videoEditText.setVisibility(View.VISIBLE);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = "image";
                imageView.setVisibility(View.VISIBLE);
                videoEditText.setVisibility(View.INVISIBLE);

                openFileChooser();
            }
        });
        publicarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicar();
            }
        });

    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
