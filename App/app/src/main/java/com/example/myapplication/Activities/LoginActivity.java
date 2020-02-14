package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.DAO.DAOAuthentication;
import com.example.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "Login Activity";
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void login(View v) {
        int i = v.getId();
        if (i == R.id.signButton) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Required.");
                return;
            }

            DAOAuthentication.getInstance(this).authWithEmail(email, password, new DAOAuthentication.UserStatus() {
                @Override
                public void LoginSuccess(FirebaseUser user) {
                    Log.d(TAG, "authWithCredential:success");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userEmail", user.getEmail());
                    Toast.makeText(getApplicationContext(),user.getEmail(), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

                @Override
                public void LoginError(String message) {
                    Log.d(TAG, "authWithCredential:failed");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(i == R.id.registerButton){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void forgottenPassword(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra("forgotPassword", "true");
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                DAOAuthentication.getInstance(this).authWithGoogle(account, new DAOAuthentication.UserStatus() {
                    @Override
                    public void LoginSuccess(FirebaseUser user) {
                        Log.d(TAG, "signInWithCredential:success");
                        Log.d(TAG, "authWithCredential:success");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("userEmail", user.getEmail());
                        Toast.makeText(getApplicationContext(),user.getEmail(), Toast.LENGTH_LONG).show();
                        startActivity(intent);





                    }

                    @Override
                    public void LoginError(String message) {
                        Log.e(TAG, "signInWithCredential:failed");
                    }
                });

                //firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }


}
