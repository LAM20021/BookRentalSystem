package com.csumb.luism.bookrentalsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {
    private BookRentalSystemDao bookRentalSystemDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        bookRentalSystemDao = BookRentalSystemDatabase.getDatabase(this).getBookRentalSystemDao();

        TextView textViewCustomerUsername = findViewById(R.id.textViewCustomerUsername);
        TextView textViewPickupDate = findViewById(R.id.textViewPickupDate);
        TextView textViewReturnDate = findViewById(R.id.textViewReturnDate);
        TextView textViewBookTitle = findViewById(R.id.textViewBookTitle);
        TextView textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        Button buttonConfirm = findViewById(R.id.buttonConfirm);

        String customerUsername = getIntent().getStringExtra("customerUsername");
        int pickupDate = getIntent().getIntExtra("pickupDate", 0);
        int returnDate = getIntent().getIntExtra("returnDate", 0);
        String bookTitle = getIntent().getStringExtra("selectedBookTitle");
        double feePerDay = getIntent().getDoubleExtra("feePerDay", 0);



        textViewCustomerUsername.setText("Customer Username: " + customerUsername);
        textViewPickupDate.setText("Pickup Date: December " + pickupDate + " 2023");
        textViewReturnDate.setText("Return Date: December " + returnDate + " 2023");
        textViewBookTitle.setText("Book Title: " + bookTitle);

        int numberOfDays = calculateNumberOfDays(pickupDate, returnDate);
        double totalAmount = feePerDay * (numberOfDays + 1);
        textViewTotalAmount.setText("Total Amount Owed: $" + String.format("%.2f", totalAmount));

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookTitle = getIntent().getStringExtra("selectedBookTitle");
                Book selectedBook = bookRentalSystemDao.getBookByTitle(bookTitle);

                updateBookReservationStatus(selectedBook);

                navigateToMainMenu();
            }
        });
    }

    private int calculateNumberOfDays(int pickupDay, int returnDay) {
        return returnDay - pickupDay;
    }



    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateBookReservationStatus(Book selectedBook) {


        String customerUsername = getIntent().getStringExtra("customerUsername");
        int pickupDate = getIntent().getIntExtra("pickupDate", 0);
        int returnDate = getIntent().getIntExtra("returnDate", 0);
        String bookTitle = getIntent().getStringExtra("selectedBookTitle");
        double feePerDay = getIntent().getDoubleExtra("feePerDay", 0);
        int numberOfDays = calculateNumberOfDays(pickupDate, returnDate);
        double totalAmount = feePerDay * (numberOfDays + 1);

        selectedBook.setReserved(true);

        selectedBook.setCustomerUsername(customerUsername);
        bookRentalSystemDao.updateBookReservationStatus(selectedBook);

        String formattedTotalAmount = String.format(Locale.getDefault(), "%.2f", totalAmount);


        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        LogEntry logEntry = new LogEntry("Place Hold", "Customer: " + customerUsername +
                ", Pickup Date: December " + pickupDate + " 2023, Return Date: December " + returnDate +
                " 2023, Book Title: " + bookTitle + ", Total Amount: $" + formattedTotalAmount, timestamp);
        bookRentalSystemDao.insertLogEntry(logEntry);
    }


}
