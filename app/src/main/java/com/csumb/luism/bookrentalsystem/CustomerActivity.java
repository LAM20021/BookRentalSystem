package com.csumb.luism.bookrentalsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.text.TextUtils;
import android.util.Patterns;



import androidx.appcompat.app.AppCompatActivity;


public class CustomerActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createAccountButton, backButton;
    private BookRentalSystemDao bookRentalSystemDao;

    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 2; // Maximum number of login attempts allowed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        bookRentalSystemDao = BookRentalSystemDatabase.getDatabase(this).getBookRentalSystemDao();

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidInput(username, password)) {
                    if (isUsernameAvailable(username)) {
                        Customer newCustomer = new Customer(username, password);
                        insertCustomerIntoDatabase(newCustomer);

                        Toast.makeText(CustomerActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                        usernameEditText.setText("");
                        passwordEditText.setText("");

                        navigateToMainMenu();
                    } else {
                        handleUsernameTaken();
                    }
                } else {
                    handleInvalidInput();
                }
            }
        });


    }

    private boolean isUsernameAvailable(String username) {
        return bookRentalSystemDao.checkUsernameAvailability(username).isEmpty();
    }

    private void insertCustomerIntoDatabase(Customer customer) {
        bookRentalSystemDao.insert(customer);

        logOperation("New Account", "Customer: " + customer.getUsername());
    }

    private void handleInvalidInput() {
        Toast.makeText(CustomerActivity.this, "Invalid input. Please check your entries.", Toast.LENGTH_SHORT).show();

        loginAttempts++;

        if (loginAttempts == 1) {
            Toast.makeText(CustomerActivity.this, "You have one more attempt.", Toast.LENGTH_SHORT).show();
        } else if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            Toast.makeText(CustomerActivity.this, "Maximum login attempts reached. Returning to main menu.", Toast.LENGTH_SHORT).show();
            navigateToMainMenu();
        } else {
            // Optionally, clear the input fields for another attempt
            usernameEditText.setText("");
            passwordEditText.setText("");
        }
    }


    private void handleUsernameTaken() {
        Toast.makeText(CustomerActivity.this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();

        loginAttempts++;

        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            Toast.makeText(CustomerActivity.this, "Maximum login attempts reached. Returning to main menu.", Toast.LENGTH_SHORT).show();
            navigateToMainMenu();
        }
    }

    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logOperation(String operationType, String details) {
        String currentDateAndTime = getCurrentDateAndTime();

        LogEntry logEntry = new LogEntry(operationType, details, currentDateAndTime);

        bookRentalSystemDao.insertLogEntry(logEntry);
    }

    private String getCurrentDateAndTime() {
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return dateFormat.format(currentDate);
    }

    private boolean isValidInput(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }

        if (!isValidUsername(username) || !isValidPassword(password)) {
            return false;
        }

        return true;
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 4 || username.length() > 10) {
            return false;
        }

        int alphabeticCount = 0;
        for (char c : username.toCharArray()) {
            if (Character.isLetter(c)) {
                alphabeticCount++;
            }
        }
        if (alphabeticCount < 3) {
            return false;
        }

        return !username.equalsIgnoreCase("Admin2");
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 4 || password.length() > 10) {
            return false;
        }

        if (!containsNumber(password)) {
            return false;
        }

        if (containsSpecialSymbol(password)) {
            return false;
        }

        return true;
    }

    private boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialSymbol(String str) {
        return !str.matches("[a-zA-Z0-9]+");
    }

}