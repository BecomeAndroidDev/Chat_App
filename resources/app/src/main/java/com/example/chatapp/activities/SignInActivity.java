package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.firebase.MessagingService;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        binding.buttonSignIn.setOnClickListener(view -> {
            if(isValidSignIn()) {
                signIn();
            }
        });
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null
                        && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME,
                                documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE,
                                documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        loading(false);
                        showToast("Unable SignIn");
                    }
                });
    }

    private boolean isValidSignIn() {
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        if(email.isEmpty()) {
            showToast("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email is invalid");
            return false;
        } else if(password.isEmpty()) {
            showToast("Enter password");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignIn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
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