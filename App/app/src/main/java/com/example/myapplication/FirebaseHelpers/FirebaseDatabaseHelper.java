package com.example.myapplication.FirebaseHelpers;

import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference users;
    private static final String TAG = "FirebaseDataBaseHelper";
    private static FirebaseDatabaseHelper single_instace = null;

    public interface DataStatus{
        void DataInserted();
    }

    private FirebaseDatabaseHelper(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
    }

    public static  FirebaseDatabaseHelper getInstance(){
        if(single_instace == null)
            single_instace = new FirebaseDatabaseHelper();
        return single_instace;
    }

    public void addUser(User user, final DataStatus dataStatus){
        String id = users.push().getKey();
        user.userInfo.userId = id;
        users.child(id).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataInserted();
                    }
                });
    }

}
