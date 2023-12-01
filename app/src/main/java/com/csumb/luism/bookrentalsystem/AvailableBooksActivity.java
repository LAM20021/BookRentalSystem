package com.csumb.luism.bookrentalsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import java.util.List;

public class AvailableBooksActivity extends AppCompatActivity {

    private TextView pickupDateTextView, returnDateTextView;
    private ListView availableBooksListView;
    private BookRentalSystemDao bookRentalSystemDao;
    private BookRentalSystemDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_books);

        bookRentalSystemDao = BookRentalSystemDatabase.getDatabase(this).getBookRentalSystemDao();
        db = BookRentalSystemDatabase.getDatabase(this);

        pickupDateTextView = findViewById(R.id.textViewPickupDate);
        returnDateTextView = findViewById(R.id.textViewReturnDate);
        availableBooksListView = findViewById(R.id.listViewAvailableBooks);

        int pickupDate = getIntent().getIntExtra("pickupDate", 0);
        int returnDate = getIntent().getIntExtra("returnDate", 0);

        pickupDateTextView.setText("Pickup Date: December " + pickupDate + " 2023");
        returnDateTextView.setText("Return Date: December " + returnDate + " 2023");

        List<Book> availableBooks = bookRentalSystemDao.getAllBooks();
        setupAvailableBooksListView(availableBooks, pickupDate, returnDate);
    }

    private void setupAvailableBooksListView(final List<Book> availableBooks, final int pickupDate, final int returnDate) {
        final AvailableBooksAdapter adapter = new AvailableBooksAdapter(this, R.layout.list_item_available_books, availableBooks);
        availableBooksListView.setAdapter(adapter);

        availableBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book selectedBook = availableBooks.get(position);


                adapter.setSelectedItemPosition(position);


                showCustomerLoginDialog(selectedBook, pickupDate, returnDate);
            }
        });
    }

    private void showCustomerLoginDialog(final Book selectedBook, final int pickupDate, final int returnDate) {
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

                if (!isBookAvailableForSelectedDates(selectedBook, pickupDate, returnDate)) {
                    // Book is not available for the chosen dates, show a toast and prevent login
                    Toast.makeText(AvailableBooksActivity.this, "This book is not available for the chosen dates.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (verifyCustomerLogin(username, password)) {
                    Intent intent = new Intent(AvailableBooksActivity.this, ConfirmationActivity.class);
                    intent.putExtra("customerUsername", username);
                    intent.putExtra("selectedBookTitle", selectedBook.getTitle());
                    intent.putExtra("pickupDate", pickupDate);
                    intent.putExtra("returnDate", returnDate);
                    intent.putExtra("feePerDay", selectedBook.getFee());
                    startActivity(intent);
                } else {
                    // Invalid login, show a message
                    Toast.makeText(AvailableBooksActivity.this, "Invalid username or password. You have one more attempt.", Toast.LENGTH_SHORT).show();
                    showCustomerLoginDialogWithRetry(selectedBook, pickupDate, returnDate);
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

    private void showCustomerLoginDialogWithRetry(final Book selectedBook, final int pickupDate, final int returnDate) {
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
                    Intent intent = new Intent(AvailableBooksActivity.this, ConfirmationActivity.class);
                    intent.putExtra("customerUsername", username);
                    intent.putExtra("selectedBookTitle", selectedBook.getTitle());
                    intent.putExtra("pickupDate", pickupDate);
                    intent.putExtra("returnDate", returnDate);
                    intent.putExtra("feePerDay", selectedBook.getFee());
                    startActivity(intent);
                } else {
                    Toast.makeText(AvailableBooksActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                    navigateToMainMenu();
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

    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isBookAvailableForSelectedDates(Book selectedBook, int pickupDate, int returnDate) {
        char[] availability = selectedBook.getAvailability();

        for (int i = pickupDate; i <= returnDate; i++) {
            if (availability[i] == '-') {
                // Book is not available for the chosen dates
                return false;
            }
        }

        // Book is available for the chosen dates
        return true;
    }

}
