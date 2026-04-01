package com.example.e_commerceapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {
    
    private EditText editName, editEmail, editCurrentPassword, editNewPassword;
    private Button updateProfileBtn, changePasswordBtn;
    private ImageView backButton;
    private TextView userEmailText;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        backButton = findViewById(R.id.backButton);
        userEmailText = findViewById(R.id.userEmailText);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editCurrentPassword = findViewById(R.id.editCurrentPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
    }

    private void loadUserData() {
        if (currentUser != null) {
            userEmailText.setText(currentUser.getEmail());
            editEmail.setText(currentUser.getEmail());
            if (currentUser.getDisplayName() != null) {
                editName.setText(currentUser.getDisplayName());
            }
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        updateProfileBtn.setOnClickListener(v -> updateProfile());
        
        changePasswordBtn.setOnClickListener(v -> changePassword());
    }

    private void updateProfile() {
        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            editName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(newEmail)) {
            editEmail.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            editEmail.setError("Please enter a valid email");
            return;
        }

        updateProfileBtn.setText("Updating...");
        updateProfileBtn.setEnabled(false);

        // Update display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update email if changed
                        if (!newEmail.equals(currentUser.getEmail())) {
                            updateEmail(newEmail);
                        } else {
                            updateProfileBtn.setText("Update Profile");
                            updateProfileBtn.setEnabled(true);
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        updateProfileBtn.setText("Update Profile");
                        updateProfileBtn.setEnabled(true);
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmail(String newEmail) {
        currentUser.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    updateProfileBtn.setText("Update Profile");
                    updateProfileBtn.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        userEmailText.setText(newEmail);
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changePassword() {
        String currentPassword = editCurrentPassword.getText().toString().trim();
        String newPassword = editNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword)) {
            editCurrentPassword.setError("Current password is required");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            editNewPassword.setError("New password is required");
            return;
        }

        if (newPassword.length() < 6) {
            editNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        changePasswordBtn.setText("Changing...");
        changePasswordBtn.setEnabled(false);

        // Re-authenticate user before changing password
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);
        
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update password
                        currentUser.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    changePasswordBtn.setText("Change Password");
                                    changePasswordBtn.setEnabled(true);
                                    
                                    if (updateTask.isSuccessful()) {
                                        editCurrentPassword.setText("");
                                        editNewPassword.setText("");
                                        Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        changePasswordBtn.setText("Change Password");
                        changePasswordBtn.setEnabled(true);
                        Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}