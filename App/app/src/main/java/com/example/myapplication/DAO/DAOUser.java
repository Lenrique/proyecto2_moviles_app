package com.example.myapplication.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.DTO.DTOUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DAOUser {

    private static final String TAG = "DAOUser";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public interface UserStatus{
        void onSuccess(DTOUser user);
        void onError();
        void onFailure();
    }

    public void update(DTOUser user){

    }

    public void addUser(final DTOUser DTOUser, final UserStatus userStatus) {

        this.getUser(DTOUser.DTOUserInfo.email, new UserStatus() {
            @Override
            public void onSuccess(com.example.myapplication.DTO.DTOUser user) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onFailure() {
                firestore.collection("users").document(DTOUser.DTOUserInfo.email).set(DTOUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });
    }

    public void getUser(String userEmail, final UserStatus userStatus){
        DocumentReference docRef = firestore.collection("users").document(userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        DTOUser DTOUser = document.toObject(DTOUser.class);
                        userStatus.onSuccess(DTOUser);
                    } else {
                        userStatus.onFailure();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void addPost(String userEmail, final UserStatus userStatus){

    }
}
