package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.firebase.MessagingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        IsPlayServicesAvailable();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()) {

                    Log.d("FCM", "Connect failed " + task.getException());
                    task.getResult();
                    return;
                }

                Log.d("FCM", "Connect successfully: " + task.getResult());
            }
        });
    }

    private void setListeners() {
        binding.textCreateAccount.setOnClickListener(view ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
    }

    //Testing
    protected void IsPlayServicesAvailable() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS){
            Log.d("FCM", "isGooglePlayServicesAvailable SUCCESS");
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode, 1).show();
        }
    }
}