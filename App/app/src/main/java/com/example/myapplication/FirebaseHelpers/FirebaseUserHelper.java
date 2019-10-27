package com.example.myapplication.FirebaseHelpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseUserHelper {
    private FirebaseAuth firebaseAuth;
    private Activity activity;
    private static final String TAG = "FirebaseUserHelper";
    private static FirebaseUserHelper single_instance = null;

    private FirebaseUserHelper(Activity activity){
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseUserHelper getInstance(Activity activity){
        if(single_instance == null)
            single_instance = new FirebaseUserHelper(activity);
        return single_instance;

    }

    public interface UserStatus{
        void LoginSuccess(FirebaseUser user);
        void LoginError(String message);
    }

    public interface UserReset{
        void ResetSuccess();
        void ResetError();
    }

    public void authWithGoogle(GoogleSignInAccount account, final UserStatus userStatus){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    userStatus.LoginSuccess(user);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.getException());
                    userStatus.LoginError(task.getException().getLocalizedMessage());
                }

            }
        });
    }

    public void createAccount(String email, String password, final UserStatus userStatus){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userStatus.LoginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            userStatus.LoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void authWithEmail(String email, String password, final UserStatus userStatus){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userStatus.LoginSuccess(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            userStatus.LoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void resetPassword(String email, final UserReset userReset){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    userReset.ResetSuccess();
                }else{
                    userReset.ResetError();
                }
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

}
