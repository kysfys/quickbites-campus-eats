
package app.lovable.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signupButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        nameEditText = findViewById(R.id.name_input);
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        confirmPasswordEditText = findViewById(R.id.confirm_password_input);
        signupButton = findViewById(R.id.signup_button);
        loginTextView = findViewById(R.id.login_text);

        // Set signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (validateInputs(name, email, password, confirmPassword)) {
                    // Perform signup operation
                    performSignup(name, email, password);
                }
            }
        });

        // Set login text click listener
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login page
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void performSignup(String name, String email, String password) {
        // In a real app, this would make an API call to register the user
        // For this example, just show a toast and navigate to login
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close signup activity
    }
}
