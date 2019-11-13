package com.example.myapplication.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.DTO.DTOPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DAOPost {
    private static final String TAG = "DAOPost";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static DAOPost single_reference;

    private DAOPost (){}

    public static DAOPost getInstance(){
        if(single_reference == null)
            single_reference = new DAOPost();
        return single_reference;

    }

    public interface PostStatus{
        void onSuccess(DTOPost post);
        void onFailure();
    }
    public interface PostReturn{
        void onSuccess(Task<QuerySnapshot> task);
    }

    public void getAllPost(final PostReturn postReturn){
        firestore.collection("post").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postReturn.onSuccess(task);
                            /*
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }*/
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addPost(final DTOPost post, final PostStatus postStatus){
        DocumentReference newPostRef = firestore.collection("post").document();

        post.postId = newPostRef.getId();
        newPostRef.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                postStatus.onSuccess(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postStatus.onFailure();
            }
        });

    }
}
