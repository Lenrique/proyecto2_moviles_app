package com.example.myapplication.DAO;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DAOImage {
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

    public interface ImageStatus{
        void onSuccess(Uri image);
        void onFailure();
    }


    public void insertImage(final Uri image, String fileExtention, final ImageStatus imageStatus) {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + fileExtention);
        fileReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageStatus.onSuccess(taskSnapshot.getUploadSessionUri());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageStatus.onFailure();
            }
        });

    }


}
