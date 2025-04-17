
package app.lovable.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import app.lovable.quickbites.utils.DataManager;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextView;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize data manager
        dataManager = new DataManager(this);
        
        // Check if already logged in
        User currentUser = dataManager.getCurrentUser();
        if (currentUser != null) {
            navigateBasedOnRole(currentUser);
            return;
        }

        // Initialize UI components
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupTextView = findViewById(R.id.signup_text);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateInputs(email, password)) {
                    // Perform login operation
                    performLogin(email, password);
                }
            }
        });

        // Set signup text click listener
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to signup page
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        // In a real app, this would make an API call to authenticate
        // For this example, we'll simulate successful login for any email/password
        
        // Create user (in a real app, this would come from backend)
        String userId = "user-" + UUID.randomUUID().toString();
        String name = email.substring(0, email.indexOf('@'));
        String role = email.contains("staff") || email.contains("admin") ? "caterer" : "customer";
        
        User user = new User(userId, name, email, role);
        
        // Save user to local storage
        dataManager.saveCurrentUser(user);
        
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        
        // Navigate based on role
        navigateBasedOnRole(user);
    }
    
    private void navigateBasedOnRole(User user) {
        Intent intent;
        
        if (user.isCaterer()) {
            intent = new Intent(this, DashboardActivity.class);
        } else {
            intent = new Intent(this, MenuActivity.class);
        }
        
        startActivity(intent);
        finish(); // Close login activity
    }
}
