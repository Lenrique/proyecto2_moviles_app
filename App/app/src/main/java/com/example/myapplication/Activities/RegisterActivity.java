package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.DTO.DTOUser;
import com.example.myapplication.DAO.DAODatabase;
import com.example.myapplication.DAO.DAOAuthentication;
import com.example.myapplication.DTO.DTOUserInfo;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    TextView bornDateTextView;
    EditText emailEditText, passwordEditText, nameEditText,lastNameEditText, phoneEditText, cityEditText;
    Button registerButton;
    String forgotPassword;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Boolean bornDate = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponets();
        isForgotPassword();
        initListeners();
    }

    public void register(View v) {
        final String email = emailEditText.getText().toString();

        if (forgotPassword != null) {
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Required.");
                return;
            }
            DAOAuthentication.getInstance(this).resetPassword(email, new DAOAuthentication.UserReset() {
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

        }

        else {

            String password = passwordEditText.getText().toString();
            final String name = nameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();
            final String city = cityEditText.getText().toString();

            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(lastName)) {
                lastNameEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                phoneEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(city)) {
                cityEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Required.");
                return;
            }
            if (!bornDate) {
                bornDateTextView.setError("Required.");
                return;
            }

            DAOAuthentication.getInstance(this).createAccount(email, password, new DAOAuthentication.UserStatus() {
                @Override
                public void LoginSuccess(FirebaseUser user) {

                    DTOUser newDTOUser = new DTOUser();
                    DTOUserInfo newDTOUserInfo = new DTOUserInfo();
                    newDTOUserInfo.name = name;
                    newDTOUserInfo.lastName = lastName;
                    newDTOUserInfo.bornDate = bornDateTextView.getText().toString();
                    newDTOUserInfo.phone = phone;
                    newDTOUserInfo.city = city;
                    newDTOUserInfo.email = email;
                    newDTOUser.userInfo = newDTOUserInfo;

                    DAODatabase.getInstance().addUser(newDTOUser, new DAODatabase.DataStatus() {
                        @Override
                        public void DataInserted() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void DataFailure() {
                            Toast.makeText(getApplicationContext(), "ERROR Para insertar", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void DataUser(DTOUser DTOUser) {

                        }
                    });
                }

                @Override
                public void LoginError(String message) {

                }
            });
        }
    }

    private void initListeners(){
        bornDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                bornDate = true;
                bornDateTextView.setText(dayOfMonth+"/"+(month + 1) +"/"+year);
            }
        };
    }

    private void initComponets(){
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        bornDateTextView = findViewById(R.id.bornDateTextView);
        nameEditText = findViewById(R.id.nameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        cityEditText = findViewById(R.id.cityEditText);

    }

    private void isForgotPassword(){
        forgotPassword = getIntent().getStringExtra("forgotPassword");

        if (forgotPassword != null) {
            passwordEditText.setVisibility(View.INVISIBLE);
            registerButton.setText("Send Email");
        }
    }

}
