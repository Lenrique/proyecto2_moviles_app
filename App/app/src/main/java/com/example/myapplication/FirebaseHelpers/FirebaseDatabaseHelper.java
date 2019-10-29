package com.example.myapplication.FirebaseHelpers;

import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference users;

    public interface DataStatus{
        void DataInserted();
    }
    public FirebaseDatabaseHelper(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
    }

    public void addUser(User user, final DataStatus dataStatus){
        String id = users.push().getKey();
        user.id = id;
        users.child(id).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataInserted();
                    }
                });
    }

}
