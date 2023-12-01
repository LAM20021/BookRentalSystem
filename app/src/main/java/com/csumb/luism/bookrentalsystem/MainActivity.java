package com.csumb.luism.bookrentalsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAccount, btnPlaceHold, btnCancelHold, btnManageSystem;
    private BookRentalSystemDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnPlaceHold = findViewById(R.id.btnPlaceHold);
        btnCancelHold = findViewById(R.id.btnCancelHold);
        btnManageSystem = findViewById(R.id.btnManageSystem);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CustomerActivity.class));
            }
        });

        btnPlaceHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlaceHoldActivity.class));
            }
        });

        btnCancelHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerLoginDialog();
            }
        });

        btnManageSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });

        appInitialization();
    }

    private void appInitialization() {
        db = BookRentalSystemDatabase.getDatabase(this);

        List<Customer> customerList = db.getBookRentalSystemDao().getAllCustomers();
        if (customerList.size() <= 0) {
            Customer[] defaultCustomers = new Customer[3];
            defaultCustomers[0] = new Customer("alice5", "csumb100");
            defaultCustomers[1] = new Customer("Brian7", "123abc");
            defaultCustomers[2] = new Customer("chris12", "CHRIS12");
            db.getBookRentalSystemDao().insert(defaultCustomers);
        }

        List<Book> bookList = db.getBookRentalSystemDao().getAllBooks();
        if (bookList.size() <= 0) {
            Book[] defaultBooks = new Book[3];
            defaultBooks[0] = new Book("Cool Java", "J. Gross", 1.50);
            defaultBooks[1] = new Book("Fun Java", "Y. Byun", 2.00);
            defaultBooks[2] = new Book("Algorithm for Java", "K. Alice", 2.25);
            db.getBookRentalSystemDao().insert(defaultBooks);
        }

        List<Librarian> librarianList = db.getBookRentalSystemDao().getAllLibrarians();
        if (librarianList.size() <= 0) {
            Librarian defaultLibrarian = new Librarian("Admin2", "Admin2");
            db.getBookRentalSystemDao().insert(defaultLibrarian);
        }
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Librarian Login");

        View view = getLayoutInflater().inflate(R.layout.dialog_librarian_login, null);
        final EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (verifyLibrarianLogin(username, password)) {
                    startActivity(new Intent(MainActivity.this, LibrarianActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. You have one more attempt.", Toast.LENGTH_SHORT).show();
                    showLoginDialogWithRetry();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        builder.create().show();
    }

    private void showLoginDialogWithRetry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Librarian Login (Retry)");

        View view = getLayoutInflater().inflate(R.layout.dialog_librarian_login, null);
        final EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (verifyLibrarianLogin(username, password)) {
                    startActivity(new Intent(MainActivity.this, LibrarianActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. No more attempts.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        builder.create().show();
    }

    private boolean verifyLibrarianLogin(String username, String password) {
        List<Librarian> librarians = db.getBookRentalSystemDao().verifyLibrarianLogin(username, password);
        return !librarians.isEmpty();
    }

    private void showCustomerLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Customer Login");

        View view = getLayoutInflater().inflate(R.layout.dialog_customer_login, null);
        final EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (verifyCustomerLogin(username, password)) {
                    Intent intent = new Intent(MainActivity.this, CancelHoldActivity.class);
                    intent.putExtra("customerUsername", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. You have one more attempt.", Toast.LENGTH_SHORT).show();
                    showCustomerLoginDialogWithRetry();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        builder.create().show();
    }


    private void showCustomerLoginDialogWithRetry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Customer Login (Retry)");

        View view = getLayoutInflater().inflate(R.layout.dialog_customer_login, null);
        final EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (verifyCustomerLogin(username, password)) {
                    Intent intent = new Intent(MainActivity.this, CancelHoldActivity.class);
                    intent.putExtra("customerUsername", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. No more attempts.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.create().show();
    }


    private boolean verifyCustomerLogin(String username, String password) {
        List<Customer> customers = db.getBookRentalSystemDao().verifyCustomerLogin(username, password);
        return !customers.isEmpty();
    }
}
