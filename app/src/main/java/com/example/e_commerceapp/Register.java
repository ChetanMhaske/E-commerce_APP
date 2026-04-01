package com.example.e_commerceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    // UI Components
    private LottieAnimationView animationView;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private AppCompatButton buttonRegister;
    private TextView textViewLogin;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Apply window insets for edge-to-edge display
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize UI components
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        animationView = findViewById(R.id.animation_view);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
    }

    private void setClickListeners() {
        buttonRegister.setOnClickListener(v -> performRegistration());
        textViewLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void performRegistration() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!validateInputs(email, password, confirmPassword)) {
            return;
        }

        buttonRegister.setText("Creating Account...");
        buttonRegister.setEnabled(false);

        createUserAccount(email, password);
    }

    private boolean validateInputs(String email, String password, String confirmPassword) {
        clearErrors();
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            editTextEmail.requestFocus();
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            isValid = false;
        } else if (password.length() < 8) {
            editTextPassword.setError("Password must be at least 8 characters long");
            editTextPassword.requestFocus();
            isValid = false;
        } else if (!isPasswordStrong(password)) {
            editTextPassword.setError("Password must contain at least one uppercase letter, one lowercase letter, and one number");
            editTextPassword.requestFocus();
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Please confirm your password");
            editTextConfirmPassword.requestFocus();
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        boolean hasUppercase = false, hasLowercase = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUppercase && hasLowercase && hasDigit;
    }

    private void clearErrors() {
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);
    }

    /**
     * Firebase Registration
     */
    private void createUserAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    buttonRegister.setText("Sign Up");
                    buttonRegister.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Register.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        navigateAfterRegistration();
                    } else {
                        Toast.makeText(Register.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void navigateAfterRegistration() {
        // Option 1: go back to Login
        Intent intent = new Intent(Register.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        // Option 2 (auto-login directly to MainActivity):
        // Intent intent = new Intent(Register.this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
        // finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) editTextEmail.setError(null); });
        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) editTextPassword.setError(null); });
        editTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) editTextConfirmPassword.setError(null); });
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToLogin();
    }
}
