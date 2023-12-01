package com.csumb.luism.bookrentalsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;

public class CancelHoldActivity extends AppCompatActivity {

    private Button cancelHoldButton;
    private BookRentalSystemDao bookRentalSystemDao;
    private ListView reservedBooksListView;
    private ReservedBooksAdapter reservedBooksAdapter; // Create an adapter for the ListView

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_hold);
        bookRentalSystemDao = BookRentalSystemDatabase.getDatabase(this).getBookRentalSystemDao();

        // Assuming you have a UI component for the customer to select the book to cancel the hold
        // and a button to trigger the cancellation process

        // Assuming you have a ListView in your layout with the id reservedBooksListView
        reservedBooksListView = findViewById(R.id.reservedBooksListView);

        // Initialize the adapter
        reservedBooksAdapter = new ReservedBooksAdapter(this, R.layout.item_reserved_book, new ArrayList<>());
        reservedBooksListView.setAdapter(reservedBooksAdapter);

        // Set an item click listener for the reservedBooksListView
        reservedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the book selection
                Book selectedBook = reservedBooksAdapter.getItem(position);

                // Update the adapter to highlight the selected item
                reservedBooksAdapter.setSelectedItemPosition(position);

                // Now you can perform any additional actions based on the selected book,
                // e.g., show details, proceed to cancellation, etc.
                // For now, let's just log the selected book's title
                if (selectedBook != null) {
                    String selectedBookTitle = selectedBook.getTitle();
                    // Log or perform any other action with the selected book title
                }
            }
        });

        cancelHoldButton = findViewById(R.id.cancelHoldButton);
        cancelHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve selected book title from the adapter
                int selectedPosition = reservedBooksAdapter.getSelectedItemPosition();
                if (selectedPosition != -1) {
                    Book selectedBook = reservedBooksAdapter.getItem(selectedPosition);
                    if (selectedBook != null) {
                        String selectedBookTitle = selectedBook.getTitle();

                        bookRentalSystemDao.cancelBookHold(selectedBookTitle);
                        // Insert log entry for the cancellation
                        String customerUsername = getIntent().getStringExtra("customerUsername");
                        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        LogEntry logEntry = new LogEntry("Cancel Hold", "Customer: " + customerUsername +
                                ", Book Title: " + selectedBookTitle, timestamp);
                        bookRentalSystemDao.insertLogEntry(logEntry);
                    }
                }

                navigateToMainMenu();
            }
        });

        // Assuming you have the logged-in customer's username
        String customerUsername = getIntent().getStringExtra("customerUsername"); // Replace with actual customer username

        // Load reserved books for the logged-in customer
        loadReservedBooksForCustomer(customerUsername);

    }

    private void loadReservedBooksForCustomer(String customerUsername) {
        // Retrieve the list of reserved books for the customer from the database
        List<Book> reservedBooks = bookRentalSystemDao.getReservedBooksForCustomer(customerUsername);

        if (reservedBooks.isEmpty()) {
            // If there are no reserved books, show a dialog and navigate back to the main menu
            showNoReservationsDialog();
        } else {
            // Load the reserved books into the adapter
            reservedBooksAdapter.clear();
            reservedBooksAdapter.addAll(reservedBooks);
            reservedBooksAdapter.notifyDataSetChanged();
        }
    }

    private void showNoReservationsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Reservations");
        builder.setMessage("You have no reserved books.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog when OK is clicked
                dialogInterface.dismiss();
                // Navigate back to the main menu
                navigateToMainMenu();
            }
        });

        builder.create().show();
    }


    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: finish the CancelHoldActivity so that it's not in the back stack
    }
}
