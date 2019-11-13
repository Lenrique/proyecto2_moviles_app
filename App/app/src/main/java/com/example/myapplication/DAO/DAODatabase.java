package com.example.myapplication.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.DTO.DTOPost;
import com.example.myapplication.DTO.DTOUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class DAODatabase {

    private FirebaseDatabase database;
    private static final String TAG = "FirebaseDataBaseHelper";
    private static DAODatabase single_instace = null;
    private FirebaseFirestore firestore;

    public interface DataStatus {
        void DataInserted();
        void DataFailure();
        void DataUser(DTOUser DTOUser);
    }

    private DAODatabase() {
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public static DAODatabase getInstance() {
        if (single_instace == null)
            single_instace = new DAODatabase();
        return single_instace;
    }





    public void addUser(final DTOUser DTOUser, final DataStatus dataStatus) {
        this.getUser(DTOUser.userInfo.email, new DataStatus() {
            @Override
            public void DataFailure() {
                firestore.collection("users").document(DTOUser.userInfo.email).set(DTOUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dataStatus.DataInserted();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dataStatus.DataFailure();
                            }
                        });
            }
            @Override
            public void DataInserted() {

            }
            @Override
            public void DataUser(DTOUser DTOUser) {

            }
        });
    }

    public void getUser(String userEmail, final DataStatus dataStatus){
        DocumentReference docRef = firestore.collection("users").document(userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        DTOUser DTOUser = document.toObject(DTOUser.class);
                        dataStatus.DataUser(DTOUser);
                    } else {
                        dataStatus.DataFailure();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
            }
        }});


    }

}
