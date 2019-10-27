package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.myapplication.FirebaseHelpers.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button registerButton;
    String forgotPassword;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        forgotPassword = getIntent().getStringExtra("forgotPassword");

        if (forgotPassword != null) {
            passwordEditText.setVisibility(View.INVISIBLE);
            registerButton.setText("Send Email");
        }

    }

    public void register(View v) {
        String email = emailEditText.getText().toString();

        if (forgotPassword != null) {
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Required.");
                return;
            }
            FirebaseUserHelper.getInstance(this).resetPassword(email, new FirebaseUserHelper.UserReset() {
                @Override
                public void ResetSuccess() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void ResetError() {
                    Toast.makeText(getApplicationContext(), "Error while sending email", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Required.");
                return;
            }
            FirebaseUserHelper.getInstance(this).createAccount(email, password, new FirebaseUserHelper.UserStatus() {
                @Override
                public void LoginSuccess(FirebaseUser user) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void LoginError(String message) {

                }
            });
        }
    }

}
